package com.pivotcam.mixin;

import com.pivotcam.PivotCam;
import com.pivotcam.PivotCamConfig;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.3 {
import java.util.List;
import org.spongepowered.asm.mixin.Final;
//?} elif <26.1 {
/*import net.minecraft.client.DeltaTracker;
import org.joml.Matrix4f;
*///?}

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Unique
	private static final Identifier PIVOTCAM_FLIP = Identifier.fromNamespaceAndPath("pivot-cam", "flip");

	//? if >=26.3 {
	@Shadow
	@Final
	private List<Identifier> requestedPostEffects;
	//?} else {
	/*@Shadow
	private void setPostEffect(Identifier id) {
	}

	@Shadow
	public abstract void clearPostEffect();

	@Shadow
	private Identifier postEffectId;
	*///?}

	//? if <26.1 {
	/*@Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
	private void pivotcam$hideHand(float partialTicks, boolean sleeping, Matrix4f pose, CallbackInfo ci) {
		if (PivotCam.isActive()) {
			ci.cancel();
		}
	}

	@Inject(method = "updateCamera", at = @At("TAIL"))
	private void pivotcam$requestFlip(DeltaTracker deltaTracker, CallbackInfo ci) {
		pivotcam$applyLegacyFlip();
	}
	*///?} elif >=26.3 {
	@Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
	private void pivotcam$hideHand(CallbackInfo ci) {
		if (PivotCam.isActive()) {
			ci.cancel();
		}
	}

	@Inject(method = "update", at = @At("TAIL"))
	private void pivotcam$requestFlip(CallbackInfo ci) {
		if (PivotCam.isActive() && PivotCamConfig.get().flipVertical) {
			this.requestedPostEffects.add(PIVOTCAM_FLIP);
		}
	}
	//?} else {
	/*@Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
	private void pivotcam$hideHand(CallbackInfo ci) {
		if (PivotCam.isActive()) {
			ci.cancel();
		}
	}

	@Inject(method = "update", at = @At("TAIL"))
	private void pivotcam$requestFlip(CallbackInfo ci) {
		pivotcam$applyLegacyFlip();
	}
	*///?}

	//? if <26.3 {
	/*@Unique
	private void pivotcam$applyLegacyFlip() {
		if (PivotCam.isActive() && PivotCamConfig.get().flipVertical) {
			if (!PIVOTCAM_FLIP.equals(this.postEffectId)) {
				this.setPostEffect(PIVOTCAM_FLIP);
			}
		} else if (PIVOTCAM_FLIP.equals(this.postEffectId)) {
			this.clearPostEffect();
		}
	}
	*///?}
}
