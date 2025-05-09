package net.phoenix.rpgfantasy.attribute;

import net.minecraft.nbt.NbtCompound;

// Per-player attribute instance
public class PlayerAttributeInstance {
	private int level = 10;
	private double baseMax = 10;
	private double bonusMax = 0;
	private double current = baseMax;
	private double regenRate = 0;

	public PlayerAttributeInstance() {
	}

	public PlayerAttributeInstance(double baseMax, double regenRate) {
		if (baseMax > 0) {
			this.baseMax = baseMax;
		}
		this.regenRate = regenRate;
	}

	public double getMin() {
		return 0;
	}

	public double getMax() {
		return baseMax + bonusMax + level * 1.5; // Multiplier per level
	}

	public double getCurrent() {
		return current;
	}

	public double getRegenRate() {
		return regenRate;
	}

	public void setCurrent(double value) {
		current = Math.max(getMin(), Math.min(getMax(), value));
	}

	public void addBonusMax(double bonus) {
		bonusMax += bonus;
	}

	public void clearBonus() {
		bonusMax = 0;
	}

	public void levelUp() {
		level++;
	}

	public int getLevel() {
		return level;
	}

	public void setRegenRate(double rate) {
		this.regenRate = rate;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("Level", level);
		nbt.putDouble("Current", current);
		nbt.putDouble("BonusMax", bonusMax);
		return nbt;
	}

	public void fromNbt(NbtCompound nbt) {
		this.level = nbt.getInt("Level");
		this.current = nbt.getDouble("Current");
		this.bonusMax = nbt.getDouble("BonusMax");
	}
}
