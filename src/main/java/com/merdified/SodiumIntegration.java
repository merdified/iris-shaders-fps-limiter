package com.merdified;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SodiumIntegration implements ConfigEntryPoint {
	private static final Identifier ENABLED_ID = IrisShadersFpsLimiter.id("enabled");
	private static final Identifier OPTION_ID = IrisShadersFpsLimiter.id("shader_fps_cap");

	@Override
	public void registerConfigLate(ConfigBuilder builder) {
		builder.registerOwnModOptions()
				.setIcon(IrisShadersFpsLimiter.id("icon.png"))
				.addPage(builder.createOptionPage()
						.setName(Component.translatable("option.iris-shaders-fps-limiter.page.general"))
						.addOption(builder.createBooleanOption(ENABLED_ID)
								.setName(Component.translatable("option.iris-shaders-fps-limiter.enabled"))
								.setTooltip(Component.translatable("option.iris-shaders-fps-limiter.enabled.tooltip"))
								.setDefaultValue(IrisShadersFpsLimiterConfig.DEFAULT_ENABLED)
								.setBinding(
										enabled -> IrisShadersFpsLimiterConfig.get().setEnabled(enabled),
										() -> IrisShadersFpsLimiterConfig.get().enabled)
								.setStorageHandler(() -> IrisShadersFpsLimiterConfig.get().save()))
						.addOption(builder.createIntegerOption(OPTION_ID)
								.setName(Component.translatable("option.iris-shaders-fps-limiter.shader_fps_cap"))
								.setTooltip(Component.translatable("option.iris-shaders-fps-limiter.shader_fps_cap.tooltip"))
								.setRange(IrisShadersFpsLimiterConfig.MIN_CAP, IrisShadersFpsLimiterConfig.MAX_CAP, 1)
								.setDefaultValue(IrisShadersFpsLimiterConfig.DEFAULT_CAP)
								.setValueFormatter(cap -> Component.translatable("option.iris-shaders-fps-limiter.shader_fps_cap.value", cap))
								.setBinding(
										cap -> IrisShadersFpsLimiterConfig.get().setFpsCap(cap),
										() -> IrisShadersFpsLimiterConfig.get().fpsCap)
								.setStorageHandler(() -> IrisShadersFpsLimiterConfig.get().save())));
	}
}
