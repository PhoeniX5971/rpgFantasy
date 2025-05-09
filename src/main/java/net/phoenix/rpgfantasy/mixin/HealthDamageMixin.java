package net.phoenix.rpgfantasy.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class HealthDamageMixin {
	@Inject(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		PlayerAttributeInstance health = ((PlayerAttributeHolder) player).getAttributes().get("health");

		// Don't apply damage if player is already dead
		if (player.isDead()) {
			cir.setReturnValue(false); // Prevent further damage
			return;
		}

		// Apply damage to custom health
		float newHealth = (float) (health.getCurrent() - amount);
		health.setCurrent(newHealth);

		// If fatal damage, handle death and prevent further health updates
		if (newHealth <= 0) {
			health.setCurrent(0);
			player.kill(world); // Kill the player
			cir.setReturnValue(false); // Let vanilla process death
			return;
		}

		// Otherwise, cancel vanilla damage (we handled it)
		cir.setReturnValue(true);

		// Update vanilla health display
		float healthPercentage = (float) (newHealth / health.getMax());
		player.setHealth(healthPercentage * 20.0f); // Scale to vanilla 20 hearts
	}
}
