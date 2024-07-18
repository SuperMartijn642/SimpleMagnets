package com.supermartijn642.simplemagnets.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.supermartijn642.core.ClientUtils;
import com.supermartijn642.simplemagnets.DemagnetizationCoilAreaHighlighter;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created 23/02/2023 by SuperMartijn642
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Unique
    private static PoseStack POSE_STACK = new PoseStack();

    @Inject(
        method = "renderLevel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;",
            shift = At.Shift.AFTER
        )
    )
    private void renderLevel(CallbackInfo ci){
        HitResult hitResult = ClientUtils.getMinecraft().hitResult;
        if(hitResult instanceof BlockHitResult)
            DemagnetizationCoilAreaHighlighter.onDrawHighlight(POSE_STACK, ((BlockHitResult)hitResult));
    }
}
