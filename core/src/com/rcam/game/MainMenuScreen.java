package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Rod on 4/14/2017.
 */

public class MainMenuScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Texture bg, playBtn, ground;

    public MainMenuScreen(final TapRunner gam){
        game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        bg = new Texture("bg.png");
        playBtn = new Texture("playBtn.png");
        ground = new Texture("ground.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(playBtn, (cam.viewportWidth / 2) - (playBtn.getWidth() / 2 ), cam.viewportHeight / 2);
        game.batch.draw(ground, 0,0);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
