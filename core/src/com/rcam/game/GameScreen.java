package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    static final int GROUND_COUNT = 2;
    final TapRunner game;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;

    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(50, 112);
        cam = new OrthographicCamera();
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));
    }

    public void handleInput() {
        if(Gdx.input.justTouched())
            runner.run();
    }

    @Override
    public void render(float delta) {
        //logic first
        cam.position.x = runner.getPosition().x + 80;
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }

        cam.update();
        handleInput();
        runner.update(delta);

        //then render
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        for(Ground ground: grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);
        game.batch.end();
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
        runner.dispose();
        grnd.dispose();
    }
}
