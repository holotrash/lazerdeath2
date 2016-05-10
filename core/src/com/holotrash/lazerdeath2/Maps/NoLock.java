package com.holotrash.lazerdeath2.Maps;

import com.holotrash.lazerdeath2.Items.Key;

public class NoLock implements Lock{

	@Override
	public boolean isElectronic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlocks(Key key) {
		return true;
	}

}
