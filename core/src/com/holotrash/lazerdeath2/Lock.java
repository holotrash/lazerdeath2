package com.holotrash.lazerdeath2;

public class Lock {
	
	private int tumbler;
	
	public Lock(int tumbler){
		this.tumbler = tumbler;
	}
	
	public int tumbler(){
		return this.tumbler;
	}
}
