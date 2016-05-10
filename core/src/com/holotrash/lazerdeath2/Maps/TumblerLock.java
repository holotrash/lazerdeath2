package com.holotrash.lazerdeath2.Maps;

import com.holotrash.lazerdeath2.Items.Key;

public class TumblerLock implements Lock{
	
	private int tumbler;
	
	public TumblerLock(int tumbler){
		this.tumbler = tumbler;
	}
	
	public int tumbler(){
		return this.tumbler;
	}

	@Override
	public boolean isElectronic() {
		return false;
	}

	@Override
	public boolean unlocks(Key key) {
		boolean result;
		
		if (!key.isElectronic() && key.pattern() == this.tumbler){
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
