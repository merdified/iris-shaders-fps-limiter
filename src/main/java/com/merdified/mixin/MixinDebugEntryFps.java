package com.merdified.mixin;

import com.merdified.IrisIntegration;
import com.merdified.IrisShadersFpsLimiterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryFps;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DebugEntryFps.class)
public abstract class MixinDebugEntryFps {
	@ModifyArgs(
		method = "display",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/components/debug/DebugScreenDisplayer;addPriorityLine(Ljava/lang/String;)V"
		)
	)
	private void irisShadersFpsLimiter$appendCap(Args args) {
		IrisShadersFpsLimiterConfig config = IrisShadersFpsLimiterConfig.get();
		if (!config.enabled) {
			return;
		}
		if (!IrisIntegration.areShadersInUse()) {
			return;
		}
		int cap = IrisShadersFpsLimiterConfig.clamp(config.fpsCap);
		int trackerLimit = Minecraft.getInstance().getFramerateLimitTracker().getFramerateLimit();
		if (cap >= trackerLimit) {
			return;
		}
		String line = args.get(0);
		args.set(0, line + Component.translatable("debug.iris-shaders-fps-limiter.cap_suffix", cap).getString());
	}
}
