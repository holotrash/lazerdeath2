package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.Units.Unit;

public class NoteItem implements Item{
	
	private String name;
	private String description;
	private Sprite tileSprite;
	private Sprite invSprite;
	private GameMaster gm;
	private int noteIndex;
	
	public NoteItem(GameMaster gm,
				String name, 
				String desc, 
				Sprite tileSprite, 
				Sprite invSprite,
				int noteIndex){
		this.gm = gm;
		this.name = name;
		this.description = desc;
		this.tileSprite = tileSprite;
		this.invSprite = invSprite;
		this.noteIndex = noteIndex;
	}
	
	public String name(){
		return this.name;
	}
	
	public String description(){
		return this.description;
	}
	
	public Sprite tileSprite(){
		return this.tileSprite;
	}
	
	public Sprite invSprite(){
		return this.invSprite;
	}
	
	public void beUsed(Unit unit){
		// do nothing
	}
	
	public int noteIndex(){
		return noteIndex;
	}
}
