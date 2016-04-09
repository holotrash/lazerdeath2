/**
 *  DialogInfo.java
 *  ----  
 *  An object containing all the information required to prepare a DialogBox
 *  to be shown.
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
