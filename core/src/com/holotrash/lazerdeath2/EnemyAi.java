/**
 *  EnemyAi.java
 *  ----  
 *  Hive-mind of the Enemy. The Enemy is compelled to obey the EnemyAi's will.
 *  The EnemyAi's gaze controls the camera during the Enemy's turn.
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

import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.Maps.MapCell;
import com.holotrash.lazerdeath2.Units.Dude;
import com.holotrash.lazerdeath2.Units.Enemy;

public class EnemyAi {

	public int enemiesMoved;
	private GameMaster gm;
	
	private Dude dudeToConfront;
	private Coord coverToTake;
	private boolean enemyCovered;
	private int tempDistance;
	private int shortestDistance;
	
	
	
	public EnemyAi(GameMaster gm){
		this.gm = gm;
		this.enemiesMoved = 0;
	}
	
	public boolean hasNextEnemy() {
		return this.enemiesMoved < gm.enemies().size();
	}

	public void nextEnemyMove() {
		this.move(gm.enemies().get(enemiesMoved));
		this.enemiesMoved = this.enemiesMoved + 1;
	}

	private void move(Enemy enemy) {		
		//this.dudesWithinRange = new ArrayList<Dude>();
		//this.coverWithinRange = new ArrayList<Coord>();
		
		//TODO: determine the weakest dude. weigh the alternatives
		
		//get a list of all the dudes in attack range. 
				
		// determine the closest dude within range
		shortestDistance = 10000;

		for (Dude d : gm.dudes()){
			tempDistance = gm.tileMath.coordDistance(enemy.position(), d.position());
			if (tempDistance < shortestDistance)
				shortestDistance = tempDistance;
			this.dudeToConfront = d;
		}
		// TODO: determine the best cover in range. which cover provides the best cover
		// from the player's units?
		
		//determine the closest cover
		enemyCovered = false;
		shortestDistance = 10000;
		for (MapCell cell : gm.tileMath.coverTiles()){
			tempDistance = gm.tileMath.coordDistance(enemy.position(), cell.location());
			if (tempDistance == 1)
				enemyCovered = true;
			if (tempDistance < shortestDistance){
				shortestDistance = tempDistance;
				this.coverToTake = cell.location();
			}
			
		}


		if (enemyCovered || gm.dice.nextInt(99) < enemy.guts()){
			// attack
			boolean dudeIsInRange = gm.tileMath.withinAttackRange(enemy, dudeToConfront);
			
			if (!enemyCovered || !dudeIsInRange){
				Coord movePosition = gm.tileMath.closestReachableCoord(enemy, dudeToConfront);
				if (movePosition != null){
					enemy.move(movePosition);
					gm.game().uiConsole.push(enemy.name() + " moves to (" + movePosition.toString() + ").");
				}
			}
			// calculate dudeInRange again because enemy may have moved
			dudeIsInRange = gm.tileMath.withinAttackRange(enemy, dudeToConfront);
			if (dudeIsInRange){
				enemy.attack(dudeToConfront);
			}
		} else {
			// seek cover
			enemy.move(gm.tileMath.closestMoveable(enemy.position(), this.coverToTake, enemy.range()));
		}
		
	}
	
	public void newTurn(){
		this.enemiesMoved = 0;
	}
}
