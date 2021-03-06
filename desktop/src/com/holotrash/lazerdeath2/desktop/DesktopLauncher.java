package com.holotrash.lazerdeath2.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.holotrash.lazerdeath2.lazerdeath2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "LAZERDEATH 2";
		config.width = 1366;
		config.height = 768;
		config.fullscreen = true;
		new LwjglApplication(new lazerdeath2(), config);
	}
}
