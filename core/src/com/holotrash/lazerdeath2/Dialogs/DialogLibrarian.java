/**
 *  DialogLibrarian.java
 *  ----  
 *  The DialogLibrarian loads the data required to generate the DialogInfos
 *  needed for each level and provides the relevant DialogInfo objects on
 *  request.
 *  ---------------------------------------------------------------------
 *  This file is part of the computer game Lazerdeath2 
 *  Copyright 2016, Robert Watson Craig III
 *
 *  Lazerdeath2 is free software published under the terms of the GNU
 *  General Public License version 3. You can redistribute it and/or 
 *  modify it under the terms of the GPL (version 3 or any later version).
 * 
 *  Lazerdeath2 is distributed in the hope that it will be entertaining,
 *  cool, and totally smooth for your mind to rock to, daddy, but WITHOUT 
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 *  FITNESS FOR A PARTICULAR PURPOSE; without even the suggestion of an
 *  implication that any of this code makes any sense whatsoever. It works
 *  on my computer and I don't think that's such a weird environment, but
 *  it might be. Or maybe it's your computer that's the weird one, did you
 *  ever think of that?  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lazerdeath2.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.holotrash.lazerdeath2.Dialogs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class DialogLibrarian {
	
	private Hashtable<String, DialogInfo> dialogs;
	private static final int LINE_LENGTH = 27;
	private static final int NUM_LINES = 6;
	
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
						new DialogInfo(this.convertMessages(messagesTemp), split[0], split[1], DialogType.CUT_SCENE_LEVEL_START));
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
		int k = 0;
		for (int i=0;i<messages.size();i++){
			currentMessage = messages.get(i);
			split = currentMessage.split(" ");
			j=0;
			while(j < split.length && k < NUM_LINES){
				while (j < split.length && line.length() + split[j].length() < LINE_LENGTH){
					line.append(split[j] + " ");
					j++;
				}
				temp.add(line.toString());
				line = new StringBuilder();
				//k++;
			}
			returnVal.add(temp);
			temp = new ArrayList<String>();
		}
		return returnVal;
	}
}
