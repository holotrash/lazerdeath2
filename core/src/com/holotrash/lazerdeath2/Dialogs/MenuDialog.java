package com.holotrash.lazerdeath2.Dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.lazerdeath2;
import com.holotrash.lazerdeath2.Items.Item;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.LazerMath.Region;

public class MenuDialog {
 
	public static final int NULL_INDEX = -1;
	
	private Sprite background;
	private Sprite buttons;
	private Sprite tabArrows;
	
	private Sprite inventoryCursor;
	private int inventoryCursorIndex = MenuDialog.NULL_INDEX;
	private Coord inventoryCursorPosition;
	private ArrayList<Coord> iCursorPositionList;
	
	private ArrayList<MenuComponent> menuComponents; 
	
	private Sprite inventoryBackground;
	private Sprite inventoryButtons;
	private Sprite groupBackground;
	private Sprite groupButtons;
	
	private lazerdeath2 game;
	private GameMaster gm;
	
	private MenuTab tab;
	private boolean enabled;
	private boolean btn1enabled;
	private boolean btn2enabled;
	private boolean btn3enabled;
	
	private int groupIndex;
	
	public MenuDialog(lazerdeath2 game){
		this.menuComponents = new ArrayList<MenuComponent>();
		this.inventoryBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_bg.png")));
		this.inventoryButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_buttons.png")));
		this.inventoryCursor = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_cursor.png")));
		this.groupBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_group_bg.png")));
		// TODO: make a real group buttons graphic
		this.groupButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_buttons.png")));
		this.tabArrows = new Sprite(new Texture(Gdx.files.internal("gfx/menu_tab_selector.png")));
		
		this.iCursorPositionList = new ArrayList<Coord>();
		int padX=0;
		int padY=0;
		int lastY = 0;
		for (int y=0;y<3;y++){
			for(int x=0;x<5;x++){
				if(x == 0){
					padX=0;
				} else {
					padX=padX+16;
				}
				if(y != lastY){
					padY=padY +16;
					lastY = y;
				}
				this.iCursorPositionList.add(new Coord(
						432+((x%5)*128)+padX,
						483+((y%3)*128)+padY));
				
				
			}
			
			this.inventoryCursorPosition = GameMaster.nullCoord;
			
		}
		
		this.game = game;
		this.gm = game.gm;
		this.enabled = false;

		this.groupIndex = 0;
		
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
	
	public ArrayList<MenuComponent> menuComponents(){
		return this.menuComponents;
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
		this.setTabInventory();
	}
	public void disable(){
		this.enabled = false;
	}

	public void buttonOnePressed() {
		// TODO: finish the tabs
		if (this.tab == MenuTab.INVENTORY){
		// BUTTON1 = USE
			if (this.inventoryCursorIndex != MenuDialog.NULL_INDEX){
				gm.useItemDialog(inventoryCursorIndex);
			}	
		}
	}
	public void buttonTwoPressed() {
		// TODO Auto-generated method stub
		
	}
	public void buttonThreePressed() {
		// TODO: finish the tabs
		if (this.tab == MenuTab.INVENTORY){
			// BUTTON3 = EXIT
			this.disable();
		}
		
	}
	
	public void attemptComponentRegionHit(Coord c){
		int found = -1;
		
		for(int i=0;i<this.menuComponents.size();i++){
			if(this.menuComponents.get(i).coordHits(c)){
				found = i;
			}
		}
		
		this.componentAction(found);
		
	}
	
	private void componentAction(int component){
		if (component != -1){
			if (this.tab == MenuTab.INVENTORY){
				System.out.println("Clicked on Menu Component " + component);
				this.inventoryCursorIndex = component;
				this.setinventoryCursor(component);
			} else if (this.tab == MenuTab.GROUP){
				//TODO group actions
			}
		}
	}
	
	private void setTabInventory(){
		this.tab = MenuTab.INVENTORY;
		
		this.buttons = inventoryButtons;
		this.background = this.inventoryBackground;
		
		this.btn1enabled = true;
		this.btn2enabled = true;
		this.btn3enabled = true;
		// add inventory items
		this.refreshInventory();
	}
	
	private void refreshInventory(){
		this.menuComponents = new ArrayList<MenuComponent>();
		Coord c;
		for(int i=0;i<15;i++){//Item item : gm.inventory){
			if(i<gm.inventory.size()){
				c = this.iCursorPositionList.get(i);
				this.menuComponents.add(new MenuComponent(
											"ITEM",
											c,
											gm.inventory.get(i).invSprite(), 
											new Region(c.x(), 
													c.x() + 128,
													c.y() - 325,
													c.y() - 197), 
											true));
			}
		}
		
		this.menuComponents.add(new MenuComponent(
									"CURSOR",
									this.inventoryCursorPosition,
									this.inventoryCursor,
									null,
									false));
	}
	
	private void setinventoryCursor(int positionIndex){
		if (positionIndex != MenuDialog.NULL_INDEX){
			this.inventoryCursorPosition = this.iCursorPositionList.get(positionIndex);
		}
		this.refreshInventory();
	}
	
}
