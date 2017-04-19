package com.rcam.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Runner;
import com.rcam.game.sprites.enemies.GroundEnemy1;
import com.sun.java_cup.internal.runtime.Symbol;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    final static float STARTING_X = 30;
    final static float STARTING_Y = 112;
    final static float SPAWN_OFFSET_X = 300;
    final static float SPAWN_DISTANCE = 1000;
    float spawnMarker = 1000;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;
    Hud hud;
    GroundEnemy1 groundEnemy1;

    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(STARTING_X, STARTING_Y);
        cam = new OrthographicCamera();
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));

        hud = new Hud(runner);

        groundEnemy1 = new GroundEnemy1();
    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        cam.position.x = runner.getPosition().x + 100;
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);

        if(runner.getPosition().x > spawnMarker ){
            Vector2 spawnPosition = new Vector2();
            spawnPosition.x = spawnMarker + SPAWN_OFFSET_X;
            spawnPosition.y = STARTING_Y;
            groundEnemy1.setPosition(spawnPosition);
            groundEnemy1.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
            spawnMarker += SPAWN_DISTANCE;
            groundEnemy1.isSpawned = true;
        }

        if(groundEnemy1.isSpawned){
            if(!(cam.position.x - (cam.viewportWidth / 2) > groundEnemy1.getPosition().x + groundEnemy1.getTexture().getWidth()) ){
                game.batch.draw(groundEnemy1.getTexture(), groundEnemy1.getPosition().x, groundEnemy1.getPosition().y);
                groundEnemy1.update(delta);
                runner.checkCollision(groundEnemy1);
            }else{
                groundEnemy1.isSpawned = false; //unrender enemy when off camera
            }
        }

        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);
        for(Ground ground: grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        //render first then logic, fixes shaking texture ??
        runner.update(delta);

        hud.meter.update(runner.getSpeed().x);
        hud.health.update();

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
        groundEnemy1.dispose();
    }
}
