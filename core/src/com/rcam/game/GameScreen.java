package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
    final static float STARTING_X = 30;
    final static float STARTING_Y = 112;
    float spawnMarker = 500;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;
    Hud hud;
    Array<GroundEnemy> groundEnemies, newGroundEnemies;
    Array<FlyingEnemy> flyingEnemies, newFlyingEnemies;
    Random rand;
    Enemy enemy;
    Level level;

    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(STARTING_X, STARTING_Y);
        cam = new OrthographicCamera();
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        groundEnemies = new Array<GroundEnemy>();
        newGroundEnemies = new Array<GroundEnemy>();
        flyingEnemies = new Array<FlyingEnemy>();
        newFlyingEnemies = new Array<FlyingEnemy>();
        enemy = new Enemy();
        rand = new Random();
        level = new Level();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));

        hud = new Hud(runner);

    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        hud.distance.update();

        game.batch.end();
        hud.render();
    }

    private void spawnEnemy(float delta){
        int[] levelDetails;


        //set enemy position
        if(runner.getPosition().x > spawnMarker ){

            for(int i = 1; i <= 3; i++){
                groundEnemies.add(new GroundEnemy(1));
                flyingEnemies.add(new FlyingEnemy(1));
            }

            levelDetails = level.getLevelPattern(level.getPattern());
            if(levelDetails[0] == 1) {
                //only set position for new spawned ground enemies
                for(int i = 1; i <= 3; i++){
                    newGroundEnemies.add(groundEnemies.pop());
                }
                positionEnemy(newGroundEnemies, levelDetails);
                for(int i = 1; i <= 3; i++){
                    groundEnemies.add(newGroundEnemies.pop());
                }
            }else {
                //only set position for new spawned ground enemies
                for(int i = 1; i <= 3; i++){
                    newFlyingEnemies.add(flyingEnemies.pop());
                }
                positionEnemy(newFlyingEnemies, levelDetails);
                for(int i = 1; i <= 3; i++){
                    flyingEnemies.add(newFlyingEnemies.pop());
                }
            }

            level.updatePattern();

        }

        //render enemy
        renderEnemy(groundEnemies, delta);
        renderEnemy(flyingEnemies, delta);
    }

    private void positionEnemy(Array<? extends Enemy> enemies, int[] levelDetails){
        boolean vertical = false;
        int counter = 1;
        int spawnCount = levelDetails[1];
        for(Enemy enemy : enemies){
            Vector2 spawnPosition = new Vector2();
            if(enemy instanceof GroundEnemy){
                spawnPosition = groundEnemySpawnPosition(counter, enemy, levelDetails);
            }else if(enemy instanceof FlyingEnemy){
                spawnPosition = flyingEnemySpawnPosition(counter, enemy, levelDetails);
            }
            enemy.setPosition(spawnPosition);
            enemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
            enemy.isSpawned = true;
            if(counter == spawnCount)
                break;
            counter = counter + 1;
        }
        if(levelDetails[2] == 3 || levelDetails[2] == 4)
             vertical = true;
        spawnMarkerDistance(levelDetails[1], vertical);
    }

    private void renderEnemy(Array<? extends Enemy> enemies, float delta){
        for(Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > enemy.getPosition().x + enemy.getTexture().getWidth() + 500)) {
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

    private Vector2 groundEnemySpawnPosition(int counter, Enemy enemy, int[] levelDetails){
        Vector2 spawnPosition = new Vector2();

        switch(levelDetails[2]){
            case 1:
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 + enemy.getTexture().getWidth() / 4)); //not grouped
                spawnPosition.y = STARTING_Y; //default y
                break;
            case 2:
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 )); //grouped
                spawnPosition.y = STARTING_Y; //default y
                break;
            default:
                throw new IllegalArgumentException("No such pattern");
        }

        return spawnPosition;
    }

    private Vector2 flyingEnemySpawnPosition(int counter, Enemy enemy, int[] levelDetails){
        Vector2 spawnPosition = new Vector2();

        switch(levelDetails[2]){
            case 1:
//                pattern 1 horizontal + above ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (enemy.getTexture().getWidth() / 4);
                break;
            case 2:
//                pattern 2 horizontal + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = (STARTING_Y);
                break;
            case 3:
//                pattern 3 vertical + above ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X;
                spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (counter * (enemy.getTexture().getWidth() / 4));
                break;
            case 4:
//                pattern 4 vertical + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X;
                spawnPosition.y = (STARTING_Y - enemy.getTexture().getHeight()) + (counter * (enemy.getTexture().getWidth() / 4));
                break;
            case 5:
//                pattern 5 diagonal leaning right + above ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = (STARTING_Y - (enemy.getTexture().getHeight() / 4)) + (counter * (enemy.getTexture().getWidth() / 4));
                break;
            case 6:
//                pattern 6 diagonal leaning right + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = (STARTING_Y - enemy.getTexture().getHeight()) + (counter * (enemy.getTexture().getWidth() / 4));
                break;
            case 7:
//                pattern 7 diagonal leaning left + above ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = ((STARTING_Y + (enemy.getTexture().getHeight() / 4)) * 2) - (counter * (enemy.getTexture().getWidth() / 4));
                break;
            case 8:
//                pattern 8 diagonal leaning left + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_X + (counter * (enemy.getTexture().getWidth() / 4 ));
                spawnPosition.y = (STARTING_Y + enemy.getTexture().getHeight() * 3) - (counter * (enemy.getTexture().getWidth() / 4));
                break;
            default:
                throw new IllegalArgumentException("No such pattern");
        }

        return spawnPosition;
    }

    private float spawnMarkerDistance(int spawnCount, boolean vertical){
        if(vertical){
            spawnMarker += 180;
        }else if(spawnCount > 1) {
            spawnMarker += 250;
        }else {
            spawnMarker += 160;
        }

        return spawnMarker;
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
