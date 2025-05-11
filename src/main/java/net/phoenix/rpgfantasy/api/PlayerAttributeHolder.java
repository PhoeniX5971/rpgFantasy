package net.phoenix.rpgfantasy.api;

import net.phoenix.rpgfantasy.attribute.PlayerAttributes;
import net.phoenix.rpgfantasy.level.PlayerLevelData;

public interface PlayerAttributeHolder {

	PlayerAttributes getAttributes();

	PlayerLevelData getLevelData();
}
