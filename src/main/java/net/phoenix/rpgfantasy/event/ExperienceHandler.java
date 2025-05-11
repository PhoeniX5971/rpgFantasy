package net.phoenix.rpgfantasy.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;

public class ExperienceHandler {
	public static void register() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof PlayerEntity)
				return; // Players don't drop XP for themselves

			if (damageSource.getAttacker() instanceof PlayerEntity player) {
				// Award XP based on mob type/health
				int xp = (int) (entity.getMaxHealth() * 2); // Example formula
				((PlayerAttributeHolder) player).getLevelData().addExperience(xp);
			}
		});
	}
}
