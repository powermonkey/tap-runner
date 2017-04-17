package com.rcam.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rcam.game.TapRunner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TapRunner.WIDTH;
		config.height = TapRunner.HEIGHT;
		config.title = TapRunner.TITLE;
		new LwjglApplication(new TapRunner(), config);
	}
}