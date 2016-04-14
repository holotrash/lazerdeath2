package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.ElectronicLock;

public class KeyCard implements Item{

	private int magStrip;
	private String name;
	private String description;
	private Sprite invSprite;
	private Sprite tileSprite;
	
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Sprite tileSprite() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Sprite invSprite() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean unlocks(ElectronicLock l){
		return this.magStrip == l.code();
	}
}