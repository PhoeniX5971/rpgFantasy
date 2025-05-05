package net.phoenix.rpgfantasy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.phoenix.rpgfantasy.util.ManaAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerDataSaveMixin {

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void saveMana(NbtCompound nbt, CallbackInfo ci) {
		if ((Object) this instanceof ManaAccessor mana) {
			nbt.putDouble("Mana", mana.getMana());
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void loadMana(NbtCompound nbt, CallbackInfo ci) {
		if ((Object) this instanceof ManaAccessor mana) {
			if (nbt.contains("Mana")) {
				mana.setMana(nbt.getDouble("Mana"));
			}
		}
	}
}
