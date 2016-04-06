package com.holotrash.lazerdeath2;

import java.util.Random;

import org.newdawn.slick.util.pathfinding.Mover;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Enemy implements Unit, Mover{
	private String name;
	private int hp;       // hit points
	private int speed;    // number of tiles that a Dude can move per turn
	private int strength;
	private int dodge;    // percent chance to avoid attack (unmodified by opponent's accuracy)
	private int accuracy; // percent chance to hit (unmodified by opponent's dodge)
	private int range;    // if an opponent is (range) tiles away, he or she can be attacked
	private Coord position; // position on Map
	private Sprite sprite;
	
	private boolean hasMoved;
	private boolean hasAttacked;
	
	private Random dice;
	
	private Weapon weapon;
	
	public Enemy(String name,
			Texture texture, 
		    int hp, 
		    int speed, 
		    int strength, 
		    int dodge, 
		    int accuracy, 
		    int range, 
		    Coord position,
		    Weapon weapon)
	{
	this.name = name;
	sprite = new Sprite(texture, 0, 0, 128, 128);
	this.hp = hp;
	this.speed = speed;
	this.strength = strength;
	this.dodge = dodge;
	this.accuracy = accuracy;
	this.range = range;
	this.position = position;
	this.hasMoved = false;
	this.hasAttacked = false;
	this.dice = new Random();
	this.weapon = weapon;
	}
	
	public void takeDmg(int dmg){
		this.hp = this.hp - dmg;
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
		return range;
	}
	
	public Sprite sprite(){
		return sprite;
	}
	
	public Coord position(){
		return position;
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
		// TODO play move sound
		this.position = destination;		
	}
	
	public String name(){
		return name;
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
	
	public boolean hasAttacked() {
		return this.hasAttacked;
	}
	
	// returns true on hit, false on miss
	public boolean Attack(Unit unit){
		boolean attackHit;
		int percentChanceToHit = ((unit.dodge() + this.accuracy())/100);
		int diceRoll = this.dice.nextInt(99) + 1;
		
		if (!(diceRoll > percentChanceToHit)){
			attackHit = true;
			unit.takeDmg(weapon.getDamage());
		} else {
			attackHit = false;
		}
		this.hasAttacked = true;
		return attackHit;
	}
	
	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
	}
}
