package com.holotrash.lazerdeath2;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MenuDialog {

	private Sprite background;
	private Sprite buttons;
	private Sprite tabArrows;
	private HashMap<Coord, Sprite> menuSprites;
	
	private Sprite inventoryCursor;
	private Coord inventoryCursorrPosition;
	private ArrayList<Coord> iCursorPositionList; 
	private Sprite inventoryBackground;
	private Sprite inventoryButtons;
	private Sprite groupBackground;
	private Sprite groupButtons;
	
	private lazerdeath2 game;
	
	private MenuTab tab;
	private boolean enabled;
	
	private int iIndex;
	private int gIndex;
	
	public MenuDialog(lazerdeath2 game){
		this.inventoryBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_bg.png")));
		this.inventoryButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_buttons.png")));
		this.inventoryCursor = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_cursor.png")));
		this.groupBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_group_bg.png")));
		// TODO: make a real group buttons graphic
		this.groupButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_buttons.png")));
		this.tabArrows = new Sprite(new Texture(Gdx.files.internal("gfx/menu_tab_selector.png")));
		// default is inventory for now
		this.tab = MenuTab.INVENTORY;
		this.buttons = inventoryButtons;
		this.background = this.inventoryBackground;
		
		this.game = game;
		this.enabled = false;
		
		this.iIndex = 0;
		this.gIndex = 0;
		
	}
	
	public MenuTab currentTab(){
		return tab;
	}
	
	public Sprite background(){
		return background;
	}
	
	public Sprite buttons(){
		return this.buttons;
	}
	
	public Sprite tabArrows(){
		return this.tabArrows;
	}
	
	public HashMap<Coord, Sprite> menuSprites(){
		return this.menuSprites;
	}
	
	public void tabLeft(){
		//TODO: advance to next menu
	}
	
	public void tabRight(){
		//TODO: revert to previous menu
	}
	
	public boolean enabled(){
		return this.enabled;
	}
	public void enable(){
		this.enabled = true;
	}
	public void disable(){
		this.enabled = false;
	}
}
