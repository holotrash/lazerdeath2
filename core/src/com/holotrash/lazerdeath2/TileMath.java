package com.holotrash.lazerdeath2;

import java.util.HashSet;
import java.util.Iterator;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

public class TileMath {
	
	private Map map;
	private AStarPathFinder pathfinder;
	private Dude dummyMover;
	
	public TileMath(Map map, AStarPathFinder pathfinder){
		this.map = map;
		this.pathfinder = pathfinder;
		this.dummyMover = new Dude();
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
	
}
