package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
    Array<TextureAtlas.AtlasRegion> regionRun;
    public static TextureAtlas loadingAtlas;
    public Animation<TextureRegion> animationFast;
    public float stateTime;
    private final AssetManager assetManager;


    public LoadingScreen(final TapRunner gam){
        this.game = gam;
        assetManager = new AssetManager();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        assetManager.load("packedimages/loading.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        loadingAtlas = assetManager.get("packedimages/loading.atlas");
        regionRun = loadingAtlas.findRegions("run");
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

        game.batch.setProjectionMatrix(cam.combined);

        if(GameAssetLoader.update()) {
            GameAssetLoader.getLoadedAssets();
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }

        game.batch.disableBlending();
        game.batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentRunnerFrame = animationFast.getKeyFrame(stateTime, true);
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
        assetManager.dispose();
        loadingAtlas.dispose();
    }
}
