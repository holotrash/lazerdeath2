package com.holotrash.lazerdeath2.Dialogs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.LazerMath.Region;

public class MenuComponent {

	public String name;
	public Coord position;
	public Sprite sprite;
	public Region region;
	public boolean isButton;
	
	public MenuComponent(
			String name,
			Coord position, 
			Sprite sprite, 
			Region region, 
			boolean isButton){
		this.name = name;
		this.position = position;
		this.sprite = sprite;
		this.region = region;
		this.isButton = isButton;
		
	}

	public boolean coordHits(Coord c) {
		boolean result;
		
		if (this.region == null){
			result = false;
		} else {
			result = region.isInside(c);
		}
		
		return result;
	}
	
}
