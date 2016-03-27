package com.holotrash.lazerdeath2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class InteractedTile {
	public Coord position; // position on map;

	public Sprite sprite;
	
	public InteractedTile(Coord position, Coord region, String mapTilesFile){
		this.position = position;
		this.sprite = new Sprite(new Texture(Gdx.files.internal(mapTilesFile)), 128*region.x(), 128*region.y(), 128, 128);
	}
}
