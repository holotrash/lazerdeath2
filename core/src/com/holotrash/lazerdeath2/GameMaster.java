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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;

public class GameMaster {

	public Map mapData;
	private Dude[] dudes;
	private Enemy[] enemies;
	public TileMath tileMath;
	private DialogLibrarian dialogLibrarian;
	private lazerdeath2 game;
	public Random dice;
	
	private int clock; // mystic clock bounded between 0 and 3600
	
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
		this.tileMath = new TileMath(mapData, new AStarPathFinder(mapData, largestDimension, false));
		 try {
	        	//TODO: make this load the current level rather than 0
	        	dialogLibrarian = new DialogLibrarian(0);
			} catch (IOException e) {
				//TODO: report error loading the current level rather than 0
				System.out.println("Error loading Dialog Librarian for level 0");
				e.printStackTrace();
			}
		 dice = new Random();
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
	
	public void setDudes(Dude[] dudes){
		this.dudes = dudes;
	}
	
	public void setEnemies(Enemy[] enemies){
		this.enemies = enemies;
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
	}
	
	public boolean enemiesTurn(){
		return enemiesTurn;
	}
	public void takeEnemiesTurn(){
		enemiesTurn = true;
		dudesTurn = false;
	}
	
	public HashSet<Coord> unitMoveCoords(Unit unit){
		return tileMath.unitMoveCoords(unit);
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
		boolean allEnemiesDead = true;
		for (Dude dude : dudes){
			if (!dude.isDead()){
				allDudesDead = false;
			}
		}
		for (Enemy enemy : enemies){
			if (!enemy.isDead()){
				allEnemiesDead = false;
			}
		}
		return allDudesDead || allEnemiesDead;
	}

	public boolean dudesTurnOver() {
		boolean allDudesActed = true;
		for (Dude dude : dudes){
			if (!dude.hasMoved() || !dude.hasAttacked())
				allDudesActed = false;
		}
		return allDudesActed;
	}

	public void resetDudesMoved() {
		for (Dude dude : dudes){
			dude.setMovable();
		}
	}

	public boolean enemiesTurnOver() {
		boolean allEnemiesMoved = true;
		for (Enemy enemy : enemies){
			if (!enemy.hasMoved() || !enemy.hasAttacked())
				allEnemiesMoved = false;
		}
		return allEnemiesMoved;
	}

	public void resetEnemiesMoved() {
		for (Enemy enemy : enemies){
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
}
