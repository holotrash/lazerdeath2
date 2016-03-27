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
