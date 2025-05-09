package net.phoenix.rpgfantasy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(LivingEntity.class)
public abstract class MaxHealthMixin {

	@Inject(method = "getMaxHealth()F", at = @At("HEAD"), cancellable = true)
	private void overrideMaxHealth(CallbackInfoReturnable<Float> cir) {
		// Only override for PlayerEntity instances
		if ((Object) this instanceof PlayerEntity) {
			cir.setReturnValue(20f); // Force vanilla to think max health is 20
		}
	}
}
