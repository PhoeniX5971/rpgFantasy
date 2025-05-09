package net.phoenix.rpgfantasy.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.phoenix.rpgfantasy.attribute.ModAttributes;
import net.phoenix.rpgfantasy.util.ManaAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDataSaveMixin {

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void writeManaToNbt(NbtCompound nbt, CallbackInfo ci) {
		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
		EntityAttributeInstance manaAttr = player.getAttributeInstance(ModAttributes.MANA);
		if (manaAttr != null) {
			nbt.putDouble("rpgfantasy.mana", manaAttr.getBaseValue());
			System.out.println("[RPGFantasy] Saved mana: " + manaAttr.getBaseValue());
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void readManaFromNbt(NbtCompound nbt, CallbackInfo ci) {
		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
		if (nbt.contains("rpgfantasy.mana")) {
			EntityAttributeInstance manaAttr = player.getAttributeInstance(ModAttributes.MANA);
			if (manaAttr != null) {
				manaAttr.setBaseValue(nbt.getDouble("rpgfantasy.mana"));
				System.out.println("[RPGFantasy] Loaded mana: " + nbt.getDouble("rpgfantasy.mana"));
			}
		}
	}
}
