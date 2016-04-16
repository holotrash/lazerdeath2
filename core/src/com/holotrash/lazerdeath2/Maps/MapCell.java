/**
 *  MapCell.java
 *  ----  
 *  A data structure containing all the junk you would want to know about a
 *  map cell. Is the cell traversable, occupied, interactable, or cover? Is
 *  it a door? 
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

import com.holotrash.lazerdeath2.LazerMath.Coord;

public class MapCell {
	
	private Coord location;
	private boolean traversable;
	private boolean occupied;
	private boolean interactable;
	private boolean cover;
	private boolean isDoor;
	private Coord interactedRegion; // offset for the tile once it's interacted
									// -1 if interactable = false
	
	public MapCell(Coord l, boolean t, boolean c, boolean i, boolean d, Coord ir){
		this.location = l;
		this.traversable = t;
		this.occupied = false;
		this.interactable = i;
		this.cover = c;
		this.isDoor = d;
		this.interactedRegion = ir;
	}
	
	public Coord location(){
		return location;
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
	
	public void setOccupied(boolean bool)
	{
		this.occupied = bool;
	}
}
