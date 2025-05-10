package net.phoenix.rpgfantasy.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;

public class StrengthHandler {
	public static void register() {
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				// Force strength to always match max value
				PlayerAttributeInstance strength = ((PlayerAttributeHolder) player)
						.getAttributes()
						.get("strength");

				strength.setCurrent(strength.getMax());
			}
		});
	}
}
