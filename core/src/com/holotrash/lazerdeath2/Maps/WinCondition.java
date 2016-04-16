package com.holotrash.lazerdeath2.Maps;

import com.holotrash.lazerdeath2.LazerMath.Coord;

public class WinCondition {

	public WinType type;
	public int numTurns;
	public Coord mapCoord;
	
	//constructor for WinType KILL_EVERYONE
	public WinCondition(WinType type){
		this.type = type;
	}
	
	//constructor for WinType OCCUPY_MAP_CELL
	public WinCondition(WinType type, Coord mapCoord){
		this.type = type;
		this.mapCoord = mapCoord;
	}
		
	//constructor for WinType SURVIVE_NUM_TURNS
	public WinCondition(WinType type, int numTurns){
		this.type = type;
		this.numTurns = numTurns;
	}
}
