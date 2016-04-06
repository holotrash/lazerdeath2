package com.holotrash.lazerdeath2;

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
	private int range;    // if an opponent is (range) tiles away, he or she can be attacked
	private Coord position; // position on map;
	private Sprite sprite;
	private GameMaster gm;
	
	private boolean hasMoved;
	
	public Dude(String name,
			    Texture texture, 
			    int hp, 
			    int speed, 
			    int strength, 
			    int dodge, 
			    int accuracy, 
			    int range, 
			    Coord position,
			    GameMaster gm)
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
		this.gm = gm;
		this.hasMoved = false;
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
		return range;
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
	
}
