package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Lock;

public class Key implements Item{

	private int tumbler;
	private String name;
	private String description;
	private Sprite tileSprite;
	private Sprite invSprite;
	
	public Key(String name, int tumbler, String description, Sprite tileSprite, Sprite invSprite){
		this.name = name;
		this.description = description;
		this.tileSprite = tileSprite;
		this.invSprite = invSprite;
		this.tumbler = tumbler;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String description() {
		return this.description;
	}

	@Override
	public Sprite tileSprite() {
		return this.tileSprite;
	}

	@Override
	public Sprite invSprite() {
		return this.invSprite;
	}
	
	public boolean unlocks(Lock l){
		return this.tumbler == l.tumbler();
	}
}