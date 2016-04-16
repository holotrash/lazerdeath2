package com.holotrash.lazerdeath2.LazerMath;

public class Region {

	public Coord max;
	public Coord min;
	
	public Region(int minX, int maxX, int minY, int maxY){
		this.max = new Coord(maxX, maxY);
		this.min = new Coord(minX, minY);
	}
	
	public boolean isInside(Coord c){
		return c.x() < max.x() &&
				c.x() > min.x() &&
				c.y() < max.y() &&
				c.y() > min.y();
	}
}
