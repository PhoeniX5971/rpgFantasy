package net.phoenix.rpgfantasy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributes;
import net.phoenix.rpgfantasy.command.ModCommands;
import net.phoenix.rpgfantasy.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpgFantasy implements ModInitializer {
	public static final String MOD_ID = "rpgfantasy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing RPG Fantasy Mod");

		// Command Registration
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ModCommands.register(dispatcher);
		});

		// Event Handlers
		ManaHandler.register();
		HealthHandler.register();

		// Respawn Handling
		ServerPlayerEvents.COPY_FROM.register((newPlayer, oldPlayer, alive) -> {
			if (!alive) {
				PlayerAttributes attributes = ((PlayerAttributeHolder) newPlayer).getAttributes();
				attributes.get("health").setCurrent(attributes.get("health").getMax());
			}
		});

		// Debug Info
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LOGGER.info("RPG Fantasy systems activated on server");
		});
	}
}
