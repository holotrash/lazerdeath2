package com.holotrash.lazerdeath2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NoteLibrarian {

	private ArrayList<ArrayList<String>> notes;
	private ArrayList<Boolean> acquisitionStatuses;
	private ArrayList<String> acquiredNoteTitles;
	private ArrayList<Integer> acquiredNoteIndices;
	
	public NoteLibrarian() throws IOException{
		notes = new ArrayList<ArrayList<String>>();
		this.acquisitionStatuses = new ArrayList<Boolean>();
		BufferedReader reader = new BufferedReader(new FileReader("data/notes.csv"));
		String line;
		ArrayList<String> temp = new ArrayList<String>();
		
		while((line = reader.readLine()) != null){
			if (line.length() > 3 && line.substring(0, 3).equals("NOTE")){
				if (temp.size() != 0){
					notes.add(temp);
					this.acquisitionStatuses.add(false);
				}
				temp = new ArrayList<String>();
			} else {
				temp.add(line);
			}
		}
		notes.add(temp);
		this.acquisitionStatuses.add(false);
		reader.close();
		this.update();
	}
	
	public ArrayList<String> acquiredNoteTitles(){
		return this.acquiredNoteTitles;
	}
	
	public int numNotesAcquired(){
		return this.acquiredNoteIndices.size();
	}
	
	public void acquireNote(int noteNumber){
		this.acquisitionStatuses.set(noteNumber, true);
		this.update();
	}
	
	public ArrayList<String> note(int relativeIndex){
		return this.notes.get(this.acquiredNoteIndices.get(relativeIndex));
	}
	
	private void update(){
		this.acquiredNoteTitles = new ArrayList<String>();
		this.acquiredNoteIndices = new ArrayList<Integer>();
		 
		for (int i=0;i<notes.size();i++){
			if(this.acquisitionStatuses.get(i)){
				this.acquiredNoteTitles.add(notes.get(i).get(0));
				this.acquiredNoteIndices.add(i);
			}
		}
	}
	
}
