/**
 *  GameMaster.java
 *  ----  
 *  Represents an essential force in the in-game universe that dictates 
 *  the happenings in-game. I don't think the GameMaster is like, an old
 *  white dude with a big beard that plays a harp in the clouds or something, 
 *  but I think, you know, we keep taking these turns and fighting these
 *  bad guys and opening doors and all this, and, you know, I just think 
 *  there has to be something out there. You know what I mean, man?
 *  ---------------------------------------------------------------------
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
 */
package com.holotrash.lazerdeath2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;

import com.holotrash.lazerdeath2.Dialogs.DialogInfo;
import com.holotrash.lazerdeath2.Dialogs.DialogLibrarian;
import com.holotrash.lazerdeath2.Items.Item;
import com.holotrash.lazerdeath2.Items.ItemWrangler;
import com.holotrash.lazerdeath2.Items.NoteLibrarian;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.LazerMath.TileMath;
import com.holotrash.lazerdeath2.Maps.Map;
import com.holotrash.lazerdeath2.Maps.WinCondition;
import com.holotrash.lazerdeath2.Maps.WinType;
import com.holotrash.lazerdeath2.Units.Dude;
import com.holotrash.lazerdeath2.Units.Enemy;
import com.holotrash.lazerdeath2.Units.Unit;

public class GameMaster {

	public final static int ENEMY_MOVE_LENGTH = 100;
	public static final Coord nullCoord = new Coord(-1,-1);
	
	public Map mapData;
	public TileMath tileMath;
	private DialogLibrarian dialogLibrarian;
	public NoteLibrarian noteLibrarian;
	public ItemWrangler itemWrangler;
	private lazerdeath2 game;
	public ArrayList<Item> inventory;
	
	private EnemyAi enemyAi;
	public Random dice;
	
	private int clock; // mystic clock bounded between 0 and 3600
	private int numTurns;
	
	// flagz
	private boolean dudesWon;
	private boolean enemiesWon;
	private boolean dudesMoving;
	private boolean sceneMovement;
	private int dudeMoving;
	private boolean dudesTurn;
	private boolean enemiesTurn;
	
