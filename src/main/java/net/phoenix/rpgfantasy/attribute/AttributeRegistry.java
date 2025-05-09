package net.phoenix.rpgfantasy.attribute;

public class AttributeRegistry {
	public static void registerDefaultAttributes(PlayerAttributes attributes) {
		attributes.registerAttribute("mana",
				new PlayerAttributeInstance(-1, 0.1)); // baseMax=-1 (unset), regen=0.1
		attributes.registerAttribute("health",
				new PlayerAttributeInstance(-1, 0.1));
		// Add more here...
	}
}
