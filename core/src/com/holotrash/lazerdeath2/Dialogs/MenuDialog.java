package com.holotrash.lazerdeath2.Dialogs;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.lazerdeath2;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.LazerMath.Region;
import com.holotrash.lazerdeath2.Units.Dude;

public class MenuDialog {
 
	public static final int NULL_INDEX = -1;
	
	private Sprite background;
	private Sprite buttons;
	private Sprite tabArrows;
	
	private Sprite cursor;
	private int cursorIndex = MenuDialog.NULL_INDEX;
	private Coord cursorPosition;
	private ArrayList<Coord> iCursorPositionList;
	private ArrayList<Coord> useItemCursorPositionList;
	
	private ArrayList<MenuComponent> menuComponents;
	private ArrayList<MenuLabel> menuLabels;
	
	private Sprite inventoryBackground;
	private Sprite inventoryButtons;
	private Sprite groupBackground;
	private Sprite groupButtons;
	private Sprite useItemBackground;
	private Sprite useItemButtons;
	private Sprite blank;
	
	private int itemIndex;
	
	private lazerdeath2 game;
	private GameMaster gm;
	
	private MenuTab tab;
	private boolean enabled;
	
	private int groupIndex;
	private Dude groupCurrentDude;
	
	public MenuDialog(lazerdeath2 game){
		this.itemIndex = MenuDialog.NULL_INDEX;
		this.menuComponents = new ArrayList<MenuComponent>();
		this.menuLabels = new ArrayList<MenuLabel>();
		this.inventoryBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_bg.png")));
		this.inventoryButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_buttons.png")));
		this.cursor = new Sprite(new Texture(Gdx.files.internal("gfx/menu_inventory_cursor.png")));
		this.groupBackground = new Sprite(new Texture(Gdx.files.internal("gfx/menu_group_bg.png")));
		this.groupButtons = new Sprite(new Texture(Gdx.files.internal("gfx/menu_group_buttons.png")));
		this.useItemBackground = new Sprite(new Texture(Gdx.files.internal("gfx/use_item_dialog_bg.png")));
		this.useItemButtons = new Sprite(new Texture(Gdx.files.internal("gfx/use_item_dialog_btn.png")));
		this.tabArrows = new Sprite(new Texture(Gdx.files.internal("gfx/menu_tab_selector.png")));
		this.blank = new Sprite((new Texture(Gdx.files.internal("gfx/blank.png"))));
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
			
		}
		
		padX = 0;
		padY = 0;
		lastY = 0;
		this.useItemCursorPositionList = new ArrayList<Coord>();
		for (int y=0;y<2;y++){
			for(int x=0;x<3;x++){
				if(x == 0){
					padX=0;
				} else {
					padX=padX+80;
				}
				if(y != lastY){
					padY=padY+30;
					lastY = y;
				}
				this.useItemCursorPositionList.add(new Coord(
						512+((x%5)*128)+padX,
						445+((y%3)*128)+padY));
				
				
			}
			
		}
		
