package com.pivotcam;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

//? if <26.1 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
*///?} else {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
//?}

public final class PivotCamCommands {
	private PivotCamCommands() {
	}

	public static void register() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			//? if <26.1 {
			/*LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal("pivotcam")
				.then(ClientCommandManager.literal("start").executes(ctx -> setActive(ctx, true)))
				.then(ClientCommandManager.literal("stop").executes(ctx -> setActive(ctx, false)))
				.then(ClientCommandManager.literal("toggle").executes(ctx -> setActive(ctx, !PivotCam.isActive())))
				.then(ClientCommandManager.literal("pivot")
					.then(ClientCommandManager.literal("here").executes(PivotCamCommands::pivotHere))
					.then(ClientCommandManager.argument("x", DoubleArgumentType.doubleArg())
						.then(ClientCommandManager.argument("y", DoubleArgumentType.doubleArg())
							.then(ClientCommandManager.argument("z", DoubleArgumentType.doubleArg())
								.executes(PivotCamCommands::pivot)))))
				.then(ClientCommandManager.literal("telescope")
					.then(ClientCommandManager.argument("blocks", DoubleArgumentType.doubleArg())
						.executes(PivotCamCommands::telescope)))
				.then(axis("pitch", true))
				.then(axis("yaw", false))
				.then(ClientCommandManager.literal("flip")
					.then(ClientCommandManager.argument("value", BoolArgumentType.bool())
						.executes(PivotCamCommands::flip)))
				.then(ClientCommandManager.literal("info").executes(PivotCamCommands::info));
			*///?} else {
			LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommands.literal("pivotcam")
				.then(ClientCommands.literal("start").executes(ctx -> setActive(ctx, true)))
				.then(ClientCommands.literal("stop").executes(ctx -> setActive(ctx, false)))
				.then(ClientCommands.literal("toggle").executes(ctx -> setActive(ctx, !PivotCam.isActive())))
				.then(ClientCommands.literal("pivot")
					.then(ClientCommands.literal("here").executes(PivotCamCommands::pivotHere))
					.then(ClientCommands.argument("x", DoubleArgumentType.doubleArg())
						.then(ClientCommands.argument("y", DoubleArgumentType.doubleArg())
							.then(ClientCommands.argument("z", DoubleArgumentType.doubleArg())
								.executes(PivotCamCommands::pivot)))))
				.then(ClientCommands.literal("telescope")
					.then(ClientCommands.argument("blocks", DoubleArgumentType.doubleArg())
						.executes(PivotCamCommands::telescope)))
				.then(axis("pitch", true))
				.then(axis("yaw", false))
				.then(ClientCommands.literal("flip")
					.then(ClientCommands.argument("value", BoolArgumentType.bool())
						.executes(PivotCamCommands::flip)))
				.then(ClientCommands.literal("info").executes(PivotCamCommands::info));
			//?}

