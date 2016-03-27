package com.holotrash.lazerdeath2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DialogLibrarian {
	
	private Hashtable<String, DialogInfo> dialogs;
	private static final int LINE_LENGTH = 27;
	
	public DialogLibrarian(int level) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader("data/level" + (new Integer(level).toString()) + ".dlg"));
		String[] split;
		String line;
		ArrayList<String> messagesTemp;
		String temp;
		dialogs = new Hashtable<String, DialogInfo>();
		
		while((line = reader.readLine()) != null){
			if (line.trim().equals("START")){
				messagesTemp = new ArrayList<String>();
				while((line = reader.readLine()).charAt(0) != '%'){
					messagesTemp.add(line);
				}
				split = line.substring(1).split(",");
				dialogs.put("START", 
						new DialogInfo(this.convertMessages(messagesTemp), split[0], split[1], DialogType.CUT_SCENE));
			} // end if cutscene start
			else if (line.trim().equals("END")){
				messagesTemp = new ArrayList<String>();
				while((line = reader.readLine()).charAt(0) != '%'){
					messagesTemp.add(line);
				}
				split = line.substring(1).split(",");
				dialogs.put("END", 
						new DialogInfo(this.convertMessages(messagesTemp), split[0], split[1], DialogType.CUT_SCENE));
			} // end if cutscene end 
			else if (line.trim().equals("DOOR")){
				messagesTemp = new ArrayList<String>();
				temp = reader.readLine(); // tile coord
				messagesTemp.add(reader.readLine().trim());
				split = reader.readLine().substring(1).split(",");
				dialogs.put(temp, 
						new DialogInfo(this.convertMessages(messagesTemp), split[0], split[1], DialogType.TILE_INTERACTION_DOOR));
			} // end if tile interaction
			else if (line.trim().equals("DEATH")){
				messagesTemp = new ArrayList<String>();
				temp = reader.readLine(); // dying unit name
				messagesTemp.add(reader.readLine().trim());
				split = reader.readLine().substring(1).split(",");
				dialogs.put(temp, 
						new DialogInfo(this.convertMessages(messagesTemp), split[0], split[1], DialogType.DEATH_SCENE));
			}
		}
		
		reader.close();
			
	}
	
	public DialogInfo getDialogInfo(String key){
		return dialogs.get(key);
	}
	
	private ArrayList<ArrayList<String>> convertMessages(ArrayList<String> messages){
		ArrayList<ArrayList<String>> returnVal = new ArrayList<ArrayList<String>>();
		ArrayList<String> temp = new ArrayList<String>();
		String currentMessage;
		String[] split;
		StringBuilder line = new StringBuilder();
		int j = 0;
		
		for (int i=0;i<messages.size();i++){
			currentMessage = messages.get(i);
			split = currentMessage.split(" ");
			while(j < split.length){
				while (j < split.length && line.length() + split[j].length() < LINE_LENGTH){
					line.append(split[j] + " ");
					j++;
				}
				temp.add(line.toString().trim());
				line = new StringBuilder();
			}
			j=0;
			returnVal.add(temp);
		}
		return returnVal;
	}
}
