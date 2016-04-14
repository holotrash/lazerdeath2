/**
 *  TileMath.java
 *  ----  
 *  Some of history's great figures were polymaths. This is just a TileMath.
 *  
 *  bresenhamLine() owes a lot to some example python code by Brian Will:
 *  https://www.youtube.com/watch?v=IDFB5CDpLDE
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
 * 
 */

package com.holotrash.lazerdeath2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

public class TileMath {
	
	private Map map;
	private ArrayList<MapCell> coverTiles;
	private AStarPathFinder pathfinder;
	private Dude dummyMover;
	private GameMaster gm;
	
	public TileMath(Map map, AStarPathFinder pathfinder, GameMaster gm){
		this.map = map;
		this.pathfinder = pathfinder;
		this.dummyMover = new Dude();
		this.gm = gm;
		
		this.coverTiles = new ArrayList<MapCell>();
		Coord maxCoord = map.maxCell;
		Coord thisCoord;
		MapCell thisCell;
		for (int x=0;x<maxCoord.x();x++){
			for (int y=0;y<maxCoord.y();y++){
				thisCoord = new Coord(x,y);
				thisCell = map.mapData.get(thisCoord);
				if (thisCell.cover()){
					coverTiles.add(thisCell);
				}
			}
		}
	}

	//returns the coords a unit is capable of moving to
	public HashSet<Coord> unitMoveCoords(Unit unit, int ap){
		HashSet<Coord> tempVal = getNeighbors(unit.position(), unit.speed()*ap);			
		HashSet<Coord> returnVal = new HashSet<Coord>();
		//for (int i=0;i<returnVal.size();i++)
		Iterator<Coord>	i = tempVal.iterator();
		Coord temp;
		while (i.hasNext()){
			temp = i.next();
			if (this.pathExists(unit.position(), temp, unit.speed()*ap)){
				returnVal.add(temp);
			}
		}
		return returnVal;
	}
	
	private HashSet<Coord> getNeighbors(Coord start, int range){ //range is speed/time, get it?
		HashSet<Coord> returnVal = new HashSet<Coord>();
		if (!(range == 0)){
			Coord[] nearNeighbors = new Coord[4];
			nearNeighbors[0] = new Coord(start.x(), start.y()+1);
			nearNeighbors[1] = new Coord(start.x(), start.y()-1);
			nearNeighbors[2] = new Coord(start.x()+1, start.y());
			nearNeighbors[3] = new Coord(start.x()-1, start.y());
			for (int i=0;i<4;i++){
				if(map.mapData.containsKey(nearNeighbors[i]) && map.mapData.get(nearNeighbors[i]).traversable())
				returnVal.add(nearNeighbors[i]);
			}
			for (int i=0;i<4;i++){
				HashSet<Coord> appendList = getNeighbors(nearNeighbors[i], range-1);
				for(Coord coord : appendList)
					returnVal.add(coord);
			}
		}
		return returnVal;
	}
	
	public boolean pathExists(Coord start, Coord end, int maxMoves){
		boolean returnVal;
		Path path =	pathfinder.findPath(dummyMover, start.x(), start.y(), end.x(), end.y());
		if (path == null || path.getLength() > maxMoves){
			returnVal = false;
		} else {
			returnVal = true;
		}
		return returnVal;
	}

	public boolean isInteractable(Coord coord) {
		boolean returnVal;
		if (!map.mapData.containsKey(coord))
			returnVal = false;
		else 
			returnVal = map.mapData.get(coord).interactable();
		return returnVal;
	}

	public boolean isDoor(Coord coord) {
		boolean returnVal;
		if(!map.mapData.containsKey(coord))
			returnVal = false;
		else 
			returnVal = map.mapData.get(coord).door();
		return returnVal;
	}

	public int coordDistance(Coord c1, Coord c2) {
		int xDistance = Math.abs(c1.x() - c2.x());
		int yDistance = Math.abs(c1.y() - c2.y());
		return xDistance + yDistance;
	}
	
	public ArrayList<MapCell> coverTiles(){
		return coverTiles;
	}

	public boolean withinAttackRange(Unit attacker, Unit mark) {
		return this.lineLength(attacker.position(), mark.position()) <= attacker.range();
	}
	
	public int lineLength(Coord start, Coord end){
		return Math.abs(start.x()-end.x()) + Math.abs(start.y()-end.y());
	}
	
	public Coord closestMoveable(Coord start, Coord end, int range){
		Coord returnVal;
		int closerX;
		int closerY;
		Path path = pathfinder.findPath(dummyMover, start.x(), start.y(), end.x(), end.y());
		
		if (Coord.coordsEqual(start, end)){
			returnVal = start;
		} else if (path == null || path.getLength() > range) {
			
			if (start.x() < end.x()){
				closerX = end.x() - 1;
			} else if (start.x() > end.x()){
				closerX = end.x() + 1;
			} else {
				closerX = end.x();
			}
			
			if (start.y() < end.y()){
				closerY = end.y() - 1;
			} else if (start.y() > end.y()) {
				closerY = end.y() + 1;
			} else {
				closerY = end.y();
			}
			returnVal = this.closestMoveable(start, 
					new Coord(closerX, closerY),
					range);
			
		} else {
			returnVal = end;
		}
		return returnVal;
	}

	public Coord closestReachableCoord(Unit mover, Unit attackTarget) {
		HashSet<Coord> moveCoords;
		int distance;
		int bestDistance = 10000;
		Path path = this.findPathToAdjacent(mover.position(), attackTarget.position());
		Coord returnVal;
		
		if (path != null){
			Coord pathEnd = new Coord(path.getX(path.getLength()-1),path.getY(path.getLength()-1));
			if (path.getLength() <= mover.speed()){
				returnVal = pathEnd;
			} else {
				moveCoords = this.unitMoveCoords(mover, 1);
				returnVal = new Coord(-1,-1);
				for (Coord c : moveCoords){
					distance = this.coordDistance(c, attackTarget.position());
					if (path.contains(c.x(), c.y()) && distance < bestDistance){
						bestDistance = distance;
						returnVal = c;
					}
				}
			}
		} else {
			returnVal = new Coord(-2,-2);
		}
		return returnVal;
	}
	
	private Path findPathToAdjacent(Coord start, Coord end){
		int x0 = end.x() + 1;
		int y0 = end.y();
		int x1 = end.x() - 1;
		int y1 = end.y();
		int x2 = end.x();
		int y2 = end.y() + 1;
		int x3 = end.x();
		int y3 = end.y() - 1;
		
		ArrayList<Path> paths = new ArrayList<Path>();
		Path path = null;
		
		paths.add(this.pathfinder.findPath(dummyMover, start.x(), start.y(), x0, y0));
		paths.add(this.pathfinder.findPath(dummyMover, start.x(), start.y(), x1, y1));
		paths.add(this.pathfinder.findPath(dummyMover, start.x(), start.y(), x2, y2));
		paths.add(this.pathfinder.findPath(dummyMover, start.x(), start.y(), x3, y3));
		
		for (Path p : paths){
			if (p != null && (path == null || p.getLength() < path.getLength())){
				path = p;
			}
		}
		return path;
		
	}
	
	public HashSet<Unit> unitsWithinAttackRange(Unit attacker){
		HashSet<Unit> result = new HashSet<Unit>();
		
		for (Dude dude : gm.dudes()){
			if (this.withinAttackRange(attacker, dude)){
				result.add(dude);
			}
		}
		for (Enemy enemy : gm.enemies()){
			if (this.withinAttackRange(attacker, enemy)){
				result.add(enemy);
			}
		}
		return result;
	}
	
}
