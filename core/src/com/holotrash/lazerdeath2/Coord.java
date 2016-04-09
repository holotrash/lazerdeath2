/**
 *  Coord.java
 *  ----  
 *  Represents a 2D coordinate. Could be a coordinate on a grid of pixels
 *  or a grid of map cells depending on the context.
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

public class Coord {

	private int x;
	private int y;
	
	public Coord(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	@Override
	public boolean equals(Object other){
		boolean returnVal;
		if (other == null) {
			returnVal = false;
		} else if (this.getClass() != other.getClass())
			returnVal = false;
		else if(((Coord)other).x() == this.x && ((Coord)other).y() == this.y){
			returnVal = true;
		} else {
			returnVal = false;
		}
		return returnVal;
	}
	
	@Override
	public int hashCode(){
		final int PRIME1 = 31;
		final int PRIME2 = 7;
	    int result;
	    result = (PRIME1 * y) + (PRIME2 * x); 
	    return result;
	}
	
	@Override
	public String toString(){
		return Integer.toString(x) + "," + Integer.toString(y);
	}
}
