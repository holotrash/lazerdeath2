package com.holotrash.lazerdeath2;

public class MapCell {
	
	private boolean traversable;
	private boolean occupied;
	private boolean interactable;
	private boolean cover;
	private boolean isDoor;
	private Coord interactedRegion; // offset for the tile once it's interacted
									// -1 if interactable = false
	
	public MapCell(boolean t, boolean c, boolean i, boolean d, Coord ir){
		this.traversable = t;
		this.occupied = false;
		this.interactable = i;
		this.cover = c;
		this.isDoor = d;
		this.interactedRegion = ir;
	}
	
	public boolean traversable(){
		return traversable;
	}
	
	public boolean occupied(){
		return occupied;
	}

	public boolean interactable(){
		return interactable;
	}
	
	public boolean cover(){
		return cover;
	}
	
	public boolean door(){
		return isDoor;
	}
	
	public Coord iRegion(){
		return interactedRegion;
	}
	
	public void setInteractable(boolean bool){
		this.interactable = bool;
	}
	
	public void setTraversable(boolean bool){
		this.traversable = bool;
	}
	
}