	public GameMaster(lazerdeath2 game, Map mapData){
		this.clock = 0;
		this.game = game;
		this.mapData = mapData;
		this.dudesWon = false;
		this.enemiesWon = false;
		this.dudesTurn = false;
		this.enemiesTurn = false;
		this.dudesMoving = false;
		this.sceneMovement = false;
		this.dudeMoving = -1;
		int width = mapData.maxCell.x();
		int height = mapData.maxCell.y();
		int largestDimension;
		if (width < height){
			largestDimension = height;
		} else
			largestDimension = width;
		
		this.tileMath = new TileMath(mapData, new AStarPathFinder(mapData, largestDimension, false), this);
		 try {
	        	//TODO: make this load the current level rather than 0
	        	dialogLibrarian = new DialogLibrarian(0);
			} catch (IOException e) {
				//TODO: report error loading the current level rather than 0
				System.out.println("Error loading Dialog Librarian for level 0");
				e.printStackTrace();
			}
		 dice = new Random();
		 enemyAi = new EnemyAi(this);
		 numTurns = 0;
		 inventory = new ArrayList<Item>();
		 try {
			 System.out.println("Loading note librarian");
			this.noteLibrarian = new NoteLibrarian();
		} catch (IOException e) {
			System.out.println("Failed to load notes from /data/notes.csv");
			e.printStackTrace();
		}
		try {
			System.out.println("Loading item wrangler");
			this.itemWrangler = new ItemWrangler(this, 0);
		} catch (IOException e1) {
			System.out.println("failed to create ItemWrangler:" + e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	public void clockTick(){
		if (clock < 3600){
			clock++;
		} else {
			clock = 0;
		}
	}
	
	public int time(){
		return clock;
	}
	
    public void showLevelStartDialog(){
    	DialogInfo di = dialogLibrarian.getDialogInfo("START");
    	game.showDialog(di);
    }
	
	public boolean dudesTurn(){
		return dudesTurn;
	}
	
	public void takeDudesTurn(){
		dudesTurn = true;
		enemiesTurn = false;
		for (Dude dude : game.dudes){
			dude.resetAp();
		}
		this.numTurns++;
		game.uiConsole.push("Round " + numTurns + ".");
		game.overlayFadeCounter = lazerdeath2.OVERLAY_FADE_TIME;
	}
	
	public boolean enemiesTurn(){
		return enemiesTurn;
	}
	public void takeEnemiesTurn(){
		enemiesTurn = true;
		dudesTurn = false;
		game.overlayFadeCounter = lazerdeath2.OVERLAY_FADE_TIME;
	}
	
	public HashSet<Coord> unitMoveCoords(Unit unit, int ap){
		return tileMath.unitMoveCoords(unit, ap);
	}
	
	public boolean dudesWon(){
		return dudesWon;
	}
	public boolean enemiesWon(){
		return enemiesWon;
	}
	public boolean dudesMoving(){
		return dudesMoving;
	}
	public void setDudesMoving(boolean state){
		this.dudesMoving = state;
	}
	public int dudeMoving(){
		return dudeMoving;
	}
	
	public void setDudeMoving(int dude){
		this.dudeMoving = dude;
	}

	public void openDoor(Coord lastClickedCell) {
		DialogInfo di = dialogLibrarian.getDialogInfo(lastClickedCell.toString());
		di.setMapCoord(lastClickedCell);
		game.showDialog(di);
	}

	public boolean gameOver() {
		boolean allDudesDead = true;
		for (Dude dude : game.dudes){
			if (!dude.isDead()){
				allDudesDead = false;
			}
		}
		
		return allDudesDead || this.gameWon();
	}

	public boolean gameWon(){
		boolean winConditionAchieved = false;
		boolean allEnemiesDead = true;
		for (WinCondition wc : mapData.winConditions){
			if (wc.type == WinType.KILL_EVERYONE){
				for (Enemy e : game.enemies){
					if (!e.isDead()){
						allEnemiesDead = false;
					}
				}
				if (allEnemiesDead == true)
					winConditionAchieved = true;
			} else if (wc.type == WinType.OCCUPY_MAP_CELL){
				for (Dude d : game.dudes){
					if (Coord.coordsEqual(d.position(), wc.mapCoord)){
						winConditionAchieved = true;
					}
				}
			} else if (wc.type == WinType.SURVIVE_NUM_TURNS){
				if (this.numTurns > wc.numTurns){
					winConditionAchieved = true;
				}
			}
		}
		return winConditionAchieved;
	}
	
	public boolean dudesTurnOver() {
		boolean allDudesActed = true;
		for (Dude dude : game.dudes){
			if (dude.ap() > 0)
				allDudesActed = false;
		}
		return allDudesActed;
	}

	public void resetDudesAP() {
		for (Dude dude : game.dudes){
			dude.resetAp();
		}
	}

	public boolean enemiesTurnOver() {
		boolean allEnemiesMoved = true;
		for (Enemy enemy : game.enemies){
			if (!enemy.hasMoved() || !enemy.hasAttacked())
				allEnemiesMoved = false;
		}
		return allEnemiesMoved;
	}

	public void resetEnemiesMoved() {
		for (Enemy enemy : game.enemies){
			enemy.setMovable();
		}
	}
	
	public boolean sceneMovement(){
		return this.sceneMovement;
	}
	
	public void setSceneMovement(boolean status){
		this.sceneMovement = status;
	}
	
	public void printToConsole(String output){
		game.uiConsole.push(output);
	}

	public void advanceGame() {
		for (int i=0;i<game.dudes.size();i++){
			if (game.dudes.get(i).isDead()){
				game.dudes.get(i).die();
				//TODO: show dude death dialog
				game.dudes.remove(i);
			}
				
		}
		for (int i=0;i<game.enemies.size();i++){
			if (game.enemies.get(i).isDead()){
				game.enemies.get(i).die();
				//TODO: show enemy death dialog
				game.enemies.remove(i);
			}
				
		}
		if (this.gameOver()){
			if (this.gameWon()){
				game.uiConsole.push("Player Wins!");
				//TODO: show end level cut scene and advance to next level
			} else {
				game.uiConsole.push("GAME OVER.");
			}
		}else if (this.dudesTurn){
			if (this.dudesTurnOver()){
				if(game.enemies.size() > 0){
					this.takeEnemiesTurn();
				} else {
					this.takeDudesTurn();
				}
			}
		} else {
			//advance enemy turn
			if (this.clock % ENEMY_MOVE_LENGTH == 0){
				if (enemyAi.hasNextEnemy()){
					enemyAi.nextEnemyMove();
				} else {
					this.takeDudesTurn();
					this.enemyAi.newTurn();
				}
			}
		}
	}
	
	public ArrayList<Dude> dudes(){
		return game.dudes;
	}
	
	public ArrayList<Enemy> enemies(){
		return game.enemies;
	}
	
	public lazerdeath2 game(){
		return this.game;
	}
	
	public void pickUpItem(Item item){
		this.inventory.add(item);
	}
	public int turn(){
		return numTurns;
	}

	public void useItemDialog(int inventoryCursorIndex) {
		// TODO Auto-generated method stub
		
	}
}
