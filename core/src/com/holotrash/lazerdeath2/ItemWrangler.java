package com.holotrash.lazerdeath2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Items.Consumable;
import com.holotrash.lazerdeath2.Items.ConsumableEffect;
import com.holotrash.lazerdeath2.Items.Container;
import com.holotrash.lazerdeath2.Items.Item;

public class ItemWrangler {

	private HashMap<Coord, Item> items;
	private HashMap<Coord, Container> containers;
	
	public ItemWrangler(int level) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("data/level" + (new Integer(level).toString()) + ".itm"));
		String[] split;
		String line;
		Coord tempCoord;
		Item tempItem;
		ArrayList<ConsumableEffect> tempEffects;
		
		items = new HashMap<Coord,Item>();
		while(!(line = reader.readLine()).equals("END_ITEMS")){
			split = line.split(",");
			tempCoord = new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			
			if (split[2].equals("LAZERDABS")){
				tempEffects = new ArrayList<ConsumableEffect>();
				tempEffects.add(new ConsumableEffect(UnitStatistic.PP, 15, -1));
				tempEffects.add(new ConsumableEffect(UnitStatistic.SPEED, -1, 5));
				tempEffects.add(new ConsumableEffect(UnitStatistic.ACCURACY, -15, 3));
				tempEffects.add(new ConsumableEffect(UnitStatistic.DODGE, -15, 3));
				tempItem = new Consumable("LazerDabs",
										"A THC concentrate. For best results, use the green laser setting on your vape pen.",
										new Sprite(new Texture(Gdx.files.internal("gfx/items/lazer_dabs_inv"))), 
										new Sprite(new Texture(Gdx.files.internal("gfx/items/lazer_dabs_inv"))), 
										tempEffects);
			} else if (split[2].equals("HEALTH")){
				// make health pill
				
			}
			
			items.put(tempCoord, tempItem);
		}
		
		containers = new HashMap<Coord, Container>();
		while(!(line = reader.readLine()).equals("END_CONTAINERS")){
			split = line.split(",");
			tempCoord = new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			
		}
	}
	
}
