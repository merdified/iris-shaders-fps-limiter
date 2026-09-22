package com.merdified.mixin;

import com.merdified.IrisIntegration;
import com.merdified.IrisShadersFpsLimiterConfig;
import net.minecraft.client.FramerateLimiter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FramerateLimiter.class)
public abstract class MixinFramerateLimiter {
	@ModifyVariable(method = "limitDisplayFPS", at = @At("HEAD"), argsOnly = true)
	private static int irisShadersFpsLimiter$capDisplayFps(int limit) {
		boolean shadersInUse = IrisIntegration.areShadersInUse();
		return IrisShadersFpsLimiterConfig.get().effectiveLimit(limit, shadersInUse);
	}
}
