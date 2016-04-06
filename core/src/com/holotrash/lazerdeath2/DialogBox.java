package com.holotrash.lazerdeath2;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DialogBox {

	private Sprite background;
	private Sprite button;
	private ArrayList<ArrayList<String>> messageText;
	private String option1Msg;
	private String option2Msg;
	private boolean enabled;
	private DialogType type;
	private int messageIndex;
	private Coord mapCell; // for tile interactions
	
	public DialogBox(){
		background = new Sprite(new Texture(Gdx.files.internal("gfx/messagebox.png")));
		button = new Sprite(new Texture(Gdx.files.internal("gfx/button.png")));
		messageText = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("Do you want to perform this action?");
		messageText.add(temp);
		option1Msg = "YES";
		option2Msg = "NO";
		enabled = false;
		messageIndex = 0;
	}
	
	public Sprite background(){
		return background;
	}
	
	public Sprite button(){
		return button;
	}
	
	public void enable(){
		this.enabled = true;
	}
	
	public void disable(){
		this.enabled = false;
	}
	
	public boolean enabled(){
		return enabled;
	}
	
	public void setMessages(ArrayList<ArrayList<String>> messages){
		this.messageText = messages;
		this.messageIndex = 0;
	}
	
	public boolean hasNextMessage(){
		boolean returnVal;
		if (messageIndex < messageText.size() - 1)
			returnVal = true;
		else
			returnVal = false;
		return returnVal;
	}
	
	public void advanceMessage(){
		messageIndex++;
	}
	
	public ArrayList<String> currentMessage(){
		return messageText.get(messageIndex);
	}
	
	public String btn1(){
		return option1Msg;
	}
	
	public String btn2(){
		return option2Msg;
	}
	
	public void setOption1Msg(String message){
		option1Msg = message;
	}
	public void setOption2Msg(String message){
		option2Msg = message;
	}
	public void setType(DialogType type){
		this.type = type;
	}
	public DialogType type(){
		return type;
	}
	public void setMapCell(Coord cell){
		this.mapCell = cell;
	}
	public Coord mapCell(){
		return mapCell;
	}
}
