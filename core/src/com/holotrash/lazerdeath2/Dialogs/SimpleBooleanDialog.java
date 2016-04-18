package com.holotrash.lazerdeath2.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.LazerMath.Coord;
import com.holotrash.lazerdeath2.LazerMath.Region;

public class SimpleBooleanDialog {

	private boolean enabled;
	private boolean result;
	private boolean resultRecorded;
	
	private String text;
	private String choice1;
	private String choice2;
	
	private Sprite bg;
	private Sprite btns;
	
	private Region btn1region;
	private Region btn2region;
	private BooleanDialogType type;
	
	public SimpleBooleanDialog(String text, String choice1, String choice2){
		this.text = text;
		this.choice1 = choice1;
		this.choice2 = choice2;
		this.type = null;
		
		this.bg = new Sprite(new Texture(Gdx.files.internal("gfx/simple_dialog_bg.png")));
		this.btns = new Sprite(new Texture(Gdx.files.internal("gfx/simple_dialog_btns.png")));
	
		this.enabled = false;
		this.resultRecorded = false;
		
		this.btn1region = new Region(560, 730, 365, 430);
		this.btn2region = new Region(783, 955, 365, 430);
		
	}
	
	public Sprite background(){
		return this.bg;
	}
	
	public Sprite buttons(){
		return this.btns;
	}
	
	public boolean enabled(){
		return this.enabled;
	}
	
	public String text(){
		return this.text;
	}
	
	public String choice1(){
		return this.choice1;
	}
	
	public String choice2(){
		return this.choice2;
	}
	
	public boolean button1Hit(int x, int y){
		return this.btn1region.isInside(new Coord(x, y));
	}
	
	public boolean button2Hit(int x, int y){
		return this.btn2region.isInside(new Coord(x,y));
	}
	
	public boolean resultRecorded(){
		return this.resultRecorded;
	}
	
	public boolean result(){
		return this.result;
	}
	
	public BooleanDialogType type(){
		return this.type;
	}
	
	public void recordResult(boolean value){
		this.result = value;
		this.resultRecorded = true;
	}
	
	public void setText(String newText){
		this.text = newText;
	}
	
	public void setChoice1(String newChoice1){
		this.choice1 = newChoice1;
	}
	
	public void setChoice2(String newChoice2){
		this.choice2 = newChoice2;
	}
	
	public void setType(BooleanDialogType type){
		this.type = type;
	}
	
	public void enable(){
		this.enabled = true;
	}
	
	public void disable(){
		this.enabled = false;
	}
	
	public void resetResult(){
		this.resultRecorded = false;
	}
	
	
}
