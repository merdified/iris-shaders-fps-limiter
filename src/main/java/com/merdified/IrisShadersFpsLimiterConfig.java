package com.merdified;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class IrisShadersFpsLimiterConfig {
	public static final boolean DEFAULT_ENABLED = true;
	public static final int DEFAULT_CAP = 120;
	public static final int MIN_CAP = 10;
	public static final int MAX_CAP = 250;

	private static final Logger LOGGER = LoggerFactory.getLogger(IrisShadersFpsLimiter.MOD_ID);
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static IrisShadersFpsLimiterConfig instance;

	public boolean enabled = DEFAULT_ENABLED;
	public int fpsCap = DEFAULT_CAP;

	public static IrisShadersFpsLimiterConfig get() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	public static void load() {
		Path path = path();
		if (Files.isRegularFile(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				instance = GSON.fromJson(reader, IrisShadersFpsLimiterConfig.class);
			} catch (Exception e) {
				LOGGER.warn("Could not read iris-shaders-fps-limiter config, using defaults", e);
			}
		}
		if (instance == null) {
			instance = new IrisShadersFpsLimiterConfig();
		}
		instance.fpsCap = clamp(instance.fpsCap);
	}

	public void save() {
		try {
			Path target = path();
			Files.createDirectories(target.getParent());
			Path parent = target.getParent();
			Path temp = Files.createTempFile(parent, "iris-shaders-fps-limiter", ".tmp");
			try (Writer writer = Files.newBufferedWriter(temp)) {
				GSON.toJson(this, writer);
			}
			try {
				Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException atomicFailed) {
				Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			LOGGER.warn("Could not save iris-shaders-fps-limiter config", e);
		}
	}

	public void setFpsCap(int cap) {
		this.fpsCap = clamp(cap);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static int clamp(int cap) {
		if (cap < MIN_CAP) {
			return MIN_CAP;
		}
		if (cap > MAX_CAP) {
			return MAX_CAP;
		}
		return cap;
	}

	public static double toSliderValue(int cap) {
		return (double) (clamp(cap) - MIN_CAP) / (MAX_CAP - MIN_CAP);
	}

	public static int fromSliderValue(double value) {
		double clamped = Math.clamp(value, 0.0, 1.0);
		return MIN_CAP + (int) Math.round(clamped * (MAX_CAP - MIN_CAP));
	}

	public int effectiveLimit(int vanillaLimit, boolean shadersInUse) {
		if (this.enabled && shadersInUse) {
			return Math.min(vanillaLimit, clamp(this.fpsCap));
		}
		return vanillaLimit;
	}

	private static Path path() {
		return FabricLoader.getInstance().getConfigDir().resolve("iris-shaders-fps-limiter.json");
	}
}
