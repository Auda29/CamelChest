package dev.wecke.camelchest.fabric.mixin;

import dev.wecke.camelchest.api.CamelChestAccess;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camel.class)
public abstract class FabricCamelChestStateMixin extends AbstractHorse implements CamelChestAccess {
    @Unique
    private static final EntityDataAccessor<Boolean> CAMELCHEST_HAS_CHEST =
        SynchedEntityData.defineId(Camel.class, EntityDataSerializers.BOOLEAN);

    protected FabricCamelChestStateMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void camelchest$defineChestData(SynchedEntityData.Builder builder, CallbackInfo callback) {
        builder.define(CAMELCHEST_HAS_CHEST, false);
    }

    @Override
    public boolean camelchest$hasChest() {
        return entityData.get(CAMELCHEST_HAS_CHEST);
    }

    @Override
    public void camelchest$setChest(boolean hasChest) {
        entityData.set(CAMELCHEST_HAS_CHEST, hasChest);
    }
}
