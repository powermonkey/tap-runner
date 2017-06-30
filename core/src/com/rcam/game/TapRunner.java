package com.rcam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TapRunner extends Game{
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "TapRunner";
	SpriteBatch batch;
	BitmapFont font;

	public AdsController adsController;

	public TapRunner(AdsController adsController){
		if (adsController != null) {
			this.adsController = adsController;
		} else {
			this.adsController = new DummyController();
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		GameAssetLoader.load();
		this.setScreen(new MainMenuScreen(this));
		if(adsController.isWifiConnected()) {
			adsController.showBannerAd();
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
