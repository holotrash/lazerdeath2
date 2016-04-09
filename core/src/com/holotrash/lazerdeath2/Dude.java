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

package com.holotrash.lazerdeath2;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.util.pathfinding.Mover;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Dude implements Unit, Mover{
	
	private String name;
	private int hp;       // hit points
	private int speed;    // number of tiles that a Dude can move per turn
	private int strength;
	private int dodge;    // percent chance to avoid attack (unmodified by opponent's accuracy)
	private int accuracy; // percent chance to hit (unmodified by opponent's dodge)
	private Coord position; // position on map;
	private Sprite sprite;
	private Sprite glamourShot;
	private GameMaster gm;
	
	private Weapon weapon;
	
	private boolean hasMoved;
	private boolean hasAttacked;
	private Random dice;
	
	private ArrayList<String> displayStatistics;
	
	public Dude(String name,
			    Texture texture, 
			    int hp, 
			    int speed, 
			    int strength, 
			    int dodge, 
			    int accuracy, 
			    Coord position,
			    GameMaster gm,
			    Sprite glamourShot)
	{
		this.name = name;
		sprite = new Sprite(texture, 0, 0, 128, 128);
		this.hp = hp;
		this.speed = speed;
		this.strength = strength;
		this.dodge = dodge;
		this.accuracy = accuracy;
		this.position = position;
		this.gm = gm;
		this.hasMoved = false;
		this.hasAttacked = false;
		this.dice = new Random();
		this.weapon = null;
		this.glamourShot = glamourShot;
		this.displayStatistics = new ArrayList<String>();
		displayStatistics.add("NAME: " + this.name.toUpperCase());
		displayStatistics.add("HP: " + this.hp);
		displayStatistics.add("SPEED: " + this.speed);
		displayStatistics.add("DODGE: " + this.dodge);
		if (this.weapon == null){
			displayStatistics.add("WEAPON: UNARMED");
		} else {
			displayStatistics.add("WEAPON: " + this.weapon.toString().toUpperCase());
		}

	}
	
	public Dude() {
			
	}

	@Override
	public void takeDmg(int dmg){
		this.hp = this.hp - dmg;
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
		return (this.hp == 0);
	}
	
	@Override
	public boolean isDude(){
		return true;
	}

	@Override
	public void move(Coord destination) {
		if (!this.hasMoved && gm.tileMath.unitMoveCoords(this).contains(destination)){
			gm.mapData.setTileOccupied(this.position, false);
			gm.mapData.setTileOccupied(this.position, true);
			this.position = destination;
			// TODO play move sound
			this.hasMoved = true;
		}
	}
	
	public String name(){
		return this.name;
	}

	@Override
	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public void setMoved() {
		this.hasMoved = true;
		
	}

	@Override
	public void setMovable() {
		this.hasMoved = false;
		
	}
	
	public void setCanAttack(){
		this.hasAttacked = false;
	}
	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
		displayStatistics.set(4, "WEAPON: " + this.weapon.toString().toUpperCase());
	}
	
	// returns true on hit, false on miss
	public boolean Attack(Unit unit){
		boolean attackHit = false;
		int percentChanceToHit = ((unit.dodge() + this.accuracy())/2);
		int diceRoll = this.dice.nextInt(99) + 1;
		if (!hasAttacked){
			if (!(diceRoll > percentChanceToHit)){
				attackHit = true;
				int damage = weapon.getDamage();
				unit.takeDmg(damage); 
				gm.printToConsole(this.name + " hits! " + unit.name() + " takes " + damage + "damage!");
				System.out.println("Attack hit! " + unit.name() + " takes " + damage + " damage." );
				gm.printToConsole(unit.name() + " has " + unit.hp() + " hit points remaining.");
				System.out.println(unit.name() + " has " + unit.hp() + " hit points.");
			} else {
				attackHit = false;
				System.out.println("Attack miss!");
				gm.printToConsole(this.name + " misses!");
			}
			this.hasAttacked = true;
		}
		return attackHit;
	}

	public boolean hasAttacked() {
		return this.hasAttacked;
	}

	@Override
	public Sprite glamourShot() {
		return glamourShot;
	}

	@Override
	public ArrayList<String> toStringz() {
		return this.displayStatistics;
	}
	
}
