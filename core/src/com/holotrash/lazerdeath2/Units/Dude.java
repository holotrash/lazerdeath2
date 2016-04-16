/**
 *  Dude.java
 *  ----  
 *  A non-gender-specific Unit that is a part of the player's party.
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
import com.holotrash.lazerdeath2.Items.Consumable;
import com.holotrash.lazerdeath2.Items.ConsumableEffect;
import com.holotrash.lazerdeath2.LazerMath.Coord;

public class Dude implements Unit, Mover{
	
	private String name;
	
	private int hp;       // hit points
	private int maxHp;
	private int pp;
	private int maxPp;
	private int speed;    // number of tiles that a Dude can move per turn
	private int strength;
	private int dodge;    // percent chance to avoid attack (unmodified by opponent's accuracy)
	private int accuracy; // percent chance to hit (unmodified by opponent's dodge)
	private int ap;
	private int maxAp;
	private Coord position; // position on map;
	
	private Sprite sprite;
	private Sprite glamourShot;
	private GameMaster gm;
	
	private ArrayList<ConsumableEffect> effects;
	
	private Weapon weapon;

	private Random dice;
	
	private ArrayList<String> displayStatistics;
	
	public Dude(String name,
			    Texture texture, 
			    int hp,
			    int pp,
			    int speed, 
			    int strength, 
			    int dodge, 
			    int accuracy,
			    int ap,
			    Coord position,
			    GameMaster gm,
			    Sprite glamourShot)
	{
		this.name = name;
		sprite = new Sprite(texture, 0, 0, 128, 128);
		this.hp = hp;
		this.maxHp = hp;
		this.maxPp = pp;
		this.pp = pp;
		this.speed = speed;
		this.strength = strength;
		this.dodge = dodge;
		this.accuracy = accuracy;
		this.maxAp = ap;
		this.ap = ap;
		this.position = position;
		this.gm = gm;
		this.dice = new Random();
		this.weapon = null;
		this.glamourShot = glamourShot;
		this.displayStatistics = new ArrayList<String>();
		displayStatistics.add("NAME: " + this.name.toUpperCase());
		displayStatistics.add("HP: " + this.hp + "   ACCURACY: " + this.accuracy);
		displayStatistics.add("SPEED: " + this.speed + "    AP: " + this.ap);
		displayStatistics.add("DODGE: " + this.dodge + " STRENGTH: " + this.strength);
		if (this.weapon == null){
			displayStatistics.add("WEAPON: UNARMED");
		} else {
			displayStatistics.add("WEAPON: " + this.weapon.toString().toUpperCase());
		}

		gm.mapData.setTileOccupied(position, true);
		this.effects = new ArrayList<ConsumableEffect>();
	}
	
	public Dude() {
			
	}

	@Override
	public void takeDmg(int dmg){
		this.hp = this.hp - dmg;
		displayStatistics.set(1, "HP: " + this.hp + "   ACCURACY: " + this.accuracy);
		
	}
	
	@Override
	public int hp(){
		return hp;
	}
	
	@Override
	public int speed(){
		return speed;
	}
	
	@Override
	public int strength(){
		return strength;
	}
	
	@Override
	public int dodge(){
		return dodge;
	}
	
	@Override
	public int accuracy(){
		return accuracy;
	}
	
	@Override
	public int range(){
		int returnVal;
		if (weapon != null)
			returnVal = weapon.range;
		else
			returnVal = 1;
		return returnVal;
	}
	
	@Override
	public Coord position(){
		return position;
	}
	
	@Override
	public Sprite sprite(){
		return sprite;
	}

	@Override
	public boolean isDead() {
		return (this.hp <= 0);
	}
	
	@Override
	public boolean isDude(){
		return true;
	}

	@Override
	public void move(Coord destination) {
		if (gm.tileMath.unitMoveCoords(this, 1).contains(destination) && this.act(1)){
			gm.mapData.setTileOccupied(this.position, false);
			gm.mapData.setTileOccupied(destination, true);
			this.position = destination;
			this.getFloorItems();
			// TODO play move sound
		} else if (gm.tileMath.unitMoveCoords(this, 2).contains(destination) && this.act(2)){
			gm.mapData.setTileOccupied(this.position, false);
			gm.mapData.setTileOccupied(destination, true);
			this.position = destination;
			this.getFloorItems();
			// TODO play move sound
		}
	}
	
	public String name(){
		return this.name;
	}

	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
		displayStatistics.set(4, "WEAPON: " + this.weapon.toString().toUpperCase());
	}
	
	// returns true on hit, false on miss
	public boolean attack(Unit unit){
		boolean attackHit = false;
		int percentChanceToHit = ((unit.dodge() + this.accuracy())/2);
		int diceRoll = this.dice.nextInt(99) + 1;
		if (this.weapon.isPsionic() && this.pp < 1){
			gm.printToConsole(this.name + " doesn't have enough Psi Points.");
		} else if (this.act(1)){
			if (!(diceRoll > percentChanceToHit)){
				attackHit = true;
				int damage = weapon.getDamage();
				unit.takeDmg(damage); 
				gm.printToConsole(this.name + " hits! " + unit.name() + " takes " + damage + "damage!");
				
				if (unit.hp() > 0){
					gm.printToConsole(unit.name() + " has " + unit.hp() + " hit points remaining.");
				} else {
					gm.printToConsole(this.name + " killed " + unit.name() + "!");
				}
				
			} else {
				attackHit = false;
				gm.printToConsole(this.name + " misses!");
			}
		} else {
			gm.printToConsole("Not enough AP for " + this.name + " to attack!");
		}
		return attackHit;
	}

	@Override
	public Sprite glamourShot() {
		return glamourShot;
	}

	@Override
	public ArrayList<String> toStringz() {
		return this.displayStatistics;
	}
	
	@Override
	public void die(){
		gm.mapData.setTileOccupied(this.position, false);
	}
	
	public int ap(){
		return this.ap;
	}
	
	public void resetAp(){
		this.ap = this.maxAp;
		displayStatistics.set(2, "SPEED: " + this.speed + "    AP: " + this.ap);
	}
	
	// returns true if the action is successful (the unit has enough ap)
	// returns false if not enough ap to perform the action
	public boolean act(int apCost){
		boolean result;
		if (apCost<=this.ap){
			result = true;
			this.ap = this.ap - apCost;
			displayStatistics.set(2, "SPEED: " + this.speed + "    AP: " + this.ap);
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public boolean hasMoved() {
		return this.ap <= 0;
	}
	
	public boolean tileInInteractRange(Coord c){
		boolean result;
		
		if (c.x() == this.position.x()
				&& Math.abs(c.y() - this.position.y()) == 1){
			result = true;
		} else if (c.y() == this.position.y()
				&& Math.abs(c.x() - this.position.x()) == 1){
			result = true;
		} else {
			result = false;
		}		
		
		return result;
	}

	public int pp(){
		return pp;
	}
	
	public void usePp(int amt){
		this.pp = this.pp - amt;
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
	
	private void getFloorItems(){
		if(gm.itemWrangler.items.containsKey(position)){
			//TODO play get item sound
			gm.inventory.add(gm.itemWrangler.items.get(position));
			gm.itemWrangler.items.remove(position);
		}
	}
}
