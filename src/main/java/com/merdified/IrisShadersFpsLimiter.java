package com.merdified;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrisShadersFpsLimiter implements ModInitializer {
	public static final String MOD_ID = "iris-shaders-fps-limiter";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		IrisShadersFpsLimiterConfig.load();
		LOGGER.info("Iris Shaders FPS limiter: enabled={}, cap={}", IrisShadersFpsLimiterConfig.get().enabled, IrisShadersFpsLimiterConfig.get().fpsCap);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
