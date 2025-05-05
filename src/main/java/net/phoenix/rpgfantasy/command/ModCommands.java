package net.phoenix.rpgfantasy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
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
				.then(CommandManager.argument("value", DoubleArgumentType.doubleArg(0))
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							double value = DoubleArgumentType.getDouble(context, "value");

							if (!(source.getEntity() instanceof PlayerEntity player)) {
								source.sendError(Text.literal("This command must be run by a player."));
								return 0;
							}

							((ManaAccessor) player).setMana(value);
							source.sendFeedback(() -> Text.literal("Mana set to: " + value), false);
							return 1;
						})));
	}
}