			dispatcher.register(root);
		});
	}

	private static LiteralArgumentBuilder<FabricClientCommandSource> axis(String name, boolean pitch) {
		//? if <26.1 {
		/*return ClientCommandManager.literal(name)
			.then(ClientCommandManager.literal("invert")
				.then(ClientCommandManager.argument("value", BoolArgumentType.bool())
					.executes(ctx -> {
						boolean value = BoolArgumentType.getBool(ctx, "value");
						if (pitch) {
							PivotCamConfig.get().pitchInvert = value;
						} else {
							PivotCamConfig.get().yawInvert = value;
						}
						return saved(ctx, name + " invert = " + value);
					})))
			.then(ClientCommandManager.literal("offset")
				.then(ClientCommandManager.argument("degrees", FloatArgumentType.floatArg())
					.executes(ctx -> {
						float degrees = FloatArgumentType.getFloat(ctx, "degrees");
						if (pitch) {
							PivotCamConfig.get().pitchOffset = degrees;
						} else {
							PivotCamConfig.get().yawOffset = degrees;
						}
						return saved(ctx, name + " offset = " + degrees);
					})));
		*///?} else {
		return ClientCommands.literal(name)
			.then(ClientCommands.literal("invert")
				.then(ClientCommands.argument("value", BoolArgumentType.bool())
					.executes(ctx -> {
						boolean value = BoolArgumentType.getBool(ctx, "value");
						if (pitch) {
							PivotCamConfig.get().pitchInvert = value;
						} else {
							PivotCamConfig.get().yawInvert = value;
						}
						return saved(ctx, name + " invert = " + value);
					})))
			.then(ClientCommands.literal("offset")
				.then(ClientCommands.argument("degrees", FloatArgumentType.floatArg())
					.executes(ctx -> {
						float degrees = FloatArgumentType.getFloat(ctx, "degrees");
						if (pitch) {
							PivotCamConfig.get().pitchOffset = degrees;
						} else {
							PivotCamConfig.get().yawOffset = degrees;
						}
						return saved(ctx, name + " offset = " + degrees);
					})));
		//?}
	}

	private static int setActive(CommandContext<FabricClientCommandSource> ctx, boolean active) {
		PivotCam.setActive(active);
		ctx.getSource().sendFeedback(Component.literal("Pivot cam " + (active ? "started" : "stopped")));
		return 1;
	}

	private static int pivot(CommandContext<FabricClientCommandSource> ctx) {
		return setPivot(ctx, new Vec3(
			DoubleArgumentType.getDouble(ctx, "x"),
			DoubleArgumentType.getDouble(ctx, "y"),
			DoubleArgumentType.getDouble(ctx, "z")
		));
	}

	private static int pivotHere(CommandContext<FabricClientCommandSource> ctx) {
		LocalPlayer player = ctx.getSource().getPlayer();
		return setPivot(ctx, player.getEyePosition());
	}

	private static int setPivot(CommandContext<FabricClientCommandSource> ctx, Vec3 pos) {
		PivotCamConfig config = PivotCamConfig.get();
		config.pivotX = pos.x;
		config.pivotY = pos.y;
		config.pivotZ = pos.z;
		return saved(ctx, "pivot = " + format(pos));
	}

	private static int telescope(CommandContext<FabricClientCommandSource> ctx) {
		double blocks = DoubleArgumentType.getDouble(ctx, "blocks");
		PivotCamConfig.get().telescope = blocks;
		return saved(ctx, "telescope = " + blocks);
	}

	private static int flip(CommandContext<FabricClientCommandSource> ctx) {
		boolean value = BoolArgumentType.getBool(ctx, "value");
		PivotCamConfig.get().flipVertical = value;
		return saved(ctx, "flip = " + value);
	}

	private static int info(CommandContext<FabricClientCommandSource> ctx) {
		PivotCamConfig config = PivotCamConfig.get();
		FabricClientCommandSource source = ctx.getSource();
		source.sendFeedback(Component.literal("Pivot cam: " + (PivotCam.isActive() ? "running" : "stopped")));
		source.sendFeedback(Component.literal("pivot = " + format(PivotCam.pivot())));
		source.sendFeedback(Component.literal("telescope = " + config.telescope));
		source.sendFeedback(Component.literal("yaw: invert = " + config.yawInvert + ", offset = " + config.yawOffset));
		source.sendFeedback(Component.literal("pitch: invert = " + config.pitchInvert + ", offset = " + config.pitchOffset));
		source.sendFeedback(Component.literal("flip = " + config.flipVertical));
		if (PivotCam.isActive()) {
			source.sendFeedback(Component.literal("camera = " + format(PivotCam.lastCameraPos())));
		}
		return 1;
	}

	private static int saved(CommandContext<FabricClientCommandSource> ctx, String message) {
		PivotCamConfig.save();
		ctx.getSource().sendFeedback(Component.literal("Pivot cam " + message));
		return 1;
	}

	private static String format(Vec3 pos) {
		return String.format("(%.4f, %.4f, %.4f)", pos.x, pos.y, pos.z);
	}
}
