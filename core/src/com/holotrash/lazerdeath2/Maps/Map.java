/**
 *  Map.java
 *  ----  
 *  A self-loading map. Contains information about the cells on the map
 *  such as if it is traversable, occupied, interactable, cover, etc.
 *  
 *  Implements the Slick2D java game library's TileBasedMap interface so 
 *  that Map can be used with the Slick A* pathfinder. 
 *  http://slick.ninjacave.com/
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

package com.holotrash.lazerdeath2.Maps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.util.pathfinding.*;

import com.holotrash.lazerdeath2.lazerdeath2;
import com.holotrash.lazerdeath2.Items.Container;
import com.holotrash.lazerdeath2.Items.Item;
import com.holotrash.lazerdeath2.LazerMath.Coord;

public class Map implements TileBasedMap{

	public String tmxFileName;
	public int scrollUpMax;        // TODO: lazerdeath2 must use these values rather than hardcoded ones
	public int scrollDownMax;
	public int scrollLeftMax;
	public int scrollRightMax;
	public Coord maxCell;          // map dimensions defined by the cell with the maximum x and y value
	public HashMap<Coord, MapCell> mapData;
	public ArrayList<WinCondition> winConditions;
	public ArrayList<Container> containers;
	public ArrayList<Item> items;
	public HashMap<Coord, Door> doors;
	private lazerdeath2 game;		
	
	private Coord aCoord;
	
	/* constructor */
	public Map(int level, lazerdeath2 game) throws IOException{
		this.game = game;
		mapData = new HashMap<Coord, MapCell>();
		this.doors = new HashMap<Coord, Door>();
		this.winConditions = new ArrayList<WinCondition>();
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("data/level" + (new Integer(level).toString()) + ".lvl"));
		String[] coordSplit;
		String[] regionSplit;
		String[] dataSplit;
		boolean traversable;
		boolean cover;
		boolean interactable;
		boolean door;
		Coord region;
		String line;
		String[] split;
		//read map info
		line = reader.readLine();
		split = line.split("#");
		this.tmxFileName = split[1];
		line = reader.readLine();
		split = line.split("#");
		this.scrollUpMax = Integer.parseInt(split[1]);
		line = reader.readLine();
		split = line.split("#");
		this.scrollDownMax = Integer.parseInt(split[1]);
		line = reader.readLine();
		split = line.split("#");
		this.scrollLeftMax = Integer.parseInt(split[1]);
		line = reader.readLine();
		split = line.split("#");
		this.scrollRightMax = Integer.parseInt(split[1]);
		line = reader.readLine();
		split = line.split("#");
		dataSplit = split[1].split(",");
		this.maxCell = new Coord(Integer.parseInt(dataSplit[0]), Integer.parseInt(dataSplit[1]));
		
		//read win conditions
		line = reader.readLine();
		split = line.split("#");
		while(!split[0].equals("endwin")){
			split = split[1].split(",");
			if (split[0].equals("CELL")){
				aCoord = new Coord(Integer.parseInt(split[1]),Integer.parseInt(split[2]));
				this.winConditions.add(new WinCondition(WinType.OCCUPY_MAP_CELL, aCoord));
			} else if (split[0].equals("KILLALL")){
				this.winConditions.add(new WinCondition(WinType.KILL_EVERYONE));
			} else if (split[0].equals("SURVIVE")){
				this.winConditions.add(new WinCondition(WinType.SURVIVE_NUM_TURNS, Integer.parseInt(split[1])));
			} else {
				throw new IOException ("BULLSHIT WIN CONDITION TYPE IDENTIFIER IN MAP DATA FILE, NUMBNUTS!!! should be CELL, KILLALL, or SURVIVE");
			}
			line = reader.readLine();
			split = line.split("#");
		}
		//read map cells
		
		while ((line = reader.readLine()).trim().compareTo("END_CELLS") != 0){
			split = line.split("#");
			coordSplit = split[0].split(",");
			dataSplit = split[1].split(",");
			
			String debug = "";
			for (int i=0;i<coordSplit.length;i++)
				debug = debug + coordSplit[i];
			System.out.println("Attempting to parse map coordinate (" + debug + ")");
			if (dataSplit[0].substring(0).compareTo("T") == 0)
				traversable = true;
			else if (dataSplit[0].substring(0).compareTo("F") == 0)
				traversable = false;
			else 
				throw new IOException("invalid traversable value for cell (" + Integer.parseInt(coordSplit[0]) + "," + Integer.parseInt(coordSplit[0]) + ")");
			
			if (dataSplit[1].substring(0).compareTo("T") == 0)
				cover = true;
			else if (dataSplit[1].substring(0).compareTo("F") == 0)
				cover = false;
			else 
				throw new IOException("invalid cover value for cell (" + Integer.parseInt(coordSplit[0]) + "," + Integer.parseInt(coordSplit[0]) + ")");
		
			if (dataSplit[2].substring(0).compareTo("T") == 0)
				interactable = true;
			else if (dataSplit[2].substring(0).compareTo("F") == 0)
				interactable = false;
			else 
				throw new IOException("invalid interactable value for cell (" + Integer.parseInt(coordSplit[0]) + "," + Integer.parseInt(coordSplit[1]) + ")");
		

			if (dataSplit[3].substring(0).compareTo("T") == 0)
				door = true;
			else if (dataSplit[3].substring(0).compareTo("F") == 0)
				door = false;
			else 
				throw new IOException("invalid door value for cell (" + Integer.parseInt(coordSplit[0]) + "," + Integer.parseInt(coordSplit[1]) + ")");
		
			if (dataSplit[4].substring(0).compareTo("NULL") == 0){
				region = new Coord(-1,-1);
			} else {
				regionSplit = dataSplit[4].split("x");
				region = new Coord(Integer.parseInt(regionSplit[0]),Integer.parseInt(regionSplit[1]));
			}
			if (interactable){
				System.out.println("adding interactable tile at Coord (" + Integer.parseInt(coordSplit[0]) + "," + Integer.parseInt(coordSplit[1]) + ")");
			}
			aCoord = new Coord(Integer.parseInt(coordSplit[0]), Integer.parseInt(coordSplit[1]));
			mapData.put(aCoord, new MapCell(aCoord, traversable, cover, interactable, door, region));
			
		} // end while
		
		// load doors
		while ((line = reader.readLine()).trim().compareTo("END_DOORS") != 0){
			split = line.split(",");
			aCoord = new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			if (split[2].trim().equals("LOCKED")){
				if(split[3].trim().equals("ELECTRONIC")){
					doors.put(aCoord, new Door(new ElectronicLock(
							Integer.parseInt(split[4].trim()))));
				} else {
					doors.put(aCoord, new Door(new TumblerLock(
							Integer.parseInt(split[4].trim()))));
				}
			} else { // door unlocked
				doors.put(aCoord, new Door(new NoLock()));
			}
		}
		reader.close();
	} // end constructor

	@Override
	public int getWidthInTiles() {
		return maxCell.x();
	}

	@Override
	public int getHeightInTiles() {
		return maxCell.y();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		boolean isBlocked;
		Coord coord = new Coord(x,y);
		MapCell cell;
		if (!mapData.containsKey(coord)){
			isBlocked = true;
		} else {
			cell = mapData.get(coord);
			isBlocked = (!cell.traversable() || cell.occupied());
		}
		return isBlocked;
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		// TODO make move cost relative to unit? terrain?
		return 1;
	}

	public void openDoor(Coord coord) {
		MapCell temp = mapData.get(coord);
		temp.setInteractable(false);
		temp.setTraversable(true);
		mapData.put(coord, temp);
		InteractedTile it = new InteractedTile(coord, temp.iRegion(), "gfx/" + tmxFileName + ".png");
		game.addInteractedTile(it);
	}
	
	public void setTileOccupied(Coord coord, boolean status){
		MapCell temp = mapData.get(coord);
		temp.setOccupied(status);
		mapData.put(coord, temp);
	}
	
}
