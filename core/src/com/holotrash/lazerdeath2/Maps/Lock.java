package com.holotrash.lazerdeath2.Maps;

import com.holotrash.lazerdeath2.Items.Key;

public interface Lock {

	public boolean isElectronic();
	public boolean unlocks(Key key);
	
}
