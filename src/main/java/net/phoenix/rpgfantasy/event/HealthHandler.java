package net.phoenix.rpgfantasy.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;

public class HealthHandler {
	private static final float HUNGER_REGEN_THRESHOLD = 7.0f;
	private static final float HUNGER_PENALTY_FACTOR = 0.3f;
	private static final float VANILLA_MAX_HEALTH = 20.0f;

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				onPlayerTick(player);
			}
		});
	}

	private static void onPlayerTick(ServerPlayerEntity player) {
		PlayerAttributeInstance health = ((PlayerAttributeHolder) player).getAttributes().get("health");
		float regenRate = calculateRegenRate(player);

		if (player.isAlive()) {
			health.setCurrent(health.getCurrent() + regenRate);
			updateVanillaHealth(player, health);
		}

		// Force death if health reaches 0 (failsafe)
		if (health.getCurrent() <= 0 && player.isAlive()) {
			forceKill(player);
		}
	}

	private static void updateVanillaHealth(ServerPlayerEntity player, PlayerAttributeInstance health) {
		float healthPercentage = (float) (health.getCurrent() / health.getMax());
		float vanillaHealth = healthPercentage * VANILLA_MAX_HEALTH;
		player.setHealth(vanillaHealth);
	}

	private static void forceKill(ServerPlayerEntity player) {
		// Completely bypass any resistance
		player.setHealth(0);
		player.getDamageSources().outOfWorld();
		player.damage(player.getServerWorld(), player.getDamageSources().outOfWorld(), Float.MAX_VALUE);
	}

	public static float onDamage(ServerPlayerEntity player, DamageSource source, float amount) {
		// Allow instant kill commands to work
		if (source == player.getDamageSources().outOfWorld()) {
			return amount;
		}

		PlayerAttributeInstance health = ((PlayerAttributeHolder) player).getAttributes().get("health");
		float newHealth = (float) health.getCurrent() - amount;

		if (newHealth <= 0) {
			health.setCurrent(0);
			forceKill(player);
			return Float.MAX_VALUE;
		}

		health.setCurrent(newHealth);
		updateVanillaHealth(player, health);
		return 0f; // Cancel vanilla damage since we handled it
	}

	private static float calculateRegenRate(ServerPlayerEntity player) {
		float baseRegen = 0.1f;
		return player.getHungerManager().getFoodLevel() < HUNGER_REGEN_THRESHOLD ? baseRegen * HUNGER_PENALTY_FACTOR
				: baseRegen;
	}
}
