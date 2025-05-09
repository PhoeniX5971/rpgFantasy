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
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;

		// Skip if already dead or invulnerable
		if (player.isDead() || player.isInvulnerableTo(world, source)) {
			return;
		}

		// Apply damage to our custom health system
		PlayerAttributeInstance health = ((PlayerAttributeHolder) player).getAttributes().get("health");
		float newHealth = (float) (health.getCurrent() - amount);
		health.setCurrent(Math.max(0, newHealth));

		// Update vanilla health display
		float healthPercentage = (float) (newHealth / health.getMax());
		player.setHealth(healthPercentage * 20f);

		// Cancel vanilla damage handling
		cir.setReturnValue(false);
	}
}
