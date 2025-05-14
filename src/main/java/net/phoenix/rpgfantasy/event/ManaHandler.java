package net.phoenix.rpgfantasy.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.phoenix.rpgfantasy.api.PlayerAttributeHolder;
import net.phoenix.rpgfantasy.attribute.PlayerAttributes;
import net.phoenix.rpgfantasy.attribute.PlayerAttributeInstance;

public class ManaHandler {
	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				onPlayerTick(player);
			}
		});
	}

	private static void onPlayerTick(ServerPlayerEntity player) {
		PlayerAttributes attributes = ((PlayerAttributeHolder) player).getAttributes();
		PlayerAttributeInstance mana = attributes.get("mana");
		mana.setCurrent(mana.getCurrent() + (mana.getRegenRate() * mana.getMax() / 100));
	}
}
