package com.holotrash.lazerdeath2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class HighlightTile {

	public Coord position; // position on map;
	public Sprite sprite;
	
	public HighlightTile(Coord position){
		this.position = position;
		this.sprite = new Sprite(new Texture(Gdx.files.internal("gfx/highlight_tile.png")));
	}
}
