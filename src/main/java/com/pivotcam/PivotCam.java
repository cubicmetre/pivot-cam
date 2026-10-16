package com.pivotcam;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

/**
 * Runtime camera state. The active flag is never persisted, so the camera always starts stopped.
 */
public final class PivotCam {
	private static boolean active;
	private static Vec3 lastCameraPos = Vec3.ZERO;

	private PivotCam() {
	}

	public static boolean isActive() {
		return active;
	}

	public static void setActive(boolean active) {
		PivotCam.active = active;
	}

	public static float transformYaw(float yaw) {
		PivotCamConfig config = PivotCamConfig.get();
		return (config.yawInvert ? -yaw : yaw) + config.yawOffset;
	}

	public static float transformPitch(float pitch) {
		PivotCamConfig config = PivotCamConfig.get();
		return Mth.clamp((config.pitchInvert ? -pitch : pitch) + config.pitchOffset, -90.0F, 90.0F);
	}

	public static Vec3 pivot() {
		PivotCamConfig config = PivotCamConfig.get();
		return new Vec3(config.pivotX, config.pivotY, config.pivotZ);
	}

	public static Vec3 lastCameraPos() {
		return lastCameraPos;
	}

	public static void setLastCameraPos(Vec3 pos) {
		lastCameraPos = pos;
	}
}
