package com.pivotcam;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PivotCamClient implements ClientModInitializer {
	public static final String MOD_ID = "pivot-cam";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		PivotCamConfig.load();
		PivotCamCommands.register();
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> PivotCam.setActive(false));
		LOGGER.info("Pivot Cam ready (/pivotcam)");
	}
}
