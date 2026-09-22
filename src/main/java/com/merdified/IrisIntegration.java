package com.merdified;

import net.fabricmc.loader.api.FabricLoader;

public final class IrisIntegration {
	private static final boolean IS_IRIS_INSTALLED = FabricLoader.getInstance().isModLoaded("iris");
	private static volatile boolean failed;

	private IrisIntegration() {
	}

	public static boolean areShadersInUse() {
		if (!IS_IRIS_INSTALLED || failed) {
			return false;
		}
		return IrisApiHolder.isInUse();
	}

	private static final class IrisApiHolder {
		static boolean isInUse() {
			try {
				return net.irisshaders.iris.api.v0.IrisApi.getInstance().isShaderPackInUse();
			} catch (LinkageError | RuntimeException e) {
				failed = true;
				IrisShadersFpsLimiter.LOGGER.warn("Iris integration failed; shader limiting disabled.", e);
				return false;
			}
		}
	}
}
