package net.phoenix.rpgfantasy.attribute;

import net.minecraft.nbt.NbtCompound;

// Per-player attribute instance
public class PlayerAttributeInstance {
	private int allocatedPoints = 0;
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
		if (regenRate > 0) {
			this.regenRate = regenRate;
		}
	}

	public double getMin() {
		return 0;
	}

	public double getMax() {
		return baseMax + bonusMax + allocatedPoints * 1.5; // Multiplier per allocatedPoints
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

	public void allocatePoint() {
		allocatedPoints++;
	}

	public int getAllocatedPoints() {
		return allocatedPoints;
	}

	public void setRegenRate(double rate) {
		this.regenRate = rate;
	}

	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putInt("allocatedPoints", allocatedPoints);
		nbt.putDouble("Current", current);
		nbt.putDouble("BonusMax", bonusMax);
		return nbt;
	}

	public void fromNbt(NbtCompound nbt) {
		this.allocatedPoints = nbt.getInt("allocatedPoints");
		this.current = nbt.getDouble("Current");
		this.bonusMax = nbt.getDouble("BonusMax");
	}
}
