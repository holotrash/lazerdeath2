package com.holotrash.lazerdeath2;

import java.util.Random;

public class Weapon {

	public int range;
	public int maxDamage;
	public int minDamage;
	private Random dice;
	
	public Weapon(WeaponType type){
		this.dice = new Random();
		if (type == WeaponType.LAZER_LV1){
			this.range = 6;
			this.minDamage = 3;
			this.maxDamage = 6;
		} else if (type == WeaponType.LAZER_LV2){
			this.range = 7;
			this.minDamage = 4;
			this.maxDamage = 10;
		} else if (type == WeaponType.LAZER_LV3){
			this.range = 8;
			this.minDamage = 8;
			this.maxDamage = 18;
		} else if (type == WeaponType.PARTICLE_BEAM_LV1) {
			this.range = 8;
			this.minDamage = 6;
			this.maxDamage = 10;
		} else if (type == WeaponType.PARTICLE_BEAM_LV2) {
			this.range = 9;
			this.minDamage = 9;
			this.maxDamage = 12;
		} else if (type == WeaponType.PARTICLE_BEAM_LV3) {
			this.range = 12;
			this.minDamage = 10;
			this.maxDamage = 15;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV1) {
			this.range = 1;
			this.minDamage = 3;
			this.maxDamage = 5;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV2) {
			this.range = 1;
			this.minDamage = 5;
			this.maxDamage = 8;
		} else if (type == WeaponType.PHASE_BLUDGEON_LV3) {
			this.range = 2;
			this.minDamage = 7;
			this.maxDamage = 11;
		} else if (type == WeaponType.PSIONIC_WILL_LV1) {
			this.range = 7;
			this.minDamage = 3;
			this.maxDamage = 10;
		} else if (type == WeaponType.PSIONIC_WILL_LV2) {
			this.range = 8;
			this.minDamage = 6;
			this.maxDamage = 15;
		} else if (type == WeaponType.PSIONIC_WILL_LV3) {
			this.range = 9;
			this.minDamage = 7;
			this.maxDamage = 20;
		} else if (type == WeaponType.LAZER_FIST_LV1) {
			this.range = 1;
			this.minDamage = 8;
			this.maxDamage = 12;
		} else if (type == WeaponType.LAZER_FIST_LV2) {
			this.range = 1;
			this.minDamage = 15;
			this.maxDamage = 17;
		}
	}
	
	
}
