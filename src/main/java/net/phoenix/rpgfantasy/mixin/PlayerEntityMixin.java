package net.phoenix.rpgfantasy.mixin;

import net.phoenix.rpgfantasy.attribute.ModAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	@ModifyReturnValue(method = "createPlayerAttributes", at = @At("RETURN"))
	private static DefaultAttributeContainer.Builder addCustomAttributes(DefaultAttributeContainer.Builder builder) {
		return builder.add(ModAttributes.MANA);
	}
}
