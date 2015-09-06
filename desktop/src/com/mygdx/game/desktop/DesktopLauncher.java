package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//Setup
		config.width = 300;
		config.height = 300;
		config.resizable=false;
		config.title="Collision Detect";

		new LwjglApplication(new MyGdxGame(), config);
	}
}
