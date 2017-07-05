package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rod on 7/5/2017.
 */

public class LoadingScreen implements Screen{
    final TapRunner game;
    OrthographicCamera cam;
    TextureAtlas.AtlasRegion ground, bg;
    Array<TextureAtlas.AtlasRegion> regionRun;
    static GameAssetManager manager;
    public static TextureAtlas atlas;
    public Animation<TextureRegion> animationFast;
    public float stateTime;

    public LoadingScreen(final TapRunner gam){
        this.game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        manager = new GameAssetManager();
        manager.loadImages();
        manager.manager.finishLoading();

        atlas = manager.manager.get("packedimages/runner.atlas");
        bg = atlas.findRegion("background");
        ground = atlas.findRegion("ground");
        regionRun = atlas.findRegions("run");
        animationFast = new Animation<TextureRegion>(0.07f, regionRun);

        GameAssetLoader.load();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();

        game.batch.setProjectionMatrix(cam.combined);

        if(GameAssetLoader.update()) {
            GameAssetLoader.getLoadedAssets();
            game.setScreen(new MainMenuScreen(game));
        }

        game.batch.begin();

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentRunnerFrame;
        currentRunnerFrame = animationFast.getKeyFrame(stateTime, true);
        game.batch.draw(currentRunnerFrame, (cam.viewportWidth / 2) - (currentRunnerFrame.getRegionWidth() / 2), (cam.viewportHeight / 2) - currentRunnerFrame.getRegionHeight());

        game.batch.end();

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
