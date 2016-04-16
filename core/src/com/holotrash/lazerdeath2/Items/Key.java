package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.Maps.Lock;
import com.holotrash.lazerdeath2.Units.Unit;

public class Key implements Item{

	private int tumbler;
	private String name;
	private String description;
	private Sprite tileSprite;
	private Sprite invSprite;
	private GameMaster gm;
	
	public Key(GameMaster gm, int tumbler, String name, String description, Sprite tileSprite, Sprite invSprite){
		this.gm = gm;
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

	@Override
	public void beUsed(Unit unit) {
		// TODO Auto-generated method stub
		
	}
}
