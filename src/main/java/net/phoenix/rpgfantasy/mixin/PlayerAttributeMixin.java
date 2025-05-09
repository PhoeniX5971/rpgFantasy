package net.phoenix.rpgfantasy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;
import net.phoenix.rpgfantasy.attribute.PlayerAttributes;

/**
 * Mixin to handle custom RPG attributes (health, mana, etc.) for players.
 * Key features:
 * - Saves/loads attributes to NBT
 * - Resets health on respawn (but not dimension changes)
 */
@Mixin(ServerPlayerEntity.class)
public abstract class PlayerAttributeMixin implements PlayerAttributeHolder {
	@Unique
	private final PlayerAttributes rpgfantasy$attributes = new PlayerAttributes();

	@Override
	public PlayerAttributes getAttributes() {
		return rpgfantasy$attributes;
	}

	// ===== NBT PERSISTENCE ===== //
	@Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("HEAD"))
	private void writeAttributes(NbtCompound nbt, CallbackInfo ci) {
		// Save all attributes to NBT under "RPGFantasyAttributes"
		nbt.put("RPGFantasyAttributes", rpgfantasy$attributes.toNbt());
	}

	@Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("HEAD"))
	private void readAttributes(NbtCompound nbt, CallbackInfo ci) {
		// Load attributes from NBT if they exist
		if (nbt.contains("RPGFantasyAttributes")) {
			rpgfantasy$attributes.fromNbt(nbt.getCompound("RPGFantasyAttributes"));
		}
	}

	// ===== RESPAWN LOGIC ===== //
	/**
	 * Handles respawning by resetting health to max.
	 * Targets ServerPlayerEntity.copyFrom() - called ONLY when:
	 * - Player dies and respawns (alive = false)
	 * - Player changes dimensions (alive = true) → ignored
	 */
	@Inject(method = "copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V", at = @At("TAIL"))
	private void onPlayerRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
		if (!alive) {
			PlayerAttributeInstance health = rpgfantasy$attributes.get("health");
			health.setCurrent(health.getMax());
		}
	}
}
