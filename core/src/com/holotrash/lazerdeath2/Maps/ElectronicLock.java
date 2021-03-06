package com.holotrash.lazerdeath2.Maps;

import com.holotrash.lazerdeath2.Items.Key;

public class ElectronicLock implements Lock{

	private int code;
	
	public ElectronicLock(int code){
		this.code = code;
	}
	
	public int code(){
		return this.code;
	}

	@Override
	public boolean isElectronic() {
		return true;
	}

	@Override
	public boolean unlocks(Key key) {
		boolean result;
		
		if(key.isElectronic() && key.pattern() == this.code){
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
}
