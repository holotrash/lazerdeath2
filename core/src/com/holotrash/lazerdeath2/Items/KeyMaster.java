package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;

public class KeyMaster {

	public static TumblerKey getTumblerKey(GameMaster gm, int pattern){
		String name=null;
		String description=null;
		Sprite tileSprite=null; 
		Sprite invSprite=null;
		
		if (pattern == 0){
			name = "basement key";
			description = "Cop 3's key to the basement door.";
			tileSprite = new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_00_inv.png")));
			invSprite = new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_00_inv.png")));
		}
		
		return new TumblerKey(gm, pattern, name, description, tileSprite, invSprite);
	}
	
}
