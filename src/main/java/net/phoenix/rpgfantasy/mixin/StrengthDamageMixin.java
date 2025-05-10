package net.phoenix.rpgfantasy.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class StrengthDamageMixin {
	@ModifyVariable(method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true)
	private float modifyDamage(float amount, ServerWorld world, DamageSource source) {
		if (source.getAttacker() instanceof PlayerEntity player) {
			// Skip non-physical damage types
			if (isNonPhysicalDamage(source)) {
				return amount;
			}

			float strength = (float) ((PlayerAttributeHolder) player).getAttributes()
					.get("strength").getCurrent();
			return amount * (strength / 10f);
		}
		return amount;
	}

	private boolean isNonPhysicalDamage(DamageSource source) {
		return source.isOf(net.minecraft.entity.damage.DamageTypes.MAGIC) ||
				source.isOf(net.minecraft.entity.damage.DamageTypes.ARROW) ||
				source.isOf(net.minecraft.entity.damage.DamageTypes.TRIDENT) ||
				source.isOf(net.minecraft.entity.damage.DamageTypes.INDIRECT_MAGIC);
	}
}
