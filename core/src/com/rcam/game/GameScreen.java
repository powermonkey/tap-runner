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

        //select level and pattern
        //level 1: intro patterns
        //level 2: simple patterns
        //level 3: combo patterns

        //set ground enemy position and render ground enemy
        spawnEnemy(delta);

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
        //set enemy position
        if(runner.getPosition().x > spawnMarker ){
            positionEnemy(groundEnemies);
            positionEnemy(flyingEnemies);
        }

        //render enemy
        renderEnemy(groundEnemies, delta);
        renderEnemy(flyingEnemies, delta);
    }

    private void positionEnemy(Array<? extends Enemy> enemies){
        int counter = 1;
        int spawnCount = rand.nextInt(SPAWN_FLUCTUATION_COUNT);
        spawnCount += 1;
//        int spawnCount = 3;
        for(Enemy enemy : enemies){
            Vector2 spawnPosition = new Vector2();
            if(enemy instanceof GroundEnemy){
                spawnPosition = groundEnemySpawnPosition(counter, enemy);
            }else if(enemy instanceof FlyingEnemy){
                spawnPosition = flyingEnemySpawnPosition(counter, enemy);
            }
            enemy.setPosition(spawnPosition);
            enemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
            enemy.isSpawned = true;
            if(counter == spawnCount)
                break;
            counter = counter + 1;
        }

        spawnMarker += enemy.SPAWN_DISTANCE;
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

    private Vector2 groundEnemySpawnPosition(int counter, Enemy enemy){
        Vector2 spawnPosition = new Vector2();

//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 + enemy.getTexture().getWidth() / 4)); //not grouped
        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 )); //grouped
        spawnPosition.y = STARTING_Y; //default y
        return spawnPosition;
    }

    private Vector2 flyingEnemySpawnPosition(int counter, Enemy enemy){
        Vector2 spawnPosition = new Vector2();

//        pattern 1 horizontal + above ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
//        spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (enemy.getTexture().getWidth() / 4);

//        pattern 2 horizontal + on ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
//        spawnPosition.y = (STARTING_Y);

//        pattern 4 vertical + above ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X;
//        spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (counter * (enemy.getTexture().getWidth() / 4));

//        pattern 3 vertical + on ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X;
//        spawnPosition.y = (STARTING_Y - enemy.getTexture().getHeight()) + (counter * (enemy.getTexture().getWidth() / 4));

//        pattern 5 diagonal leaning right + above ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
//        spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (counter * (enemy.getTexture().getWidth() / 4));

//        pattern 5 diagonal leaning right + on ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
//        spawnPosition.y = (STARTING_Y - enemy.getTexture().getHeight()) + (counter * (enemy.getTexture().getWidth() / 4));

//        pattern 5 diagonal leaning left + on ground
//        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
//        spawnPosition.y = (STARTING_Y + enemy.getTexture().getHeight() * 3) - (counter * (enemy.getTexture().getWidth() / 4));

        //        pattern 5 diagonal leaning left + above ground
        spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
        spawnPosition.y = ((STARTING_Y + (enemy.getTexture().getHeight() / 4)) * 2) - (counter * (enemy.getTexture().getWidth() / 4));

        return spawnPosition;
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
