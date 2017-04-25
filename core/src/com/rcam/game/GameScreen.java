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
import com.rcam.game.sprites.enemies.FlyingEnemy;
import com.rcam.game.sprites.enemies.GroundEnemy;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    final static int SPAWN_FLUCTUATION_COUNT = 3; // +1
    final static float STARTING_X = 30;
    final static float STARTING_Y = 112;
    float spawnMarker = 500;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;
    Hud hud;
    Array<GroundEnemy> groundEnemies;
    Array<FlyingEnemy> flyingEnemies;
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
        flyingEnemies = new Array<FlyingEnemy>();
        enemy = new Enemy();
        rand = new Random();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));

        hud = new Hud(runner);

        for(int i = 1; i <= 3; i++){
            groundEnemies.add(new GroundEnemy(1));
            flyingEnemies.add(new FlyingEnemy(1));
        }

    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        cam.position.x = runner.getPosition().x + 100;
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);

        //set ground position
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }

        //set ground enemy position and render ground enemy
        spawnEnemy(delta);
//      spawnFlyingEnemy(delta);

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

    private void spawnEnemy(float delta){
        //set ground enemy position
        if(runner.getPosition().x > spawnMarker ){
            int counter = 1;
            int spawnCount = rand.nextInt(SPAWN_FLUCTUATION_COUNT);
            spawnCount += 1;
            for(GroundEnemy groundEnemy : groundEnemies){
                Vector2 spawnPosition = new Vector2();
                spawnPosition.x = spawnMarker + groundEnemy.SPAWN_OFFSET_X + (counter * (groundEnemy.getTexture().getWidth() / 4 + groundEnemy.ENEMY_GAP));
                spawnPosition.y = STARTING_Y;
                groundEnemy.setPosition(spawnPosition);
                groundEnemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
                groundEnemy.isSpawned = true;
                if(counter == spawnCount)
                    break;
                counter = counter + 1;
            }

            spawnMarker += enemy.SPAWN_DISTANCE;

            counter = 1;
            spawnCount = rand.nextInt(SPAWN_FLUCTUATION_COUNT);
            spawnCount += 1;
            for(FlyingEnemy flyingEnemy : flyingEnemies){
                Vector2 spawnPosition = new Vector2();
                spawnPosition.x = spawnMarker + flyingEnemy.SPAWN_OFFSET_X;
                spawnPosition.y = (STARTING_Y - flyingEnemy.getTexture().getHeight()) + (counter * (flyingEnemy.getTexture().getWidth() / 4));
                flyingEnemy.setPosition(spawnPosition);
                flyingEnemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
                flyingEnemy.isSpawned = true;
                if(counter == spawnCount)
                    break;
                counter = counter + 1;
            }
            spawnMarker += enemy.SPAWN_DISTANCE;
        }

        renderEnemy(groundEnemies, delta);
        renderEnemy(flyingEnemies, delta);
    }

    private void renderEnemy(Array<? extends Enemy> enemies, float delta){
        for(Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > enemy.getPosition().x + enemy.getTexture().getWidth())) {
                    enemy.stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = enemy.animation.getKeyFrame(enemy.stateTime, true);
                    game.batch.draw(currentFrame, enemy.getPosition().x, enemy.getPosition().y);
                    enemy.update(delta);
                    runner.checkCollision(enemy);
                } else {
                    enemy.isSpawned = false; //unrender enemy when off camera
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
        for(FlyingEnemy flyingEnemy : flyingEnemies)
            flyingEnemy.dispose();
    }
}
