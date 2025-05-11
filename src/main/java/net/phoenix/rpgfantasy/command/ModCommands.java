package net.phoenix.rpgfantasy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;
import net.phoenix.rpgfantasy.attribute.PlayerAttributes;
import net.phoenix.rpgfantasy.level.PlayerLevelData;

public class ModCommands {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		// Get Attribute Value
		// Get Attribute Value
		dispatcher.register(CommandManager.literal("rpg-get")
				.then(CommandManager.argument("attribute", StringArgumentType.word())
						.executes(context -> {
							ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
							String attr = StringArgumentType.getString(context, "attribute");

							PlayerAttributeInstance instance = ((PlayerAttributeHolder) player).getAttributes()
									.get(attr);
							context.getSource().sendFeedback(
									() -> Text.literal(attr + ": " + instance.getCurrent() + "/" + instance.getMax()),
									false);
							return 1;
						}))); // ← MISSING THIS!

		// Set Attribute Value
		dispatcher.register(CommandManager.literal("rpg-set")
				.requires(src -> src.hasPermissionLevel(2))
				.then(CommandManager.argument("target", EntityArgumentType.player())
						.then(CommandManager.argument("attribute", StringArgumentType.word())
								.then(CommandManager.argument("value", DoubleArgumentType.doubleArg(0))
										.executes(context -> {
											ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
											String attr = StringArgumentType.getString(context, "attribute");
											double value = DoubleArgumentType.getDouble(context, "value");

											PlayerAttributeInstance instance = ((PlayerAttributeHolder) target)
													.getAttributes().get(attr);
											instance.setCurrent(value);

											context.getSource().sendFeedback(
													() -> Text.literal("Set " + target.getName().getString() + "'s "
															+ attr + " to " + value),
													true);
											return 1;
										})))));

		// Level Up Attribute
		dispatcher.register(CommandManager.literal("rpg-levelup")
				.then(CommandManager.argument("attribute", StringArgumentType.word())
						.executes(context -> {
							ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
							String attr = StringArgumentType.getString(context, "attribute");

							PlayerAttributeInstance instance = ((PlayerAttributeHolder) player).getAttributes()
									.get(attr);
							instance.allocatePoint();

							context.getSource().sendFeedback(
									() -> Text.literal(
											attr + " has " + instance.getAllocatedPoints() + " allocated points!"),
									true);
							return 1;
						})));

		// Full Heal Command
		dispatcher.register(CommandManager.literal("rpg-heal")
				.executes(context -> {
					ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
					PlayerAttributes attrs = ((PlayerAttributeHolder) player).getAttributes();

					attrs.get("health").setCurrent(attrs.get("health").getMax());
					attrs.get("mana").setCurrent(attrs.get("mana").getMax());

					context.getSource().sendFeedback(() -> Text.literal("Fully restored health and mana!"), false);
					return 1;
				}));

		// Level and XP commands
		dispatcher.register(CommandManager.literal("rpg-xp")
				.then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
						.executes(context -> {
							ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
							int amount = IntegerArgumentType.getInteger(context, "amount");
							((PlayerAttributeHolder) player).getLevelData().addExperience(amount);
							context.getSource().sendFeedback(
									() -> Text.literal("Added " + amount + " XP!"),
									false);
							return 1;
						})));

		dispatcher.register(CommandManager.literal("rpg-level")
				.executes(context -> {
					ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
					PlayerLevelData levelData = ((PlayerAttributeHolder) player).getLevelData();
					context.getSource().sendFeedback(
							() -> Text.literal("Level: " + levelData.getLevel() +
									" | XP: " + levelData.getExperience() + "/" +
									levelData.getRequiredExperience(levelData.getLevel()) +
									" | Points: " + levelData.getUnallocatedPoints()),
							false);
					return 1;
				}));

		// Updated levelup command to use unallocated points
		dispatcher.register(CommandManager.literal("rpg-levelup")
				.then(CommandManager.argument("attribute", StringArgumentType.word())
						.executes(context -> {
							ServerPlayerEntity player = (ServerPlayerEntity) context.getSource().getEntity();
							String attr = StringArgumentType.getString(context, "attribute");

							PlayerLevelData levelData = ((PlayerAttributeHolder) player).getLevelData();
							if (levelData.useUnallocatedPoint()) {
								PlayerAttributeInstance instance = ((PlayerAttributeHolder) player)
										.getAttributes().get(attr);
								instance.allocatePoint();

								context.getSource().sendFeedback(
										() -> Text.literal("Allocated point to " + attr +
												"! Remaining points: " + levelData.getUnallocatedPoints()),
										true);
							} else {
								context.getSource().sendError(Text.literal("No unallocated points available!"));
							}
							return 1;
						})));
	}
}
