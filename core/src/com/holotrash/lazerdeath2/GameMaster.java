package com.holotrash.lazerdeath2;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;

public class GameMaster {

	public Map mapData;
	private Dude[] dudes;
	private Enemy[] enemies;
	public TileMath tileMath;
	private DialogLibrarian dialogLibrarian;
	private lazerdeath2 game;
	
	// flagz
	private boolean dudesWon;
	private boolean enemiesWon;
	private boolean dudesMoving;
	private int dudeMoving;
	private boolean dudesTurn;
	private boolean enemiesTurn;
	
	public GameMaster(lazerdeath2 game, Map mapData){
		this.game = game;
		this.mapData = mapData;
		this.dudesWon = false;
		this.enemiesWon = false;
		this.dudesTurn = false;
		this.enemiesTurn = false;
		this.dudesMoving = false;
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
}
