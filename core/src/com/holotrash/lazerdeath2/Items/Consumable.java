package com.holotrash.lazerdeath2.Items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Units.Unit;

public class Consumable implements Item{

	private ArrayList<ConsumableEffect> effects;
	private String name;
	private String description;
	private Sprite tileSprite;
	private Sprite invSprite;
	
	public Consumable(String name, 
			String description, 
			Sprite tileSprite, 
			Sprite invSprite, 
			ArrayList<ConsumableEffect> effects){
		this.name = name;
		this.description = description;
		this.tileSprite = tileSprite;
		this.invSprite = invSprite;		
		this.effects = effects;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Sprite tileSprite() {
		return this.tileSprite;
	}

	@Override
	public Sprite invSprite() {
		return this.invSprite;
	}

	@Override
	public void beUsed(Unit unit) {
		unit.addEffects(this.effects);
	}

	public ArrayList<ConsumableEffect> effects() {
		return this.effects;
	}

	@Override
	public boolean isKey() {
		return false;
	}
	
}
