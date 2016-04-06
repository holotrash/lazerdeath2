package com.holotrash.lazerdeath2;

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
	
	public Enemy(String name,
			Texture texture, 
		    int hp, 
		    int speed, 
		    int strength, 
		    int dodge, 
		    int accuracy, 
		    int range, 
		    Coord position)
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
		return (hp == 0);
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
}
