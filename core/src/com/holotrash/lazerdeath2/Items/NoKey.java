package com.holotrash.lazerdeath2.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.holotrash.lazerdeath2.Units.Unit;

public class NoKey implements Item, Key {

	@Override
	public boolean isElectronic() {
		return false;
	}

	@Override
	public int pattern() {
		return -1;
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public String description() {
		return null;
	}

	@Override
	public Sprite tileSprite() {
		return null;
	}

	@Override
	public Sprite invSprite() {
		return null;
	}

	@Override
	public void beUsed(Unit unit) {
		
	}

	@Override
	public boolean isKey() {
		return true;
	}

}
