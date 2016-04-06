package com.holotrash.lazerdeath2;

public class EnemyAi {

	public Enemy[] enemies;
	public Dude[] dudes;
	public int enemiesMoved;
	
	public EnemyAi(){
		this.enemiesMoved = 0;
	}
	
	public EnemyAi(Enemy[] enemies, Dude[] dudes){
		this.enemies = enemies;
		this.dudes = dudes;
		this.enemiesMoved = 0;
	}
	
	public void setEnemies(Enemy[] enemies){
		this.enemies = enemies;
	}

	public boolean hasNextEnemy() {
		return this.enemiesMoved < this.enemies.length;
	}

	public void nextEnemyMove() {
		this.move(enemies[enemiesMoved]);
		this.enemiesMoved = this.enemiesMoved + 1;
	}

	private void move(Enemy enemy) {
		// TODO: Auto-generated method stubr
		
		// TODO: determine move strategy
		// 		 	- find Dude dudeToConfront
		//			- find Coord closeCover
		
	}

	public boolean nextEnemyReady() {
		// TODO human-generated method stub
		return this.hasNextEnemy();
	}
	
	public void newTurn(){
		this.enemiesMoved = 0;
	}
	
}