		this.cursorPosition = GameMaster.nullCoord;
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
		Sprite result;
		if (tab == MenuTab.USE_ITEM){
			result = blank;
		} else {
			result = this.tabArrows;
		}
		return result;
	}
	
	public ArrayList<MenuComponent> menuComponents(){
		return this.menuComponents;
	}
	
	public ArrayList<MenuLabel> menuLabels(){
		return this.menuLabels;
	}
	public void tabLeft(){
		if (this.tab == MenuTab.INVENTORY){
			this.setTab(MenuTab.GROUP);
		} else if (this.tab == MenuTab.GROUP){
			this.setTab(MenuTab.INVENTORY);
		}
	}
	
	public void tabRight(){
		if (this.tab == MenuTab.INVENTORY){
			this.setTab(MenuTab.GROUP);
		} else if (this.tab == MenuTab.GROUP){
			this.setTab(MenuTab.INVENTORY);
		}
	}
	
	public boolean enabled(){
		return this.enabled;
	}
	public void enable(){
		this.enabled = true;
		this.setTab(MenuTab.INVENTORY);
	}
	public void disable(){
		this.enabled = false;
	}

	public void buttonOnePressed() {
		// TODO: finish the tabs
		if (this.tab == MenuTab.INVENTORY){
			// BUTTON1 = USE
			if (this.cursorIndex != MenuDialog.NULL_INDEX){
				this.itemIndex = this.cursorIndex;
				this.setTab(MenuTab.USE_ITEM);
			}	
		} else if (this.tab == MenuTab.USE_ITEM){
			//BUTTON1==USE ON UNIT
			if(this.cursorIndex != MenuDialog.NULL_INDEX){
				System.out.println("using item: " + gm.inventory.get(itemIndex).name());
				gm.dudes().get(cursorIndex).useItem(gm.inventory.get(itemIndex));
				gm.inventory.remove(itemIndex);
				this.setTab(MenuTab.INVENTORY);
			}
		}
	}
	public void buttonTwoPressed() {
		if (this.tab == MenuTab.USE_ITEM){
			//BUTTON2 == BACK
			this.setTab(MenuTab.INVENTORY);
		} else if (this.tab == MenuTab.INVENTORY){
			//Button2 == discard
			if(this.cursorIndex != MenuDialog.NULL_INDEX){
				System.out.println("discarding item: " + gm.inventory.get(cursorIndex).name());
				gm.inventory.remove(cursorIndex);
				this.refreshInventory();
			}
		}
	}
	public void buttonThreePressed() {
		// TODO: finish the tabs
		if (this.tab == MenuTab.INVENTORY){
			// BUTTON3 = EXIT
			this.disable();
		} else if (this.tab == MenuTab.GROUP){
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
				if (component == 0){
					this.tabLeft();
					System.out.println("menu tab left");
				} else if (component == 1){
					System.out.println("menu tab right");
					this.tabRight();
				} else {
					System.out.println("Clicked on inventory item " + (component-2));
					this.setinventoryCursor(component-2);
				}
			} else if (this.tab == MenuTab.GROUP){
				if (component == 0){
					this.tabLeft();
					System.out.println("menu tab left");
				} else if (component == 1){
					System.out.println("menu tab right");
					this.tabRight();
				}
			} else if (this.tab == MenuTab.USE_ITEM){
				System.out.println("Clicked on unit " + component + "in use item menu dialog");
				this.setUseItemCursor(component);
			}
		}
	}
	
	private void setUseItemCursor(int positionIndex) {
		if (positionIndex != MenuDialog.NULL_INDEX){
			this.cursorPosition = this.useItemCursorPositionList.get(positionIndex);
		}
		this.refreshUseItem();
	}

	private void setTab(MenuTab tab){
		this.tab = tab;
		if (tab == MenuTab.INVENTORY){
			this.cursorPosition = GameMaster.nullCoord;
			this.buttons = inventoryButtons;
			this.background = this.inventoryBackground;
			// add inventory items
			this.refreshInventory();
		} else if (tab == MenuTab.USE_ITEM) {
			this.cursorPosition = GameMaster.nullCoord;
			this.buttons = this.useItemButtons;
			this.background = this.useItemBackground;
			// add units
			this.refreshUseItem();
		} else if (tab == MenuTab.GROUP) {
			this.cursorPosition = GameMaster.nullCoord;
			this.background = this.groupBackground;
			this.buttons = this.groupButtons;
			this.refreshGroupTab();
		}
	}
	
	private void refreshGroupTab() {
		this.menuComponents = new ArrayList<MenuComponent>();
		this.menuLabels = new ArrayList<MenuLabel>();
		
		this.enableMenuTabButtons();
		this.groupCurrentDude = gm.dudes().get(this.groupIndex);
		this.menuComponents.add(new MenuComponent("GLAMOURSHOT",
											new Coord(400,350),
											groupCurrentDude.glamourShot(),
											null,
											false));
		this.menuComponents.add(new MenuComponent("SPRITE",
				new Coord(470,220),
				groupCurrentDude.sprite(),
				null,
				false));
		
		this.menuLabels.add(new MenuLabel("NAME", 
									new Coord(665, 600), 
									"    NAME: " + groupCurrentDude.name()));
		this.menuLabels.add(new MenuLabel("HP", 
				new Coord(665, 550), 
				"      HP: " + groupCurrentDude.hp()));
		this.menuLabels.add(new MenuLabel("PP", 
				new Coord(665, 500), 
				"      PP: " + groupCurrentDude.pp()));
		this.menuLabels.add(new MenuLabel("SPEED", 
				new Coord(665, 450), 
				"   SPEED: " + groupCurrentDude.speed()));
		this.menuLabels.add(new MenuLabel("DODGE", 
				new Coord(665, 400), 
				"   DODGE: " + groupCurrentDude.dodge()));
		this.menuLabels.add(new MenuLabel("ACCURACY", 
				new Coord(665, 350), 
				"ACCURACY: " + groupCurrentDude.accuracy()));
		this.menuLabels.add(new MenuLabel("WEAPON", 
				new Coord(665, 300), 
				"  WEAPON: " + groupCurrentDude.weapon().toString()));
		
	}

	private void refreshUseItem(){
		this.menuComponents = new ArrayList<MenuComponent>();
		this.menuLabels = new ArrayList<MenuLabel>();
		
		Coord c;
		Dude dude;
		for(int i=0;i<6;i++){
			if(i<gm.dudes().size()){
				c = this.useItemCursorPositionList.get(i);
				dude = gm.dudes().get(i);
				this.menuComponents.add(new MenuComponent(
											dude.name(),
											c,
											dude.sprite(),
											new Region(c.x(), 
													c.x() + 128,
													c.y() - 325,
													c.y() - 197),
											true));
			}
		}
		
		this.menuComponents.add(new MenuComponent(
				"CURSOR",
				this.cursorPosition,
				this.cursor,
				null,
				false));
	}
	
	private void refreshInventory(){
		this.menuComponents = new ArrayList<MenuComponent>();
		this.menuLabels = new ArrayList<MenuLabel>();
		
		this.enableMenuTabButtons();
		
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
									this.cursorPosition,
									this.cursor,
									null,
									false));
	}
	
	private void setinventoryCursor(int positionIndex){
		this.cursorIndex = positionIndex;
		if (positionIndex != MenuDialog.NULL_INDEX){
			this.cursorPosition = this.iCursorPositionList.get(positionIndex);
		}
		this.refreshInventory();
	}
	
	private void enableMenuTabButtons(){
		this.menuComponents.add(new MenuComponent(
				"TAB_LEFT",
				new Coord(880, 140),
				this.blank,
				new Region(880, 910, 100, 140),
				true));
		this.menuComponents.add(new MenuComponent(
				"TAB_RIGHT",
				new Coord(1075, 140),
				this.blank,
				new Region(1075, 1105, 100, 140),
				true));
	}
}
