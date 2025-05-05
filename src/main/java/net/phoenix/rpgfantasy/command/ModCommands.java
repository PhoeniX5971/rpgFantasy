package net.phoenix.rpgfantasy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.phoenix.rpgfantasy.util.ManaAccessor;

public class ModCommands {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("getmana").executes(context -> {
			ServerCommandSource source = context.getSource();

			if (!(source.getEntity() instanceof PlayerEntity player)) {
				source.sendError(Text.literal("This command must be run by a player."));
				return 0;
			}

			double currentMana = ((ManaAccessor) player).getMana();
			source.sendFeedback(() -> Text.literal("Your mana: " + currentMana), false);
			return 1;
		}));

		dispatcher.register(CommandManager.literal("setmana")
				.requires(source -> source.hasPermissionLevel(2)) // Only allow OPs
				.then(CommandManager.argument("target", EntityArgumentType.player())
						.then(CommandManager.argument("value", DoubleArgumentType.doubleArg(0.0))
								.executes(context -> {
									ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
									double value = DoubleArgumentType.getDouble(context, "value");

									if (target instanceof ManaAccessor mana) {
										mana.setMana(value);
										context.getSource().sendFeedback(
												() -> Text.literal(
														"Set " + target.getName().getString() + "'s mana to " + value),
												true);
										return 1;
									}
									context.getSource().sendError(Text.literal("Target does not support mana."));
									return 0;
								}))));
	}
}
