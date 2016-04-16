package com.holotrash.lazerdeath2.Items;

import java.util.ArrayList;

import com.holotrash.lazerdeath2.LazerMath.Coord;

public class Container {

	public Coord position;
	public ArrayList<Item> contents;
	
	public Container(){
		this.contents = new ArrayList<Item>();
	}
}
