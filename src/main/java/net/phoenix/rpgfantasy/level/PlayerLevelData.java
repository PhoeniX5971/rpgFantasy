package net.phoenix.rpgfantasy.level;

import net.minecraft.nbt.NbtCompound;

public class PlayerLevelData {
	private int level = 1;
	private int experience = 0;
	private int unallocatedPoints = 0;

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return experience;
	}

	public int getUnallocatedPoints() {
		return unallocatedPoints;
	}

	public void addExperience(int amount) {
		this.experience += amount;
		checkLevelUp();
	}

	public void addUnallocatedPoints(int points) {
		this.unallocatedPoints += points;
	}

	public boolean useUnallocatedPoint() {
		if (unallocatedPoints > 0) {
			unallocatedPoints--;
			return true;
		}
		return false;
	}

	private void checkLevelUp() {
		int requiredExp = getRequiredExperience(level);
		while (experience >= requiredExp) {
			experience -= requiredExp;
			level++;
			unallocatedPoints += 2; // Give 2 points per level
			requiredExp = getRequiredExperience(level);
		}
	}

	public int getRequiredExperience(int level) {
		// Example: exponential growth
		return (int) (100 * Math.pow(1.1, level - 1));
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("level", level);
		nbt.putInt("experience", experience);
		nbt.putInt("unallocatedPoints", unallocatedPoints);
		return nbt;
	}

	public void fromNbt(NbtCompound nbt) {
		this.level = nbt.getInt("level");
		this.experience = nbt.getInt("experience");
		this.unallocatedPoints = nbt.getInt("unallocatedPoints");
	}
}
