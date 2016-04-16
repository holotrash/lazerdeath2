package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.ElectronicLock;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.Unit;

public class KeyCard implements Item{

	private int magStrip;
	private String name;
	private String description;
	private Sprite invSprite;
	private Sprite tileSprite;
	private GameMaster gm;
	
	public KeyCard(GameMaster gm, String name, String description, int code, Sprite invSprite, Sprite tileSprite){
		this.gm = gm;
		this.name = name;
		this.description = description;
		this.magStrip = code;
		this.invSprite = invSprite;
		this.tileSprite = tileSprite;
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
	
	public boolean unlocks(ElectronicLock l){
		return this.magStrip == l.code();
	}

	@Override
	public void beUsed(Unit unit) {
		// TODO Auto-generated method stub
		
	}
}