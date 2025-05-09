package net.phoenix.rpgfantasy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;
import net.phoenix.rpgfantasy.attribute.PlayerAttributes;

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
							instance.levelUp();

							context.getSource().sendFeedback(
									() -> Text.literal(attr + " leveled up to " + instance.getLevel() + "!"),
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
	}
}
