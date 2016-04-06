package com.holotrash.lazerdeath2;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

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
    
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    
    Dude[] dudes;
    Enemy[] enemies;
    EnemyAi enemyAi;
    ArrayList<HighlightTile> highlightTiles;
    ArrayList<InteractedTile> interactedTiles;
    boolean highlightOn;
    Color hlColor;
    DialogBox dialogBox;
    BitmapFont dialogFont;
    GlyphLayout layout;
    String temp;
    Vector3 tempV3;
  
    Map mapData;
    SpriteBatch spriteBatch;
    SpriteBatch highlightBatch;
    Coord lastClickedCell;
    GameMaster gm;
    
        
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
        gm.setDudes(this.dudes);
        gm.setEnemies(this.enemies);
        enemyAi = new EnemyAi(this.enemies, this.dudes);
        lastClickedCell = new Coord(-1,-1);
        highlightTiles = new ArrayList<HighlightTile>(); 
        highlightOn = false;
        interactedTiles = new ArrayList<InteractedTile>();
        
        tiledMap = new TmxMapLoader().load("gfx/" + mapData.tmxFileName + ".tmx");
        spriteBatch = new SpriteBatch();
        highlightBatch = new SpriteBatch();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, spriteBatch);
        hlColor = Color.WHITE;        
        Gdx.input.setInputProcessor(this);
        dialogBox = new DialogBox();
        dialogFont = new BitmapFont(Gdx.files.internal("fonts/bank.fnt"));
        layout = new GlyphLayout();
        gm.showLevelStartDialog();
        gm.takeDudesTurn();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        tiledMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        
        //change states of sprites based on dude/enemy flags here
        for (int i=0;i<dudes.length;i++){
        	if(dudes[i].position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(dudes[i]);
        	}
        }
        for (int i=0;i<enemies.length;i++){
        	if(enemies[i].position().equals(lastClickedCell) && gm.dudesTurn()){
        		makeRangeHighlight(enemies[i]);
        	}
        }
        
        spriteBatch.begin();
        //draw interacted tiles
        for (InteractedTile it : interactedTiles){
    		spriteBatch.draw(it.sprite, 128*it.position.x(), 128*it.position.y());
    	}
        //draw dudes and enemies
        for (int i=0;i<dudes.length;i++){
        	spriteBatch.draw(dudes[i].sprite(),
        			         128*(dudes[i].position().x()), 
        			         128*(dudes[i].position().y()));
        }
        for (int i=0;i<enemies.length;i++){
        	spriteBatch.draw(enemies[i].sprite(),
        					 128*(enemies[i].position().x()),
        				     128*(enemies[i].position().y()));
        }
        if (gm.dudesTurn()){
        	spriteBatch.setColor(hlColor);
        	for (HighlightTile hlt : highlightTiles){
        		spriteBatch.draw(hlt.sprite, 128*hlt.position.x(), 128*hlt.position.y());
        	}
        	spriteBatch.setColor(Color.WHITE);
        }
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
        spriteBatch.end();
        
        
        // check turn/game state rules
        if (gm.gameOver()){
        	//TODO: end game appropriately based on win/loss conditions
        } else if (gm.dudesTurn()){
        	if (gm.dudesTurnOver()){
        		gm.takeEnemiesTurn();
        	}
        } else if (gm.enemiesTurn()){
        	if (gm.enemiesTurnOver()){
        		gm.takeEnemiesTurn();
        	}
        	
        	if (enemyAi.hasNextEnemy() && enemyAi.nextEnemyReady())
            		enemyAi.nextEnemyMove();
        	
        }
    }
    
    
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
		
    		//move neccessary dudes
    		if (gm.dudesMoving()){ // dudes are responsible for limiting their own movement
    			dudes[gm.dudeMoving()].move(lastClickedCell);
    			lastClickedCell = new Coord(-1,-1);
    			this.highlightTiles.clear();
    		}
    		boolean dudesMoving = false;
    		for (int i=0;i<dudes.length;i++){
    			if (dudes[i].position().equals(lastClickedCell)){
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
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void initializeDudes(){
    	dudes = new Dude[1];
    	Texture rickyTexture = new Texture(Gdx.files.internal("gfx/ricky.png"));
    	Dude ricky = new Dude("Ricky", rickyTexture, 25, 3, 25, 50, 75, 7, new Coord(4,2), gm);
    	dudes[0] = ricky;
    	
    }
    
    public void initializeEnemies(){
    	enemies = new Enemy[3];
    	Texture copTexture = new Texture(Gdx.files.internal("gfx/cop.png"));
    	Enemy tempCop = new Enemy("Cop1", copTexture, 25, 3, 25, 50, 75, 7, new Coord(6,2));
    	enemies[0] = tempCop;
    	tempCop = new Enemy("Cop2", copTexture, 25, 3, 25, 50, 75, 7, new Coord(7,6));
    	enemies[1] = tempCop;
    	tempCop = new Enemy("Cop3", copTexture, 25, 3, 25, 50, 75, 7, new Coord(3,8));
    	enemies[2] = tempCop;
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
    	} else if (dialogBox.type() == DialogType.CUT_SCENE){
    		if (dialogBox.hasNextMessage()){
    			dialogBox.advanceMessage();
    		} else {
    			dialogBox.disable();
    		}
    	}
    	
    }
    
    private void executeDialogOption2(){
     	System.out.println("Dialog Option 2");
     	if (dialogBox.type() == DialogType.TILE_INTERACTION_DOOR){
     		dialogBox.disable();
     		
     	} else if (dialogBox.type() == DialogType.CUT_SCENE){
     		dialogBox.disable();
     	} else
     		dialogBox.disable();
    }
}
