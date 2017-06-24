package com.rcam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class TapRunner extends Game{
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final String TITLE = "TapRunner";
	SpriteBatch batch;
	BitmapFont font;

	private AdsController adsController;

	public TapRunner(AdsController adsController){
		this.adsController = adsController;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
		adsController.showBannerAd();
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
