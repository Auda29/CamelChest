package dev.wecke.camelchest.mixin;

import dev.wecke.camelchest.CamelChestRules;
import dev.wecke.camelchest.api.CamelChestAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse {
    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private CamelChestAccess camelchest$state() {
        return (CamelChestAccess) (Object) this;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void camelchest$equipChest(
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResult> callback
    ) {
        ItemStack heldStack = player.getItemInHand(hand);
        boolean canEquip = CamelChestRules.canEquip(
            isBaby(),
            camelchest$state().camelchest$hasChest(),
            player.isSecondaryUseActive(),
            heldStack.is(Items.CHEST)
        );

        if (!canEquip) {
            return;
        }

        camelchest$state().camelchest$setChest(true);
        playSound(
            SoundEvents.DONKEY_CHEST,
            1.0F,
            1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F
        );
        heldStack.consume(1, player);
        createInventory();
        callback.setReturnValue(InteractionResult.SUCCESS);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void camelchest$writeChestData(ValueOutput output, CallbackInfo callback) {
        boolean hasChest = camelchest$state().camelchest$hasChest();
        output.putBoolean("CamelChest", hasChest);

        if (!hasChest) {
            return;
        }

        ValueOutput.TypedOutputList<ItemStackWithSlot> items = output.list(
            "CamelChestItems",
            ItemStackWithSlot.CODEC
        );

        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
                items.add(new ItemStackWithSlot(slot, stack));
            }
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void camelchest$readChestData(ValueInput input, CallbackInfo callback) {
        camelchest$state().camelchest$setChest(input.getBooleanOr("CamelChest", false));
        createInventory();

        if (!camelchest$state().camelchest$hasChest()) {
            return;
        }

        for (ItemStackWithSlot savedItem : input.listOrEmpty("CamelChestItems", ItemStackWithSlot.CODEC)) {
            if (savedItem.isValidInContainer(inventory.getContainerSize())) {
                inventory.setItem(savedItem.slot(), savedItem.stack());
            }
        }
    }

    @Override
    public int getInventoryColumns() {
        return CamelChestRules.inventoryColumns(camelchest$state().camelchest$hasChest());
    }

    @Override
    public SlotAccess getSlot(int slot) {
        if (slot != 499) {
            return super.getSlot(slot);
        }

        return new SlotAccess() {
            @Override
            public ItemStack get() {
                return camelchest$state().camelchest$hasChest()
                    ? new ItemStack(Items.CHEST)
                    : ItemStack.EMPTY;
            }

            @Override
            public boolean set(ItemStack stack) {
                if (stack.isEmpty()) {
                    if (camelchest$state().camelchest$hasChest()) {
                        camelchest$state().camelchest$setChest(false);
                        createInventory();
                    }
                    return true;
                }

                if (!stack.is(Items.CHEST)) {
                    return false;
                }

                if (!camelchest$state().camelchest$hasChest()) {
                    camelchest$state().camelchest$setChest(true);
                    createInventory();
                }
                return true;
            }
        };
    }

    @Override
    protected void dropEquipment(ServerLevel level) {
        super.dropEquipment(level);
        if (camelchest$state().camelchest$hasChest()) {
            spawnAtLocation(level, Blocks.CHEST);
            camelchest$state().camelchest$setChest(false);
        }
    }
}
