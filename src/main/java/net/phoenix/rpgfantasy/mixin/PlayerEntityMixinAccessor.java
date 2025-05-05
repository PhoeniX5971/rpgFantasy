package net.phoenix.rpgfantasy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.phoenix.rpgfantasy.attribute.ModAttributes;
import net.phoenix.rpgfantasy.util.ManaAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixinAccessor implements ManaAccessor {
	@Override
	public double getMana() {
		EntityAttributeInstance attr = ((PlayerEntity) (Object) this).getAttributeInstance(ModAttributes.MANA);
		return attr != null ? attr.getValue() : 0.0;
	}

	@Override
	public void setMana(double value) {
		EntityAttributeInstance attr = ((PlayerEntity) (Object) this).getAttributeInstance(ModAttributes.MANA);
		if (attr != null)
			attr.setBaseValue(value);
	}
}
