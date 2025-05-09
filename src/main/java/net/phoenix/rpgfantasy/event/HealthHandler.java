package net.phoenix.rpgfantasy.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;

public class HealthHandler {
	private static final float HUNGER_REGEN_THRESHOLD = 7.0f;
	private static final float HUNGER_PENALTY_FACTOR = 0.3f;

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				onPlayerTick(player);
			}
		});
	}

	private static void onPlayerTick(ServerPlayerEntity player) {
		PlayerAttributeInstance health = ((PlayerAttributeHolder) player).getAttributes().get("health");

		// Only process if alive
		if (player.isAlive()) {
			// Handle regeneration
			float regenRate = calculateRegenRate(player);
			health.setCurrent(health.getCurrent() + regenRate);

			// Update vanilla health display
			float healthPercentage = (float) (health.getCurrent() / health.getMax());
			player.setHealth(healthPercentage * 20f);

			// Handle death if health reaches 0
			if (health.getCurrent() <= 0) {
				player.kill(player.getServerWorld());
			}
		}
	}

	private static float calculateRegenRate(ServerPlayerEntity player) {
		float baseRegen = 0.1f;
		return player.getHungerManager().getFoodLevel() < HUNGER_REGEN_THRESHOLD ? baseRegen * HUNGER_PENALTY_FACTOR
				: baseRegen;
	}
}
