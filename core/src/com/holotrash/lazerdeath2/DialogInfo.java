package com.holotrash.lazerdeath2;

import java.util.ArrayList;

public class DialogInfo {

	private ArrayList<ArrayList<String>> messages;
	private String btn1;
	private String btn2;
	private DialogType type;
	private Coord mapCoord;
	
	public DialogInfo(ArrayList<ArrayList<String>> messages, 
			String btn1, String btn2, DialogType type){
		this.messages = messages;
		this.btn1 = btn1;
		this.btn2 = btn2;
		this.type = type;
	}
	
	public ArrayList<ArrayList<String>> messages(){
		return messages;
	}
	
	public String btn1(){
		return btn1;
	}
	public String btn2(){
		return btn2;
	}
	public DialogType type(){
		return type;
	}
	public Coord mapCoord(){
		return mapCoord;
	}
	public void setMapCoord(Coord mapCoord){
		this.mapCoord = mapCoord;
	}
}
