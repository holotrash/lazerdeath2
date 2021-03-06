package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Units.Unit;

public interface Item {

	public String name();
	
	public String description();
	
	public Sprite tileSprite();
	
	public Sprite invSprite();
	
	public void beUsed(Unit unit);
	
	public boolean isKey();
}
