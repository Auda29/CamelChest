package dev.wecke.camelchest.mixin;

import dev.wecke.camelchest.CamelChestRules;
import dev.wecke.camelchest.api.CamelChestAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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
    private void camelchest$writeChestData(CompoundTag tag, CallbackInfo callback) {
        boolean hasChest = camelchest$state().camelchest$hasChest();
        tag.putBoolean("CamelChest", hasChest);

        if (!hasChest) {
            return;
        }

        ListTag items = new ListTag();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
                CompoundTag savedItem = (CompoundTag) stack.save(registryAccess());
                savedItem.putByte("Slot", (byte) slot);
                items.add(savedItem);
            }
        }
        tag.put("CamelChestItems", items);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void camelchest$readChestData(CompoundTag tag, CallbackInfo callback) {
        camelchest$state().camelchest$setChest(tag.getBoolean("CamelChest"));
        createInventory();

        if (!camelchest$state().camelchest$hasChest()) {
            return;
        }

        ListTag items = tag.getList("CamelChestItems", Tag.TAG_COMPOUND);
        for (int index = 0; index < items.size(); index++) {
            CompoundTag savedItem = items.getCompound(index);
            int slot = savedItem.getByte("Slot") & 255;
            if (slot < inventory.getContainerSize()) {
                inventory.setItem(slot, ItemStack.parseOptional(registryAccess(), savedItem));
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
    protected void dropEquipment() {
        super.dropEquipment();
        if (camelchest$state().camelchest$hasChest()) {
            spawnAtLocation(Blocks.CHEST);
            camelchest$state().camelchest$setChest(false);
        }
    }
}
