/**
 *  lazerdeath2.java
 *  -------
 *  This file is just a mess. It aims to be the game in some sense. 
 *  
 *  In future versions, it would be nice to abstract some of its 
 *  behaviors away from it into other classes to make this class 
 *  less cluttered and more focused on just executing the game
 *  loop in a clean and comprehensible way. For instance, there 
 *  really ought to be a separate InputProcessor. 
 *  
 *  Right now I just want to get this thing working in some form.
 *  --------------------------------------------------------------------  
 *  This file is part of the computer game Lazerdeath2 
 *  Copyright 2016, Robert Watson Craig III
 *
 *  Lazerdeath2 is free software published under the terms of the GNU
 *  General Public License version 3. You can redistribute it and/or 
 *  modify it under the terms of the GPL (version 3 or any later version).
 * 
 *  Lazerdeath2 is distributed in the hope that it will be entertaining,
 *  cool, and totally smooth for your mind to rock to, daddy, but WITHOUT 
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 *  FITNESS FOR A PARTICULAR PURPOSE; without even the suggestion of an
 *  implication that any of this code makes any sense whatsoever. It works
 *  on my computer and I don't think that's such a weird environment, but
 *  it might be. Or maybe it's your computer that's the weird one, did you
 *  ever think of that?  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lazerdeath2.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.holotrash.lazerdeath2;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.holotrash.lazerdeath2.Dialogs.BooleanDialogType;
import com.holotrash.lazerdeath2.Dialogs.DialogBox;
import com.holotrash.lazerdeath2.Dialogs.DialogInfo;
import com.holotrash.lazerdeath2.Dialogs.DialogType;
import com.holotrash.lazerdeath2.Dialogs.MenuComponent;
import com.holotrash.lazerdeath2.Dialogs.MenuDialog;
import com.holotrash.lazerdeath2.Dialogs.MenuLabel;
import com.holotrash.lazerdeath2.Dialogs.SimpleBooleanDialog;
import com.holotrash.lazerdeath2.Items.Item;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.Maps.HighlightTile;
import com.holotrash.lazerdeath2.Maps.InteractedTile;
import com.holotrash.lazerdeath2.Maps.Map;
import com.holotrash.lazerdeath2.Units.Dude;
import com.holotrash.lazerdeath2.Units.Enemy;
import com.holotrash.lazerdeath2.Units.Unit;
import com.holotrash.lazerdeath2.Units.Weapon;
import com.holotrash.lazerdeath2.Units.WeaponType;

public class lazerdeath2 extends ApplicationAdapter implements InputProcessor {
    
	public static final int OVERLAY_FADE_TIME = 90;
	public static final int SCROLLING_MULTIPLIER = 500;
	
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    
    ArrayList<Dude> dudes;
    ArrayList<Enemy> enemies;
    
    HashMap<Coord, HighlightTile> highlightTiles;
    ArrayList<InteractedTile> interactedTiles;
    boolean highlightOn;
    Color hlColor1;
    Color hlColor2;
    DialogBox dialogBox;
    BitmapFont dialogFont;
    BitmapFont uiFont;
    GlyphLayout layout;
    MenuDialog menuDialog;
    SimpleBooleanDialog ynDialog;
    
    String temp;
    Vector3 tempV3;
    Vector3 screenOrigin;
  
    Map mapData;
    SpriteBatch spriteBatch;
    SpriteBatch highlightBatch;
    Coord lastClickedCell;
    Unit selectedUnit;
    
    public GameMaster gm;

    Sprite dudesTurnSprite;
    Sprite enemiesTurnSprite;
    Sprite leftStatusBox;
    Sprite rightStatusBox;
    Sprite rightStatusBoxFade;
    Sprite glamourShot;
    Sprite defaultGlamourShot;
    Sprite endTurnButton;
    Sprite attackButton;
    Sprite exitGameButton;
    Sprite menuButton;
    Item tempItem;
    
    ArrayList<String> currentUnitStats;
    ArrayDeque<String> uiConsole;
    Iterator<String> consoleIterator;
    
    int overlayFadeCounter;
    
    boolean scrollingUp = false;
    boolean scrollingDown = false;
    boolean scrollingLeft = false;
    boolean scrollingRight = false;
    
    boolean playerAttacking = false;
    
    float delta;
        
    @Override
    public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        camera.update();
        mapData = null;
        try {
			mapData = new Map(0, this);
		} catch (IOException e) {
			System.out.println("loading map data failed for level 0");
			e.printStackTrace();
		}
        gm = new GameMaster(this, mapData);
        initializeDudes();
        initializeEnemies();
        
        lastClickedCell = new Coord(-1,-1);
        highlightTiles = new HashMap<Coord, HighlightTile>(); 
        highlightOn = false;
        interactedTiles = new ArrayList<InteractedTile>();
        
        dudesTurnSprite = new Sprite(new Texture(Gdx.files.internal("gfx/dudes_turn.png")));
        enemiesTurnSprite = new Sprite(new Texture(Gdx.files.internal("gfx/enemy_turn.png")));
        this.leftStatusBox = new Sprite(new Texture(Gdx.files.internal("gfx/left_status_box.png")));
        this.rightStatusBox = new Sprite(new Texture(Gdx.files.internal("gfx/right_status_box.png")));
        this.rightStatusBoxFade = new Sprite(new Texture(Gdx.files.internal("gfx/right_status_box_fade_out.png")));
        this.defaultGlamourShot = new Sprite(new Texture(Gdx.files.internal("gfx/default_glamourshot.png")));
        this.glamourShot = defaultGlamourShot;
        this.endTurnButton = new Sprite(new Texture(Gdx.files.internal("gfx/end_turn.png")));
        this.attackButton = new Sprite(new Texture(Gdx.files.internal("gfx/attack_button.png")));
        this.exitGameButton = new Sprite(new Texture(Gdx.files.internal("gfx/exit_game.png")));
        this.menuButton = new Sprite(new Texture(Gdx.files.internal("gfx/menu_button.png")));
        
        tiledMap = new TmxMapLoader().load("gfx/" + mapData.tmxFileName + ".tmx");
        spriteBatch = new SpriteBatch();
        highlightBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, spriteBatch);       
        Gdx.input.setInputProcessor(this);
        menuDialog = new MenuDialog(this);
        dialogBox = new DialogBox();
        dialogFont = new BitmapFont(Gdx.files.internal("fonts/bank.fnt"));
        uiFont = new BitmapFont(Gdx.files.internal("fonts/hack.fnt"));
        layout = new GlyphLayout();
        this.ynDialog = new SimpleBooleanDialog("Do you wanna fuck?", "Jeah!", "Nah...");
        gm.showLevelStartDialog();
        selectedUnit = null;
        
        currentUnitStats = new ArrayList<String>();
        currentUnitStats.add("SELECT A UNIT TO");
        currentUnitStats.add("BEGIN PERPETUATING");
        currentUnitStats.add("THE CYCLE OF");
        currentUnitStats.add("VIOLENCE");
        currentUnitStats.add("...");
        
        uiConsole = new ArrayDeque<String>();
        uiConsole.push("Battle start...");
        
        this.screenOrigin = new Vector3(0,768,0);
        this.screenOrigin = camera.unproject(this.screenOrigin);
        
    }

    @Override
    public void render () {
    	delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (scrollingUp && camera.position.y < mapData.scrollUpMax){
        	camera.translate(0,SCROLLING_MULTIPLIER*delta);
        }
        if (scrollingDown && camera.position.y > mapData.scrollDownMax){
        	camera.translate(0, (0-SCROLLING_MULTIPLIER)*delta);
        }
        if (scrollingLeft && camera.position.x > mapData.scrollLeftMax){
        	camera.translate((0-SCROLLING_MULTIPLIER)*delta, 0);
        }
        if (scrollingRight && camera.position.x < mapData.scrollRightMax){
        	camera.translate(SCROLLING_MULTIPLIER*delta, 0);
        }
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        tiledMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        
        
        for (Enemy enemy : enemies){
        	if (enemy.position().equals(lastClickedCell)){
        		selectedUnit = enemy;
        		System.out.println("selectedUnit equals enemy: " + enemy.name());
        	}
        	
        }
        
        for (Dude dude : dudes){
        	if (dude.position().equals(lastClickedCell)){
				selectedUnit = dude;
        		System.out.println("selectedUnit equals dude: " + dude.name());
        	}
        }
        
        //change states of sprites based on dude/enemy flags here
        for (int i=0;i<dudes.size();i++){
        	if(!this.playerAttacking && dudes.get(i).position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(dudes.get(i));
        	}
        }
        for (int i=0;i<enemies.size();i++){
        	if(!this.playerAttacking && enemies.get(i).position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(enemies.get(i));
        	}
        }
        
        spriteBatch.begin();
   
        //draw interacted tiles
        for (InteractedTile it : interactedTiles){
    		spriteBatch.draw(it.sprite, 128*it.position.x(), 128*it.position.y());
    	}
        //draw items

        for (Coord coord : gm.itemWrangler.items.keySet()){
        	tempItem = gm.itemWrangler.items.get(coord);
        	spriteBatch.draw(tempItem.tileSprite(),
        			128*coord.x(),
        			128*coord.y());
        }
        
        //draw dudes and enemies
        for (int i=0;i<dudes.size();i++){
        	spriteBatch.draw(dudes.get(i).sprite(),
        			         128*(dudes.get(i).position().x()), 
        			         128*(dudes.get(i).position().y()));
        
        	
        }
        for (int i=0;i<enemies.size();i++){
        	spriteBatch.draw(enemies.get(i).sprite(),
        					 128*(enemies.get(i).position().x()),
        				     128*(enemies.get(i).position().y()));
        }
        if (gm.dudesTurn()){
        	//spriteBatch.setColor(hlColor);
        	for (Coord c : highlightTiles.keySet()){
        		spriteBatch.setColor(highlightTiles.get(c).color);
        		spriteBatch.draw(highlightTiles.get(c).sprite, 128*highlightTiles.get(c).position.x(), 128*highlightTiles.get(c).position.y());
        	}
        	spriteBatch.setColor(Color.WHITE);
        }
        
        // get a screen-relative point of reference for drawing ui elements
        tempV3 = new Vector3(0,768,0);
        tempV3 = camera.unproject(tempV3);
        
        // draw left side ui buttons
        spriteBatch.draw(this.endTurnButton, tempV3.x + 10, tempV3.y + 625);
        spriteBatch.draw(this.attackButton, tempV3.x + 10, tempV3.y + 500);
        spriteBatch.draw(this.menuButton, tempV3.x + 10, tempV3.y + 375);
        spriteBatch.draw(this.exitGameButton, tempV3.x + 10, tempV3.y + 250);
        
        // draw left status dialog stuff
        if (this.selectedUnit != null){
        	this.glamourShot = selectedUnit.glamourShot();
        	this.currentUnitStats = selectedUnit.toStringz();
        	// change left status box text to relevant unit stats:
        	
        }
        
        spriteBatch.draw(this.leftStatusBox, tempV3.x, tempV3.y);
        spriteBatch.draw(this.rightStatusBox, tempV3.x + 768, tempV3.y - 64);
        spriteBatch.draw(this.glamourShot, tempV3.x, tempV3.y);
        uiFont.setColor(Color.BLACK);
        for (int i=0;i<5;i++){        
        	if (currentUnitStats.size() > i)
        		uiFont.draw(spriteBatch, 
        				currentUnitStats.get(i),
        				tempV3.x + 266, 
        				tempV3.y + 182 - (i*32));
        }
        
        //draw right side ui status console text
        consoleIterator = uiConsole.iterator();
        for (int i=0;i<5;i++){        
        	if (consoleIterator.hasNext())
        		uiFont.draw(spriteBatch, 
        				consoleIterator.next(),
        				tempV3.x + 832, 
        				tempV3.y + 32 + (i*32));
        }
        //draw right side ui status console fade out
        spriteBatch.draw(this.rightStatusBoxFade, tempV3.x + 768, tempV3.y - 64);
        
        if (dialogBox.enabled()){
        	// draw dialog box
        	tempV3 = new Vector3(286,700,0);
        	tempV3 = camera.unproject(tempV3);
        	spriteBatch.draw(dialogBox.background(), tempV3.x, tempV3.y);
        	tempV3 = new Vector3(384,635,0);
        	tempV3 = camera.unproject(tempV3);
        	spriteBatch.draw(dialogBox.button(), tempV3.x, tempV3.y);
        	tempV3 = new Vector3(725,635,0);
        	tempV3 = camera.unproject(tempV3);
        	spriteBatch.draw(dialogBox.button(), tempV3.x, tempV3.y);
        	// draw dialog text
        	int numLines = dialogBox.currentMessage().size();
        	if (numLines > 6)
        		numLines = 6;
        	tempV3 = new Vector3(384,150,0);
        	tempV3 = camera.unproject(tempV3);
        	for (int i=0;i<numLines;i++){
        		//dialogFont.draw(spriteBatch, dialogLines.get(i), 384, 625 - (i*50));
        		dialogFont.draw(spriteBatch, dialogBox.currentMessage().get(i), tempV3.x, tempV3.y -(i*50));
        	}
        	tempV3 = new Vector3(565,540,0);
        	tempV3 = camera.unproject(tempV3);
        	temp = dialogBox.btn1();
        	layout.setText(dialogFont, temp);
            dialogFont.draw(spriteBatch, temp, tempV3.x - layout.width, tempV3.y - layout.height);
        	tempV3 = new Vector3(965,540,0);
        	tempV3 = camera.unproject(tempV3);
            temp = dialogBox.btn2();
        	layout.setText(dialogFont, temp);
        	dialogFont.draw(spriteBatch, temp, tempV3.x - layout.width, tempV3.y - layout.height);
        } else if (this.menuDialog.enabled()){
        	// show menuDialog
            this.screenOrigin = new Vector3(0,768,0);
            this.screenOrigin = camera.unproject(this.screenOrigin);
        	spriteBatch.draw(menuDialog.background(), screenOrigin.x + 384, screenOrigin.y + 64);
        	spriteBatch.draw(menuDialog.buttons(), screenOrigin.x + 384, screenOrigin.y + 64);
        	spriteBatch.draw(menuDialog.tabArrows(), screenOrigin.x + 384, screenOrigin.y + 64);
        	// show menuDialog components
        	for(MenuComponent mc : menuDialog.menuComponents()){
        		if (!Coord.coordsEqual(mc.position, GameMaster.nullCoord)){
        			spriteBatch.draw(mc.sprite,
        				screenOrigin.x + mc.position.x(), 
        				screenOrigin.y + mc.position.y());
        		}
        	}
        	// show menu labels
        	for (MenuLabel ml : menuDialog.menuLabels()){
        		if (!Coord.coordsEqual(ml.position, GameMaster.nullCoord)){
        			dialogFont.draw(spriteBatch, 
        					ml.text, 
        					screenOrigin.x + ml.position.x(), 
        					screenOrigin.y + ml.position.y());
        		}
        	}
        		
        } else if (this.ynDialog.enabled()){
        	//show ynDialog
        	if(!ynDialog.resultRecorded()){
        		this.screenOrigin = new Vector3(0,768,0);
        		this.screenOrigin = camera.unproject(this.screenOrigin);
        		spriteBatch.draw(ynDialog.background(), 
            					screenOrigin.x + 500,
            					screenOrigin.y + 300);
        		spriteBatch.draw(ynDialog.buttons(),
            					screenOrigin.x + 500,
            					screenOrigin.y + 300);
        		dialogFont.draw(spriteBatch,
        						ynDialog.text(),
        						screenOrigin.x + 510,
        						screenOrigin.y + 525);
        	} else {
        		if (this.ynDialog.type() == BooleanDialogType.EXIT_GAME){
        			if(ynDialog.result()){       			
        				Gdx.app.exit();
        			} else {
        				ynDialog.disable();
        			}     	
        		}
        	}
        } else {
        
        	//Text overlay? dudes turn? enemies turn?
        	tempV3 = new Vector3(0,768,0);
        	tempV3 = camera.unproject(tempV3);
        	if (gm.dudesTurn() && this.overlayFadeCounter > 0){
        		spriteBatch.draw(this.dudesTurnSprite, tempV3.x + 350, tempV3.y + 275);
        		overlayFadeCounter--;
        	} else if (gm.enemiesTurn() && this.overlayFadeCounter > 0){
        		spriteBatch.draw(this.enemiesTurnSprite, tempV3.x + 350, tempV3.y + 275);
        		overlayFadeCounter--;
        	}
        	
        }
        spriteBatch.end();
        if (!dialogBox.enabled()){	
        	gm.clockTick();
        	gm.advanceGame();
        }
        
    } // end render()
    
    
    private void makeRangeHighlight(Unit unit) {
    	highlightTiles.clear();
    	HashSet<Coord> tileCoords;
    	
    	for (int i=0;i<unit.ap()+1;i++){
    	
    		tileCoords = gm.unitMoveCoords(unit, i);
    		if (unit.isDude()){
    			hlColor1 = new Color(0.0f,0.0f,1.0f,0.5f/i);
    			//hlColor2 = new Color(0.0f,0.0f,1.0f,0.2f);
    		} else {
    			hlColor1 = new Color(1.0f,0.0f,0.0f,0.5f/i);
    			//hlColor2 = new Color(1.0f,0.0f,0.0f,0.2f);
    		}
    	
    		for(Coord coord : tileCoords){
    			if (!highlightTiles.containsKey(coord))
    				highlightTiles.put(coord, new HighlightTile(coord, hlColor1));
    		}
    	}
	}

    private void makeAttackHighlight(Unit unit){
    	this.highlightTiles.clear();
    	this.playerAttacking = true;
    	HashSet<Unit> tileUnits = gm.tileMath.unitsWithinAttackRange(unit);
    	hlColor1 = new Color(0.0f,0.0f,1.0f,0.5f);
    	hlColor2 = new Color(1.0f,0.0f,0.0f,0.5f);
    	for (Unit u : tileUnits){
    		if (u.isDude()){
    			highlightTiles.put(u.position(), new HighlightTile(u.position(), hlColor1));
    		} else {
    			highlightTiles.put(u.position(), new HighlightTile(u.position(), hlColor2));
    		}
    	}
    }
    
	@Override
    public boolean keyDown(int keycode) {
    
		// now scrolling is done with the mouse
		
		/**
		if(keycode == Input.Keys.LEFT && camera.position.x > 767){
            camera.translate(-128,0);
        }
        if(keycode == Input.Keys.RIGHT && camera.position.x < 769){
            camera.translate(128,0);
        }	
        if(keycode == Input.Keys.UP && camera.position.y < 1025){
            camera.translate(0,128);
        }
        if(keycode == Input.Keys.DOWN && camera.position.y > 511){
            camera.translate(0,-128);
        }
        **/
    	return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

	@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	if (dialogBox.enabled()){
    		if (screenX > 405 && screenX < 620 && screenY > 535 && screenY < 610)
    			this.executeDialogOption1();
    		else if (screenX > 745 && screenX < 960 && screenY > 540 && screenY < 620)
    			this.executeDialogOption2();
    		
    	} else if (ynDialog.enabled()) {
    		System.out.println("ynDialog touchdown at: (" + screenX + "," + screenY + ")");
    		if (ynDialog.button1Hit(screenX, screenY)){
    			System.out.println("ynDialog button 1 pressed");
    			ynDialog.recordResult(true);
    		} else if (ynDialog.button2Hit(screenX, screenY)){
    			System.out.println("ynDialog button 2 pressed");
    			ynDialog.recordResult(false);
    		}
    		
    		
    	} else if (menuDialog.enabled()){
    	
    		 // detect touchDown on menuDialog buttons
    		System.out.println("Menu Dialog Touchdown at: (" + screenX + "," + screenY + ")");
    		if (screenX > 435
    			&& screenX < 635
    			&& screenY > 595
    			&& screenY < 670){
    			
    			System.out.println("Menu button 1 selected");
    			menuDialog.buttonOnePressed();
    		} else if (screenX > 680
        			&& screenX < 880
        			&& screenY > 595
        			&& screenY < 670){
        			
        			System.out.println("Menu button 2 selected");
        			menuDialog.buttonTwoPressed();
    		} else if (screenX > 930
        			&& screenX < 1130
        			&& screenY > 595
        			&& screenY < 670){
        			
        			System.out.println("Menu button 3 selected");
        			menuDialog.buttonThreePressed();
        	}
    		// detect touchDown on menu components
    		menuDialog.attemptComponentRegionHit(new Coord(screenX, screenY));
    	} else if (screenX > 15 // detect touchDown on left side ui buttons
    				&& screenX < 135
    				&& screenY > 25
    				&& screenY < 125){
    		gm.takeEnemiesTurn();
    		System.out.println("Player ends turn.");
    		System.out.println("last clicked pixel: (" + screenX + "," + screenY + ")");
    	} else if (screenX > 15 
				&& screenX < 135
				&& screenY > 150
				&& screenY < 250){
	
    		System.out.println("Attack!");
    		System.out.println("last clicked pixel: (" + screenX + "," + screenY + ")");
    		if (selectedUnit == null || !selectedUnit.isDude()){
    			uiConsole.push("Select a dude.");
    		} else {
    			this.makeAttackHighlight(selectedUnit);
    		}
    	} else if (screenX > 15 
				&& screenX < 135
				&& screenY > 272
				&& screenY < 375){

    		System.out.println("Menu.");
    		this.menuDialog.enable();
    		System.out.println("last clicked pixel: (" + screenX + "," + screenY + ")");
    	} else if (screenX > 15 
				&& screenX < 135
				&& screenY > 395
				&& screenY < 500){

    		System.out.println("Exit.");
    		System.out.println("last clicked pixel: (" + screenX + "," + screenY + ")");
    		
    		this.ynDialog.setText("Are you sure? Unsaved progress will be lost.");
    		this.ynDialog.setChoice1("YES");
    		this.ynDialog.setChoice2("NO");
    		this.ynDialog.setType(BooleanDialogType.EXIT_GAME);
    		this.ynDialog.resetResult();
    		this.ynDialog.enable();
    		
    	}else if (this.playerAttacking){
    		lastClickedCell = pixelToCell(vector3ToCoord(camera.unproject(new Vector3(screenX, screenY, 0))));
    		boolean foundAttack = false;
    		for (Dude d : dudes){
    			if (!foundAttack && Coord.coordsEqual(d.position(), lastClickedCell)){
    				selectedUnit.attack(d);
    				this.playerAttacking = false;
    				foundAttack = true;
    				lastClickedCell = selectedUnit.position();    				
    			}
    		}
    		for (Enemy e : enemies){
    			if (!foundAttack && Coord.coordsEqual(e.position(), lastClickedCell)){
    				selectedUnit.attack(e);
    				this.playerAttacking = false;
    				foundAttack = true;
    				lastClickedCell = selectedUnit.position();
    			}
    		}
    		
    	} else {
    		
    		System.out.println("last clicked pixel: (" + screenX + "," + screenY + ")");
    		lastClickedCell = pixelToCell(vector3ToCoord(camera.unproject(new Vector3(screenX, screenY, 0))));
    		System.out.println("last clicked cell: (" + lastClickedCell.x() + "," + lastClickedCell.y() + ")");
		
    		//move necessary dudes
    		if (gm.dudesMoving()){ // dudes are responsible for limiting their own movement and attack
    			//if lastClickedCell contains a unit, attempt to attack.
    			// otherwise attempt to move
    			Dude dudeMoving = dudes.get(gm.dudeMoving());
    			//boolean attack = false;
    			//for (Dude dude : dudes){
    			//	if (dude.position().equals(lastClickedCell)){
    			//		if (!(dudeMoving == dude)){
    			//			dudeMoving.attack(dude);
    			//			System.out.println("Attacking dude: " + dude.name());
    			//			attack = true;
    			//		}
    			//	}
    			//}
    			//for (Enemy enemy : enemies){
    			//	if (enemy.position().equals(lastClickedCell)){
    			//		dudeMoving.attack(enemy);
    			//		System.out.println("Attacking enemy: " + enemy.name());
    			//		attack = true;
    			//	}
    			//}
    			//if (!attack)
    			dudes.get(gm.dudeMoving()).move(lastClickedCell);
    			this.highlightTiles.clear();
    		   		// check for interactable tiles
    			if (gm.tileMath.isInteractable(lastClickedCell)){
    				if (gm.tileMath.isDoor(lastClickedCell) 
    						&& dudeMoving.tileInInteractRange(lastClickedCell)
    						&& dudeMoving.act(1)){
    					gm.openDoor(lastClickedCell);
    				} else if (gm.tileMath.isDoor(lastClickedCell)) {
    					uiConsole.push("This door is out of " + dudeMoving.name() + "'s reach.");
    				}
    			}
    			lastClickedCell = new Coord(-1,-1);
    		}
    		boolean dudesMoving = false;
    		for (int i=0;i<dudes.size();i++){
    			if (dudes.get(i).position().equals(lastClickedCell)){
    				dudesMoving = true;
    				gm.setDudeMoving(i);
    			}
    		}
    		gm.setDudesMoving(dudesMoving);
    		if(!gm.dudesMoving()){
    			gm.setDudeMoving(-1);
    		}
    		//end dude moving
		
    		
    	} // end of dialogBox.enabled = false block
    		return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (screenX < 10 && camera.position.x > 767){
        	scrollingLeft = true;
        	//camera.translate(-1,0);
        } else {
        	scrollingLeft = false;
        }
        if(screenX > 1014){
            scrollingRight = true;
        } else {
        	scrollingRight = false;
        }
        if(screenY < 10){
            scrollingUp = true;
        } else {
        	scrollingUp = false;
        }
        if(screenY > 700){
            scrollingDown = true;
        } else {
        	scrollingDown = false;
        }
    	
    	return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void initializeDudes(){
    	dudes = new ArrayList<Dude>();
    	Texture rickyTexture = new Texture(Gdx.files.internal("gfx/ricky.png"));
    	Sprite glamourShot = new Sprite(new Texture(Gdx.files.internal("gfx/dude_glamourshot.png")));
    	Dude ricky = new Dude("Ricky", rickyTexture, 25, 15, 3, 25, 50, 75, 2, new Coord(4,2), gm, glamourShot);
    	ricky.setWeapon(new Weapon(WeaponType.PSIONIC_WILL_LV1));
    	dudes.add(ricky);
    }
    
    public void initializeEnemies(){
    	enemies = new ArrayList<Enemy>();
    	Texture copTexture = new Texture(Gdx.files.internal("gfx/cop.png"));
    	Sprite copGlamourShot = new Sprite(new Texture(Gdx.files.internal("gfx/cop_glamourshot.png")));
    	
    	enemies.add(new Enemy("Cop1", 
    			copTexture, 25, 3, 25, 50, 75, 15, 75, 
    			new Coord(6,2), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot,
    			gm));
    	enemies.add(new Enemy("Cop2", 
    			copTexture, 25, 3, 25, 50, 75, 25, 85, 
    			new Coord(7,6), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot,
    			gm));
    	enemies.add(new Enemy("Cop3", 
    			copTexture, 25, 3, 25, 50, 75, 10, 64, 
    			new Coord(3,8), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot,
    			gm));
    }
    
    public void showDialog(DialogInfo di){
    	System.out.println("YO");
    	dialogBox.setMessages(di.messages());
    	dialogBox.setOption1Msg(di.btn1());
    	dialogBox.setOption2Msg(di.btn2());
    	dialogBox.setType(di.type());
    	dialogBox.setMapCell(di.mapCoord());
    	
    	dialogBox.enable();
    }
    
    public void addInteractedTile(InteractedTile it){
    	this.interactedTiles.add(it);
    }

    public Coord pixelToCell(Coord pixelCoord){
    	return new Coord(pixelCoord.x()/128,pixelCoord.y()/128);
    }
    
    public Coord cellToPixel(Coord cellCoord){
    	return new Coord(cellCoord.x()*128,cellCoord.y()*128);
    }
    
    public Coord vector3ToCoord(Vector3 vector3){
    	return new Coord((int)(vector3.x),(int)(vector3.y));
    }
    
    private void executeDialogOption1(){
    	System.out.println("Dialog Option 1");
    	if (dialogBox.type() == DialogType.TILE_INTERACTION_DOOR){
    		mapData.openDoor(dialogBox.mapCell());
    		dialogBox.disable();
    	} else if (dialogBox.type() == DialogType.CUT_SCENE_LEVEL_START){
    		if (dialogBox.hasNextMessage()){
    			dialogBox.advanceMessage();
    		} else {
    			dialogBox.disable();
        		gm.takeDudesTurn();
                overlayFadeCounter = OVERLAY_FADE_TIME;

    		}
    	}
    	
    }
    
    private void executeDialogOption2(){
     	System.out.println("Dialog Option 2");
     	if (dialogBox.type() == DialogType.TILE_INTERACTION_DOOR){
     		dialogBox.disable();
     		
     	} else if (dialogBox.type() == DialogType.CUT_SCENE_LEVEL_START){
     		dialogBox.disable();
    		gm.takeDudesTurn();
            overlayFadeCounter = OVERLAY_FADE_TIME;

     	} else
     		dialogBox.disable();
    }
}
