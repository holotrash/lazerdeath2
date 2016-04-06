package com.holotrash.lazerdeath2;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Unit {
	
	
	public boolean isDead();
	
	public boolean isDude();
	
	public boolean hasMoved();
	
	public void setMoved();
	
	public void setMovable();
	
	public void takeDmg(int dmg);
	
	public int hp();
	
	public int speed();
	
	public int strength();
	
	public int dodge();
	
	public int accuracy();
	
	public int range();
	
	public Coord position();
	
	public Sprite sprite();
	
	public void move(Coord destination);
	
	public String name();
}
