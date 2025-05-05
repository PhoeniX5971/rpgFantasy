package net.phoenix.rpgfantasy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.phoenix.rpgfantasy.attribute.ModAttributes;
import net.phoenix.rpgfantasy.command.ModCommands;
import net.phoenix.rpgfantasy.util.ManaAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpgFantasy implements ModInitializer {
	public static final String MOD_ID = "rpgfantasy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModAttributes.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ModCommands.register(dispatcher);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			PlayerEntity player = handler.player;
			if (player instanceof ManaAccessor mana) {
				mana.setMana(200);
			}
		});
	}
}
