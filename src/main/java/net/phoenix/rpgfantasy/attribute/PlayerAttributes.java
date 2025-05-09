package net.phoenix.rpgfantasy.attribute;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;

public class PlayerAttributes {
	private final Map<String, PlayerAttributeInstance> attributes = new HashMap<>();

	public PlayerAttributes() {
		AttributeRegistry.registerDefaultAttributes(this);
	}

	public void registerAttribute(String id, PlayerAttributeInstance attribute) {
		attributes.put(id, attribute);
	}

	public PlayerAttributeInstance get(String key) {
		return attributes.get(key);
	}

	public void fromNbt(NbtCompound nbt) {
		for (String key : attributes.keySet()) {
			if (nbt.contains(key, NbtElement.COMPOUND_TYPE)) {
				attributes.get(key).fromNbt(nbt.getCompound(key));
			}
		}
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		for (Map.Entry<String, PlayerAttributeInstance> entry : attributes.entrySet()) {
			nbt.put(entry.getKey(), entry.getValue().toNbt());
		}
		return nbt;
	}
}
