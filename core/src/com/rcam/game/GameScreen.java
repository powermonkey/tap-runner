package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Runner;
import com.rcam.game.sprites.enemies.Enemy;
import com.rcam.game.sprites.enemies.GroundEnemy;

import java.util.Random;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    final static int SPAWN_FLUCTUATION_COUNT = 3; // +1
    final static float STARTING_X = 30;
    final static float STARTING_Y = 112;
    float spawnMarker = 1000;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;
    Hud hud;
    Array<GroundEnemy> groundEnemies;
    Random rand;
    Enemy enemy;

    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(STARTING_X, STARTING_Y);
        cam = new OrthographicCamera();
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        groundEnemies = new Array<GroundEnemy>();
        enemy = new Enemy();
        rand = new Random();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));

        hud = new Hud(runner);

        for(int i = 1; i <= 3; i++){
            groundEnemies.add(new GroundEnemy(1));
        }
    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        cam.position.x = runner.getPosition().x + 100;

        //set ground position
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);

        //set ground enemy position and render ground enemy
        spawnGroundEnemy(delta);

        //render runner
        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);

        //render ground
        for(Ground ground : grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        //render first then logic, fixes shaking texture ??
        runner.update(delta);

        hud.meter.update(runner.getSpeed().x);
        hud.health.update();

        game.batch.end();
        hud.render();
    }

    private void spawnGroundEnemy(float delta){
        if(runner.getPosition().x > spawnMarker ){
            int counter = 1;
            int spawnCount = rand.nextInt(SPAWN_FLUCTUATION_COUNT);
            spawnCount += 1;
            for(GroundEnemy groundEnemy : groundEnemies){
                Vector2 spawnPosition = new Vector2();
                spawnPosition.x = spawnMarker + groundEnemy.SPAWN_OFFSET_X + (counter * (groundEnemy.getTexture().getWidth() / 4 + groundEnemy.GROUND_ENEMY_GAP));
                spawnPosition.y = STARTING_Y;
                groundEnemy.setPosition(spawnPosition);
                groundEnemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position

                groundEnemy.isSpawned = true;
                if(counter == spawnCount)
                    break;
                counter = counter + 1;
            }
            spawnMarker += enemy.SPAWN_DISTANCE;
        }

        //render enemy
        for(GroundEnemy groundEnemy : groundEnemies) {
            if (groundEnemy.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > groundEnemy.getPosition().x + groundEnemy.getTexture().getWidth())) {
                    groundEnemy.stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = groundEnemy.walkAnimation.getKeyFrame(groundEnemy.stateTime, true);
                    game.batch.draw(currentFrame, groundEnemy.getPosition().x, groundEnemy.getPosition().y);
                    groundEnemy.update(delta);
                    runner.checkCollision(groundEnemy);
                } else {
                    groundEnemy.isSpawned = false; //unrender enemy when off camera
                }
            }
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
        runner.dispose();
        grnd.dispose();
        for(Ground ground : grounds)
            ground.dispose();
        hud.dispose();
        for(GroundEnemy groundEnemy : groundEnemies)
            groundEnemy.dispose();
    }
}
