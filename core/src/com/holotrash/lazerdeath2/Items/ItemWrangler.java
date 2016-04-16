package com.holotrash.lazerdeath2.Items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.GameMaster;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.Units.UnitStatistic;

public class ItemWrangler {

	public HashMap<Coord, Item> items;
	public HashMap<Coord, Container> containers;
	private GameMaster gm;
	
	public ItemWrangler(GameMaster gm, int level) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("data/level" + (new Integer(level).toString()) + ".itm"));
		String[] split;
		String line;
		Coord tempCoord;
		Item tempItem;
		int tempInt;
		String tempString;
		ArrayList<ConsumableEffect> tempEffects = new ArrayList<ConsumableEffect>();
		this.gm = gm;
		
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
										new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/lazer_dabs_inv.png"))), 
										new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/lazer_dabs_inv.png"))), 
										tempEffects);
			} else if (split[2].equals("HEALTH")){
				// make health pill
				tempEffects = new ArrayList<ConsumableEffect>();
				tempEffects.add(new ConsumableEffect(UnitStatistic.HP, 15, -1));
				tempItem = new Consumable("HEALTH",
										"If you don't have HEALTH brand pills, you don't have health!",
										new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/health_inv.png"))), 
										new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/health_inv.png"))), 
										tempEffects);
			} else if (split[2].length() > 3 && split[2].substring(0, 3).equals("NOTE")){
				// make note (lol)
				tempInt = Integer.parseInt(split[2].substring(4, 4));
				tempItem = new NoteItem(gm, 
						gm.noteLibrarian.noteTitle(tempInt), 
						"A note", 
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/note_inv.png"))), 
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/note_inv.png"))),
						tempInt);
			} else if (split[2].length() > 6 && split[2].substring(0, 6).equals("KEYCARD")){
				// make key
				tempInt = Integer.parseInt(split[2].substring(7, 7));
				//TODO: handle keycards above index 9 (parse double digits)
				if (tempInt < 10){
					tempString = "0" + tempInt;
				} else {
					tempString = "" + tempInt;
				}
				
				tempItem = new KeyCard(gm, 
						"A keycard", 
						"A keycard",
						tempInt,
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/keycard_" + tempString + "_inv.png"))), 
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/keycard_" + tempString + "_inv.png"))));
			} else if (split[2].length() > 2 && split[2].substring(0, 2).equals("KEY")){
				// make key
				tempInt = Integer.parseInt(split[2].substring(3, 3));
				//TODO: handle keys above index 9 (parse double digits)
				if (tempInt < 10){
					tempString = "0" + tempInt;
				} else {
					tempString = "" + tempInt;
				}
				
				tempItem = new Key(gm, 
						tempInt,
						"A key", 
						"A key", 
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_" + tempString + "_inv.png"))), 
						new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_" + tempString + "_inv.png"))));
			} else {
				tempItem = new Consumable(line, line, null, null, tempEffects);
				throw new IOException("bad item in /data/level" + (new Integer(level).toString()) + ".itm" +"\r\n" + line);
			}
			
			items.put(tempCoord, tempItem);
		}// end while (load items)
		Container tempContainer;
		containers = new HashMap<Coord, Container>();
		while(!(line = reader.readLine()).equals("END_CONTAINERS")){

			split = line.split(",");
			tempCoord = new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
			tempContainer = new Container();
			tempContainer.position = tempCoord;
			
			for (int i=2;i<split.length;i++){
				if (split[i].equals("LAZERDABS")){
					tempEffects = new ArrayList<ConsumableEffect>();
					tempEffects.add(new ConsumableEffect(UnitStatistic.PP, 15, -1));
					tempEffects.add(new ConsumableEffect(UnitStatistic.SPEED, -1, 5));
					tempEffects.add(new ConsumableEffect(UnitStatistic.ACCURACY, -15, 3));
					tempEffects.add(new ConsumableEffect(UnitStatistic.DODGE, -15, 3));
					tempItem = new Consumable("LazerDabs",
											"A THC concentrate. For best results, use the green laser setting on your vape pen.",
											new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/lazer_dabs_inv.png"))), 
											new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/lazer_dabs_inv.png"))), 
											tempEffects);
				} else if (split[i].equals("HEALTH")){
					// make health pill
					tempEffects = new ArrayList<ConsumableEffect>();
					tempEffects.add(new ConsumableEffect(UnitStatistic.HP, 15, -1));
					tempItem = new Consumable("HEALTH",
											"If you don't have HEALTH brand pills, you don't have health!",
											new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/health_inv.png"))), 
											new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/health_dabs_inv.png"))), 
											tempEffects);
				} else if (split[i].length() > 3 && split[i].substring(0, 4).equals("NOTE")){
					// make note (lol)
					tempInt = Integer.parseInt(split[i].substring(4, 5));
					tempItem = new NoteItem(gm, 
							gm.noteLibrarian.noteTitle(tempInt), 
							"A note", 
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/note_inv.png"))), 
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/note_inv.png"))),
							tempInt);
				} else if (split[i].length() > 6 && split[i].substring(0, 7).equals("KEYCARD")){
					// make key
					tempInt = Integer.parseInt(split[i].substring(7, 8));
					//TODO: handle keycards above index 9 (parse double digits)
					if (tempInt < 10){
						tempString = "0" + tempInt;
					} else {
						tempString = "" + tempInt;
					}
					
					tempItem = new KeyCard(gm, 
							"A keycard", 
							"A keycard",
							tempInt,
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/keycard_" + tempString + "_inv.png"))), 
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/keycard_" + tempString + "_inv.png"))));
				} else if (split[i].length() > 2 && split[i].substring(0, 3).equals("KEY")){
					// make key
					tempInt = Integer.parseInt(split[i].substring(3, 4));
					//TODO: handle keys above index 9 (parse double digits)
					if (tempInt < 10){
						tempString = "0" + tempInt;
					} else {
						tempString = "" + tempInt;
					}
					
					tempItem = new Key(gm, 
							tempInt,
							"A key", 
							"A key", 
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_" + tempString + "_inv.png"))), 
							new Sprite(new Texture(Gdx.files.internal("gfx/items/inv/key_" + tempString + "_inv.png"))));
				} else {
					tempItem = new Consumable(line, line, null, null, tempEffects);
					throw new IOException("bad container item in /data/level" + (new Integer(level).toString()) + ".itm" +"\r\n" + line);
				}
				
				tempContainer.contents.add(tempItem);
			}// end for (load single container
			containers.put(tempCoord, tempContainer);
		} // end while (load containers)
		reader.close();
	}// end constructor
	
}
