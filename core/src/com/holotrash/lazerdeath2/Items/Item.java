package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Item {

	public String name();
	
	public String description();
	
	public Sprite tileSprite();
	
	public Sprite invSprite();
}
