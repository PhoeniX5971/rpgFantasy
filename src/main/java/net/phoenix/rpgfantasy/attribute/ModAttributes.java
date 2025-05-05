package net.phoenix.rpgfantasy.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.phoenix.rpgfantasy.RpgFantasy;

public class ModAttributes {
	public static final RegistryKey<EntityAttribute> MANA_KEY = RegistryKey.of(Registries.ATTRIBUTE.getKey(),
			Identifier.of(RpgFantasy.MOD_ID, "mana"));

	public static final RegistryEntry<EntityAttribute> MANA = Registry.registerReference(
			Registries.ATTRIBUTE,
			MANA_KEY,
			new ClampedEntityAttribute("attribute.name.generic.mana", 100.0, 0.0, 1000.0).setTracked(true));

	public static void register() {
		RpgFantasy.LOGGER.info("Registering Mod Attributes for " + RpgFantasy.MOD_ID);
	}
}
