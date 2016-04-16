/**
 *  Weapon.java
 *  ----  
 *  Weapons have only one purpose: to do damage to units.
 *  
 *  It is my belief that the individuals that operate the companies that
 *  manufacture these Weapons should be brought to justice. As I am one
 *  of these individuals, it is my hope that they are not.
 *  
 *  P.S. I lied. Reusable healing items must be equipped as weapons. So
 *  weapons have only two purposes: to do damage to units, and to serve
 *  as reusable healing items because the author of Lazerdeath2 is too
 *  lazy to make them two distinct things that inherit from the same 
 *  parent.
 *  ---------------------------------------------------------------------
 *  This file is part of the computer game Lazerdeath2 
 *  Copyright 2016, Robert Watson Craig III
 *
 *  Lazerdeath2 is free software published under the terms of the GNU
 *  General Public License version 3. You can redistribute it and/or 
 *  modify it under the terms of the GPL (version 3 or any later version).
 * 
 *  Lazerdeath2 is distributed in the hope that it will be entertaining,
 *  cool, and totally smooth for your mind to rock to, daddy, but WITHOUT 
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 *  FITNESS FOR A PARTICULAR PURPOSE; without even the suggestion of an
 *  implication that any of this code makes any sense whatsoever. It works
 *  on my computer and I don't think that's such a weird environment, but
 *  it might be. Or maybe it's your computer that's the weird one, did you
 *  ever think of that?  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lazerdeath2.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package com.holotrash.lazerdeath2.Units;

import java.util.Random;

public class Weapon {

	private String name;
	public int range;
	public int minDamage;
	public int dmgRange;
	public boolean healing;
	private Random dice;
	private WeaponType type;
	
	public Weapon(WeaponType type){
		this.dice = new Random();
		this.type = type;
		if (type == WeaponType.LAZER_LV1){
			this.range = 6;
			this.minDamage = 3;
			this.dmgRange = 3;
			this.healing = false;
			this.name = "Lv1 Lazer";
		} else if (type == WeaponType.LAZER_LV2){
			this.range = 7;
			this.minDamage = 4;
			this.dmgRange = 6;
			this.healing = false;
			this.name = "Lv2 Lazer";
		} else if (type == WeaponType.LAZER_LV3){
			this.range = 8;
			this.minDamage = 8;
			this.dmgRange = 10;
			this.healing = false;
			this.name = "Lv3 Lazer";
		} else if (type == WeaponType.PARTICLE_BEAM_LV1) {
			this.range = 8;
			this.minDamage = 6;
			this.dmgRange = 4;
			this.healing = false;
			this.name = "Lv1 Particle Beam";
		} else if (type == WeaponType.PARTICLE_BEAM_LV2) {
			this.range = 9;
			this.minDamage = 9;
			this.dmgRange = 3;
			this.healing = false;
			this.name = "Lv2 Particle Beam";
		} else if (type == WeaponType.PARTICLE_BEAM_LV3) {
			this.range = 12;
			this.minDamage = 10;
			this.dmgRange = 5;
			this.healing = false;
			this.name = "Lv3 Particle Beam";
		} else if (type == WeaponType.PHASE_BLUDGEON_LV1) {
			this.range = 1;
			this.minDamage = 3;
			this.dmgRange = 2;
			this.healing = false;
			this.name = "Lv1 Phase Bludgeon";
		} else if (type == WeaponType.PHASE_BLUDGEON_LV2) {
			this.range = 1;
			this.minDamage = 5;
			this.dmgRange = 3;
			this.healing = false;
			this.name = "Lv2 Phase Bludgeon";
		} else if (type == WeaponType.PHASE_BLUDGEON_LV3) {
			this.range = 2;
			this.minDamage = 7;
			this.dmgRange = 4;
			this.healing = false;
			this.name = "Lv3 Phase Bludgeon";
		} else if (type == WeaponType.PSIONIC_WILL_LV1) {
			this.range = 7;
			this.minDamage = 3;
			this.dmgRange = 7;
			this.healing = false;
			this.name = "Lv1 Psionic Will";
		} else if (type == WeaponType.PSIONIC_WILL_LV2) {
			this.range = 8;
			this.minDamage = 6;
			this.dmgRange = 9;
			this.healing = false;
			this.name = "Lv2 Psionic Will";
		} else if (type == WeaponType.PSIONIC_WILL_LV3) {
			this.range = 9;
			this.minDamage = 7;
			this.dmgRange = 13;
			this.healing = false;
			this.name = "Lv3 Psionic Will";
		} else if (type == WeaponType.LAZER_FIST_LV1) {
			this.range = 1;
			this.minDamage = 8;
			this.dmgRange = 4;
			this.healing = false;
			this.name = "Lv1 Lazer Fist";
		} else if (type == WeaponType.LAZER_FIST_LV2) {
			this.range = 1;
			this.minDamage = 15;
			this.dmgRange = 2;
			this.healing = false;
			this.name = "Lv2 Lazer Fist";
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV1) {
			this.range = 1;
			this.minDamage = 4;
			this.dmgRange = 2;
			this.healing = true;
			this.name = "Lv1 Tissue Replcatr";
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV2) {
			this.range = 1;
			this.minDamage = 5;
			this.dmgRange = 3;
			this.healing = true;
			this.name = "Lv2 Tissue Replcatr";
		} else if (type == WeaponType.TISSUE_REPLICATOR_LV3) {
			this.range = 1;
			this.minDamage = 9;
			this.dmgRange = 4;
			this.healing = true;
			this.name = "Lv3 Tissue Replicatr";
		}
	}

	public int getDamage() {
		int returnVal = dice.nextInt(dmgRange) + minDamage;
		if (this.healing) {
			returnVal = 0 - returnVal;
		}
		return returnVal; 
	}
	
	public String toString(){
		return this.name;
	}
	
	public boolean isPsionic(){
		return this.type == WeaponType.PSIONIC_WILL_LV1
				|| this.type == WeaponType.PSIONIC_WILL_LV2
				|| this.type == WeaponType.PSIONIC_WILL_LV3;
	}
}
