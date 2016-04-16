package com.holotrash.lazerdeath2.Items;

import com.holotrash.lazerdeath2.UnitStatistic;

public class ConsumableEffect {

	public UnitStatistic statistic;
	public int modifier;
	public int duration; // in number of turns
	public int expiration;
	
	public ConsumableEffect (UnitStatistic stat, int mod, int dur){
		this.statistic = stat;
		this.modifier = mod;
		this.duration = dur;
	}
}
