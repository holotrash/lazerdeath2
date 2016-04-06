package com.holotrash.lazerdeath2;

import java.util.Random;

public class Weapon {

	public int range;
	public int minDamage;
	public int dmgRange;
	public boolean healing;
	private Random dice;
	
	public Weapon(WeaponType type){
		this.dice = new Random();
		if (type == WeaponType.LAZER_LV1){
			this.range = 6;
			this.minDamage = 3;
			this.dmgRange = 3;
			this.healing = false;
		} else if (type == WeaponType.LAZER_LV2){
			this.range = 7;
			this.minDamage = 4;
			this.dmgRange = 6;
			this.healing = false;
		} else if (type == WeaponType.LAZER_LV3){
			this.range = 8;
			this.minDamage = 8;
			this.dmgRange = 10;
			this.healing = false;
		} else if (type == WeaponType.PARTICLE_BEAM_LV1) {
			this.range = 8;
			this.minDamage = 6;
			this.dmgRange = 4;
			this.healing = false;
		} else if (type == WeaponType.PARTICLE_BEAM_LV2) {
			this.range = 9;
			this.minDamage = 9;
			this.dmgRange = 3;
			this.healing = false;
		} else if (type == WeaponType.PARTICLE_BEAM_LV3) {
			this.range = 12;
			this.minDamage = 10;
			this.dmgRange = 5;
			this.healing = false;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV1) {
			this.range = 1;
			this.minDamage = 3;
			this.dmgRange = 2;
			this.healing = false;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV2) {
			this.range = 1;
			this.minDamage = 5;
			this.dmgRange = 3;
			this.healing = false;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV3) {
			this.range = 2;
			this.minDamage = 7;
			this.dmgRange = 4;
			this.healing = false;
		} else if (type == WeaponType.PSIONIC_WILL_LV1) {
			this.range = 7;
			this.minDamage = 3;
			this.dmgRange = 7;
			this.healing = false;
		} else if (type == WeaponType.PSIONIC_WILL_LV2) {
			this.range = 8;
			this.minDamage = 6;
			this.dmgRange = 9;
			this.healing = false;
		} else if (type == WeaponType.PSIONIC_WILL_LV3) {
			this.range = 9;
			this.minDamage = 7;
			this.dmgRange = 13;
			this.healing = false;
		} else if (type == WeaponType.LAZER_FIST_LV1) {
			this.range = 1;
			this.minDamage = 8;
			this.dmgRange = 4;
			this.healing = false;
		} else if (type == WeaponType.LAZER_FIST_LV2) {
			this.range = 1;
			this.minDamage = 15;
			this.dmgRange = 2;
			this.healing = false;
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV1) {
			this.range = 1;
			this.minDamage = 4;
			this.dmgRange = 2;
			this.healing = true;
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV2) {
			this.range = 1;
			this.minDamage = 5;
			this.dmgRange = 3;
			this.healing = true;
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV3) {
			this.range = 1;
			this.minDamage = 9;
			this.dmgRange = 4;
			this.healing = true;
		}
	}

	public int getDamage() {
		int returnVal = dice.nextInt(dmgRange) + minDamage;
		if (this.healing) {
			returnVal = 0 - returnVal;
		}
		return returnVal; 
	}
	
	
}
