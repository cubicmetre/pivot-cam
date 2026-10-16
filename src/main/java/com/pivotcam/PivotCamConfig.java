package com.pivotcam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Persistent camera setup, stored as JSON in config/pivot-cam.json.
 */
public final class PivotCamConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("pivot-cam.json");

	private static PivotCamConfig instance = new PivotCamConfig();

	public double pivotX;
	public double pivotY;
	public double pivotZ;
	public double telescope;
	public boolean yawInvert;
	public float yawOffset;
	public boolean pitchInvert;
	public float pitchOffset;
	public boolean flipVertical;

	public static PivotCamConfig get() {
		return instance;
	}

	public static void load() {
		if (!Files.exists(PATH)) {
			return;
		}

		try (Reader reader = Files.newBufferedReader(PATH)) {
			PivotCamConfig loaded = GSON.fromJson(reader, PivotCamConfig.class);
			if (loaded != null) {
				instance = loaded;
			}
		} catch (IOException | JsonParseException e) {
			PivotCamClient.LOGGER.error("Failed to read {}", PATH, e);
		}
	}

	public static void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(instance, writer);
			}
		} catch (IOException e) {
			PivotCamClient.LOGGER.error("Failed to write {}", PATH, e);
		}
	}
}
