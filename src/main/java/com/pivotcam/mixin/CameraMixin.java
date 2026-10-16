package com.pivotcam.mixin;

import com.pivotcam.PivotCam;
import com.pivotcam.PivotCamConfig;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? if <26.1 {
/*import net.minecraft.world.level.Level;
*///?}

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	private Entity entity;

	@Shadow
	private boolean detached;

	@Shadow
	@Final
	private Vector3f forwards;

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Shadow
	protected abstract void setPosition(Vec3 position);

	//? if <26.1 {
	/*@Inject(method = "setup", at = @At("TAIL"))
	private void pivotcam$alignWithPivot(Level level, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float partialTicks, CallbackInfo ci) {
		pivotcam$applyPivot(partialTicks);
	}
	*///?} else {
	@Inject(method = "alignWithEntity", at = @At("TAIL"))
	private void pivotcam$alignWithPivot(float partialTicks, CallbackInfo ci) {
		pivotcam$applyPivot(partialTicks);
	}
	//?}

	@Unique
	private void pivotcam$applyPivot(float partialTicks) {
		if (!PivotCam.isActive() || this.entity == null) {
			return;
		}

		this.setRotation(
			PivotCam.transformYaw(this.entity.getViewYRot(partialTicks)),
			PivotCam.transformPitch(this.entity.getViewXRot(partialTicks))
		);

		double telescope = PivotCamConfig.get().telescope;
		Vec3 pos = PivotCam.pivot().add(this.forwards.x * telescope, this.forwards.y * telescope, this.forwards.z * telescope);
		this.setPosition(pos);
		PivotCam.setLastCameraPos(pos);
		this.detached = true;
	}

	@Inject(method = "getFluidInCamera", at = @At("HEAD"), cancellable = true)
	private void pivotcam$ignoreFluid(CallbackInfoReturnable<FogType> cir) {
		if (PivotCam.isActive()) {
			cir.setReturnValue(FogType.NONE);
		}
	}
}
