package com.holotrash.lazerdeath2;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;

public class GameMaster {

	private Map mapData;
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
	
	public GameMaster(lazerdeath2 game, Map mapData, Dude[] dudes, Enemy[] enemies){
		this.game = game;
		this.mapData = mapData;
		this.dudes = dudes;
		this.enemies = enemies;
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
	
	public boolean dudesTurn(){
		return dudesTurn;
	}
	
	public void takeDudesTurn(){
		dudesTurn = true;
	}
	
	public boolean enemiesTurn(){
		return enemiesTurn;
	}
	public void takeEnemiesTurn(){
		enemiesTurn = true;
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
}
