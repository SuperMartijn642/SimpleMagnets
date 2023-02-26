package com.supermartijn642.simplemagnets.mixin;

import com.supermartijn642.simplemagnets.MagnetItem;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 26/02/2023 by SuperMartijn642
 */
@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    @Inject(
        method = "addPairing",
        at = @At("TAIL")
    )
    private void addPairing(ServerPlayer player, CallbackInfo ci){
        MagnetItem.onStartTracking(this.entity, player);
    }
}
