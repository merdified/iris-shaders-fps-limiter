package com.merdified;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class IrisShadersFpsLimiterConfigScreen extends Screen {
	private final Screen parent;

	public IrisShadersFpsLimiterConfigScreen(Screen parent) {
		super(Component.translatable("screen.iris-shaders-fps-limiter.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int centerX = this.width / 2;
		int centerY = this.height / 2;

		this.addRenderableOnly(new StringWidget(centerX - 155, centerY - 62, 310, 12, this.title, this.font));

		this.addRenderableWidget(new AbstractSliderButton(
				centerX - 155, centerY - 40, 150, 20,
				label(IrisShadersFpsLimiterConfig.get().fpsCap),
				IrisShadersFpsLimiterConfig.toSliderValue(IrisShadersFpsLimiterConfig.get().fpsCap)) {
			@Override
			protected void updateMessage() {
				this.setMessage(label(IrisShadersFpsLimiterConfig.fromSliderValue(this.value)));
			}

			@Override
			protected void applyValue() {
				IrisShadersFpsLimiterConfig.get().setFpsCap(IrisShadersFpsLimiterConfig.fromSliderValue(this.value));
			}
		});

		this.addRenderableWidget(Button.builder(
				enabledLabel(IrisShadersFpsLimiterConfig.get().enabled),
				button -> {
					IrisShadersFpsLimiterConfig.get().setEnabled(!IrisShadersFpsLimiterConfig.get().enabled);
					button.setMessage(enabledLabel(IrisShadersFpsLimiterConfig.get().enabled));
				})
				.bounds(centerX + 5, centerY - 40, 150, 20)
				.build());

		this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.onClose())
				.bounds(centerX - 100, centerY - 8, 200, 20)
				.build());
	}

	@Override
	public void onClose() {
		IrisShadersFpsLimiterConfig.get().save();
		if (this.minecraft != null) {
			this.minecraft.setScreenAndShow(this.parent);
		}
	}

	private static Component enabledLabel(boolean enabled) {
		Component state = enabled ? Component.translatable("options.on") : Component.translatable("options.off");
		return Component.translatable("option.iris-shaders-fps-limiter.enabled").append(": ").append(state);
	}

	private static Component label(int cap) {
		return Component.translatable("option.iris-shaders-fps-limiter.shader_fps_cap")
				.append(": ")
				.append(Component.translatable("option.iris-shaders-fps-limiter.shader_fps_cap.value", cap));
	}
}
