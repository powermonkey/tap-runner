package com.rcam.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;
    Hud hud;

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

        hud = new Hud(runner);

    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        cam.position.x = runner.getPosition().x + 80;
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);
        for(Ground ground: grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        runner.update(delta); //render first then logic fixes shaking texture, why?

        hud.meter.update(runner.getSpeed().x);
        game.batch.end();
        hud.render();
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
        for(Ground ground : grounds)
            ground.dispose();
        hud.dispose();
    }
}
