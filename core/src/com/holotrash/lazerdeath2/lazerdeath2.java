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
import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class lazerdeath2 extends ApplicationAdapter implements InputProcessor {
    
	public static final int OVERLAY_FADE_TIME = 90;
	public static final int SCROLLING_MULTIPLIER = 3;
	
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    
    ArrayList<Dude> dudes;
    ArrayList<Enemy> enemies;
    
    ArrayList<HighlightTile> highlightTiles;
    ArrayList<InteractedTile> interactedTiles;
    boolean highlightOn;
    Color hlColor;
    DialogBox dialogBox;
    BitmapFont dialogFont;
    BitmapFont uiFont;
    GlyphLayout layout;
    String temp;
    Vector3 tempV3;
  
    Map mapData;
    SpriteBatch spriteBatch;
    SpriteBatch highlightBatch;
    Coord lastClickedCell;
    Unit selectedUnit;
    
    GameMaster gm;

    Sprite dudesTurnSprite;
    Sprite enemiesTurnSprite;
    Sprite leftStatusBox;
    Sprite rightStatusBox;
    Sprite rightStatusBoxFade;
    Sprite glamourShot;
    Sprite defaultGlamourShot;
    
    ArrayList<String> currentUnitStats;
    ArrayDeque<String> uiConsole;
    Iterator<String> consoleIterator;
    
    int overlayFadeCounter;
    
    boolean scrollingUp = false;
    boolean scrollingDown = false;
    boolean scrollingLeft = false;
    boolean scrollingRight = false;
        
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
        highlightTiles = new ArrayList<HighlightTile>(); 
        highlightOn = false;
        interactedTiles = new ArrayList<InteractedTile>();
        
        dudesTurnSprite = new Sprite(new Texture(Gdx.files.internal("gfx/dudes_turn.png")));
        enemiesTurnSprite = new Sprite(new Texture(Gdx.files.internal("gfx/enemy_turn.png")));
        this.leftStatusBox = new Sprite(new Texture(Gdx.files.internal("gfx/left_status_box.png")));
        this.rightStatusBox = new Sprite(new Texture(Gdx.files.internal("gfx/right_status_box.png")));
        this.rightStatusBoxFade = new Sprite(new Texture(Gdx.files.internal("gfx/right_status_box_fade_out.png")));
        this.defaultGlamourShot = new Sprite(new Texture(Gdx.files.internal("gfx/default_glamourshot.png")));
        this.glamourShot = defaultGlamourShot;
        
        tiledMap = new TmxMapLoader().load("gfx/" + mapData.tmxFileName + ".tmx");
        spriteBatch = new SpriteBatch();
        highlightBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, spriteBatch);
        hlColor = Color.WHITE;        
        Gdx.input.setInputProcessor(this);
        dialogBox = new DialogBox();
        dialogFont = new BitmapFont(Gdx.files.internal("fonts/bank.fnt"));
        uiFont = new BitmapFont(Gdx.files.internal("fonts/hack.fnt"));
        layout = new GlyphLayout();
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
        
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (scrollingUp){
        	camera.translate(0,SCROLLING_MULTIPLIER);
        }
        if (scrollingDown){
        	camera.translate(0, 0-SCROLLING_MULTIPLIER);
        }
        if (scrollingLeft){
        	camera.translate(0-SCROLLING_MULTIPLIER, 0);
        }
        if (scrollingRight){
        	camera.translate(SCROLLING_MULTIPLIER, 0);
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
        	if(dudes.get(i).position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(dudes.get(i));
        	}
        }
        for (int i=0;i<enemies.size();i++){
        	if(enemies.get(i).position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(enemies.get(i));
        	}
        }
        
        spriteBatch.begin();
   
        //draw interacted tiles
        for (InteractedTile it : interactedTiles){
    		spriteBatch.draw(it.sprite, 128*it.position.x(), 128*it.position.y());
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
        	spriteBatch.setColor(hlColor);
        	for (HighlightTile hlt : highlightTiles){
        		spriteBatch.draw(hlt.sprite, 128*hlt.position.x(), 128*hlt.position.y());
        	}
        	spriteBatch.setColor(Color.WHITE);
        }
        // draw left status dialog stuff
        if (this.selectedUnit != null){
        	this.glamourShot = selectedUnit.glamourShot();
        	this.currentUnitStats = selectedUnit.toStringz();
        	// change left status box text to relevant unit stats:
        	
        }
        
        tempV3 = new Vector3(0,768,0);
        tempV3 = camera.unproject(tempV3);
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
        }
        
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
        
        spriteBatch.end();
        	
        gm.clockTick();
        gm.advanceGame();
        
    } // end render()
    
    
    private void makeRangeHighlight(Unit unit) {
    	highlightTiles.clear();
    	HashSet<Coord> tileCoords = gm.unitMoveCoords(unit);
    	if (unit.isDude()){
    		hlColor = new Color(0.0f,0.0f,1.0f,0.4f);
    	} else {
    		hlColor = new Color(1.0f,0.0f,0.0f,0.4f);
    	}
    	
    	for(Coord coord : tileCoords){
    		highlightTiles.add(new HighlightTile(coord));
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
    		
    	} else{
    		lastClickedCell = pixelToCell(vector3ToCoord(camera.unproject(new Vector3(screenX, screenY, 0))));
    		System.out.println("last clicked cell: (" + lastClickedCell.x() + "," + lastClickedCell.y() + ")");
		
    		//move necessary dudes
    		if (gm.dudesMoving()){ // dudes are responsible for limiting their own movement and attack
    			//if lastClickedCell contains a unit, attempt to attack.
    			// otherwise attempt to move
    			Dude dudeMoving = dudes.get(gm.dudeMoving());
    			boolean attack = false;
    			for (Dude dude : dudes){
    				if (dude.position().equals(lastClickedCell)){
    					if (!(dudeMoving == dude)){
    						dudeMoving.Attack(dude);
    						System.out.println("Attacking dude: " + dude.name());
    						attack = true;
    					}
    				}
    			}
    			for (Enemy enemy : enemies){
    				if (enemy.position().equals(lastClickedCell)){
    					dudeMoving.Attack(enemy);
    					System.out.println("Attacking enemy: " + enemy.name());
    					attack = true;
    				}
    			}
    			if (!attack)
    				dudes.get(gm.dudeMoving()).move(lastClickedCell);
    			lastClickedCell = new Coord(-1,-1);
    			this.highlightTiles.clear();
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
		
    		// TODO: check for interactable tiles
    		if (gm.tileMath.isInteractable(lastClickedCell)){
    			if (gm.tileMath.isDoor(lastClickedCell))
    					gm.openDoor(lastClickedCell);
    			lastClickedCell = new Coord(-1,-1);
    		}
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
        if(screenX > 1014 && camera.position.x < 769){
            scrollingRight = true;
        } else {
        	scrollingRight = false;
        }
        if(screenY < 10 && camera.position.y < 1025){
            scrollingUp = true;
        } else {
        	scrollingUp = false;
        }
        if(screenY > 700 && camera.position.y > 511){
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
    	Dude ricky = new Dude("Ricky", rickyTexture, 25, 3, 25, 50, 75, new Coord(4,2), gm, glamourShot);
    	ricky.setWeapon(new Weapon(WeaponType.PSIONIC_WILL_LV1));
    	dudes.add(ricky);
    }
    
    public void initializeEnemies(){
    	enemies = new ArrayList<Enemy>();
    	Texture copTexture = new Texture(Gdx.files.internal("gfx/cop.png"));
    	Sprite copGlamourShot = new Sprite(new Texture(Gdx.files.internal("gfx/cop_glamourshot.png")));
    	
    	enemies.add(new Enemy("Cop1", 
    			copTexture, 25, 3, 25, 50, 75, 7, 15, 75, 
    			new Coord(6,2), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot));
    	enemies.add(new Enemy("Cop2", 
    			copTexture, 25, 3, 25, 50, 75, 7, 25, 85, 
    			new Coord(7,6), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot));
    	enemies.add(new Enemy("Cop3", 
    			copTexture, 25, 3, 25, 50, 75, 7, 10, 64, 
    			new Coord(3,8), 
    			new Weapon(WeaponType.PHASE_BLUDGEON_LV1), 
    			copGlamourShot));
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
