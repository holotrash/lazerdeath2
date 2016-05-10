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
	
	private String line1;
	private String line2;
	private String line3;
	private String choice1;
	private String choice2;
	
	private Sprite bg;
	private Sprite btns;
	
	private Region btn1region;
	private Region btn2region;
	private BooleanDialogType type;

	
	public SimpleBooleanDialog(String line1, String line2, String line3, String choice1, String choice2){
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
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
	
	public String line1(){
		return this.line1;
	}
	
	public String line2(){
		return this.line2;
	}
	
	public String line3(){
		return this.line3;
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
	
	public void setLine1(String newText){
		this.line1 = newText;
	}
	
	public void setLine2(String newText){
		this.line2 = newText;
	}
	
	public void setLine3(String newText){
		this.line3 = newText;
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
