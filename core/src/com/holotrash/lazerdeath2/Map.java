package com.holotrash.lazerdeath2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import org.newdawn.slick.util.pathfinding.*;

public class Map implements TileBasedMap{

	public String tmxFileName;
	public int scrollUpMax;
	public int scrollDownMax;
	public int scrollLeftMax;
	public int scrollRightMax;
	public Coord maxCell;          // map dimensions defined by the cell with the maximum x and y value
	public HashMap<Coord, MapCell> mapData;
	private lazerdeath2 game;
	
	/* constructor */
	public Map(int level, lazerdeath2 game) throws IOException{
		this.game = game;
		mapData = new HashMap<Coord, MapCell>();
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
		
		//read map cells
		
		while ((line = reader.readLine()) != null){
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
			mapData.put(new Coord(Integer.parseInt(coordSplit[0]),Integer.parseInt(coordSplit[1])), new MapCell(traversable, cover, interactable, door, region));
			
		} // end while
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
		if (!mapData.containsKey(coord)){
			isBlocked = true;
		} else {
			isBlocked = !(mapData.get(new Coord(x,y)).traversable());
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
	
}