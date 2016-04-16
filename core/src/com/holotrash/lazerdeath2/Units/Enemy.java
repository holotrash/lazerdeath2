/**
 *  Enemy.java
 *  ----  
 *  A Unit controlled by the EnemyAi that wants to kill the player's dudes
 *  with all of it's little heart.
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

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.util.pathfinding.Mover;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.Items.ConsumableEffect;
import com.holotrash.lazerdeath2.LazerMath.Coord;

public class Enemy implements Unit, Mover{
	private String name;
	
	// statistics
	
	private int hp;       // hit points
	private int ap;
	private int speed;    // number of tiles that a Dude can move per turn
	private int strength;
	private int dodge;    // percent chance to avoid attack (unmodified by opponent's accuracy)
	private int accuracy; // percent chance to hit (unmodified by opponent's dodge)
	
	// personality traits - these inform EnemyAi how to determine action. 
	//                      each personality trait ranges from 0-99
	
	private int jitter;   // percent chance to do something weird
	private int guts;     // enemies with high guts are more likely to advance
						  // towards the players units. if an enemy fails a guts check,
						  // it may hide.
	                           
	private ArrayList<ConsumableEffect> effects;
	
	private Coord position; // position on Map
	private Sprite sprite;
	private Sprite glamourShot;
	
	private boolean hasMoved;
	private boolean hasAttacked;
	
	private Random dice;
	
	private Weapon weapon;
	private GameMaster gm;
	
	private ArrayList<String> displayStats;
	
	public Enemy(String name,
			Texture texture, 
		    int hp, 
		    int speed, 
		    int strength, 
		    int dodge, 
		    int accuracy, 
		    int jitter,
		    int guts,
		    Coord position,
		    Weapon weapon,
		    Sprite glamourShot,
		    GameMaster gm)
	{
	this.name = name;
	sprite = new Sprite(texture, 0, 0, 128, 128);
	this.hp = hp;
	this.speed = speed;
	this.strength = strength;
	this.dodge = dodge;
	this.accuracy = accuracy;
	this.jitter = jitter;
	this.guts = guts;
	this.position = position;
	this.hasMoved = false;
	this.hasAttacked = false;
	this.dice = new Random();
	this.weapon = weapon;
	this.glamourShot = glamourShot;
	this.gm = gm;
	this.ap = 2;
	
	this.displayStats = new ArrayList<String>();
	displayStats.add("NAME: " + this.name.toUpperCase());
	displayStats.add("HP: " + this.hp);
	displayStats.add("SPEED: " + this.speed);
	displayStats.add("DODGE: " + this.dodge);
	if (this.weapon == null){
		displayStats.add("WEAPON: UNARMED");
	} else {
		displayStats.add("WEAPON: " + this.weapon.toString().toUpperCase());
	}
	gm.mapData.setTileOccupied(position, true);
	}
	
	public void takeDmg(int dmg){
		this.hp = this.hp - dmg;
		displayStats.set(1, "HP: " + this.hp);
	}
	
	public int hp(){
		return hp;
	}
	
	public int speed(){
		return speed;
	}
	
	public int strength(){
		return strength;
	}
	
	public int dodge(){
		return dodge;
	}
	
	public int accuracy(){
		return accuracy;
	}
	
	public int range(){
		int range;
		if (weapon == null){
			range = 1;
		} else {
			range = weapon.range;
		}
		return range;
	}
	
	public Sprite sprite(){
		return sprite;
	}
	
	public Coord position(){
		return position;
	}
	
	public int guts(){
		return guts;
	}
	
	public int jitter(){
		return jitter;
	}

	@Override
	public boolean isDead() {
		return !(hp > 0);
	}

	@Override
	public boolean isDude() {
		return false;
	}

	@Override
	public void move(Coord destination) {
		if (destination != null && !Coord.coordsEqual(destination, position) && gm.tileMath.pathExists(position, destination, speed)){
			// TODO play move sound
			gm.mapData.setTileOccupied(this.position, false);
			gm.mapData.setTileOccupied(destination, true);
			this.position = destination;
		}		
	}
	
	public String name(){
		return name;
	}

	@Override
	public boolean hasMoved() {
		return hasMoved;
	}

	public void setMoved() {
		this.hasMoved = true;
		
	}

	public void setMovable() {
		this.hasMoved = false;
	}

	public void setCanAttack(){
		this.hasAttacked = false;
	}
	
	public boolean hasAttacked() {
		return this.hasAttacked;
	}
	
	// returns true on hit, false on miss
	public boolean attack(Unit unit){
		boolean attackHit;
		int percentChanceToHit = ((unit.dodge() + this.accuracy())/2);
		int diceRoll = this.dice.nextInt(99) + 1;
		int damage;
		
		if (!(diceRoll > percentChanceToHit)){
			attackHit = true;
			damage = weapon.getDamage();
			if (damage > unit.hp())
				damage = unit.hp();
			unit.takeDmg(damage);
			gm.printToConsole(this.name + " hits " + unit.name() + " for " + damage + " hp.");
		} else {
			attackHit = false;
			gm.printToConsole(this.name + " misses " + unit.name() + ".");
		}
		this.hasAttacked = true;
		return attackHit;
	}
	
	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
	}

	@Override
	public Sprite glamourShot() {
		return glamourShot;
	}

	@Override
	public ArrayList<String> toStringz() {
		return this.displayStats;
	}
	
	@Override
	public void die(){
		gm.mapData.setTileOccupied(this.position, false);
	}

	@Override
	public int ap() {
		return ap;
	}

	@Override
	public void addEffects(ArrayList<ConsumableEffect> effects) {
		for (ConsumableEffect e : effects){
			e.expiration = e.duration + gm.turn();
			this.effects.add(e);
		}
	}
	
	public void updateEffects(){
		for (int i=0;i<this.effects.size();i++){
			while (this.effects.get(i).expiration > gm.turn()){
				this.effects.remove(i);
				// kind of iffy about removing in this loop because it affects
				// index/size but if there are problems maybe use another strategy
			}
		}
	}
}
