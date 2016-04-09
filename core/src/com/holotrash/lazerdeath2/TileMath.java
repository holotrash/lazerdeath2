/**
 *  TileMath.java
 *  ----  
 *  Some of history's great figures were polymaths. This is just a TileMath.
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
	
	public TileMath(Map map, AStarPathFinder pathfinder){
		this.map = map;
		this.pathfinder = pathfinder;
		this.dummyMover = new Dude();
		
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
	public HashSet<Coord> unitMoveCoords(Unit unit){
		HashSet<Coord> tempVal = getNeighbors(unit.position(), unit.speed());			
		HashSet<Coord> returnVal = new HashSet<Coord>();
		//for (int i=0;i<returnVal.size();i++)
		Iterator<Coord>	i = tempVal.iterator();
		Coord temp;
		while (i.hasNext()){
			temp = i.next();
			if (this.pathExists(unit.position(), temp, unit.speed())){
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
	
	// returns null if no path can be found
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
	
}
