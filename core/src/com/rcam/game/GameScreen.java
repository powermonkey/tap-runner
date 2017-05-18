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
import com.rcam.game.sprites.PowerUp;
import com.rcam.game.sprites.Runner;
import com.rcam.game.sprites.enemies.Enemy;
import com.rcam.game.sprites.enemies.FlyingEnemy;
import com.rcam.game.sprites.enemies.GroundEnemy;

import java.util.Random;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    final static float STARTING_X = 30;
    final static float STARTING_Y = 112;
    float spawnMarker = 50;
    float levelMarker = 0;
    float powerUpMarker = 350;
    static int levelCounter = 1;

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
    Array<PowerUp> powerUps, newPowerUps;
    long startingTime;

    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(STARTING_X, STARTING_Y);
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        groundEnemies = new Array<GroundEnemy>();
        newGroundEnemies = new Array<GroundEnemy>();
        flyingEnemies = new Array<FlyingEnemy>();
        newFlyingEnemies = new Array<FlyingEnemy>();
        powerUps = new Array<PowerUp>();
        newPowerUps = new Array<PowerUp>();
        enemy = new Enemy();
        rand = new Random();
        level = new Level();


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

        //spawn power up
        spawnPowerUp(delta);

        //set ground enemy position and render ground enemy
        if(runner.getPosition().x > levelMarker) {
            if(level.getLevelKey() == levelCounter){
                spawnEnemy(delta);
            }else if(levelCounter == 4){
                levelCounter = 1;
            }else{
                levelMarker = spawnMarker;
                levelCounter ++;
            }
        }

        //render ground
        for(Ground ground : grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        //set ground position
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }

        //render enemy
            renderEnemy(groundEnemies, delta);
            renderEnemy(flyingEnemies, delta);

        //render runner
        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);

        //render first then logic, fixes shaking texture ??
        runner.update(delta);

        //if runner ran out of health
        if(runner.isDead) {
            if(!runner.animatingDeath) {
                startingTime = millis();
                runner.death();
                if(runner.indicatePosition() > runner.getHighScore()){
                    runner.setHighScore(runner.indicatePosition());
                }
            }else {
                if(timeSinceMillis(startingTime) > 2000) {
                    game.setScreen(new GameOverScreen(this.game, runner));
                    dispose();
                }
            }
        }

        game.batch.end();
        hud.meter.update(runner.getSpeed().x);
        hud.health.update();
        hud.distance.update();
        hud.render();
    }

    private void spawnEnemy(float delta){
        int[] levelDetails;
        int type, spawnCount, pattern, monsterType;
        levelDetails = level.getLevelPattern(level.getPattern());
            type = levelDetails[0];
            spawnCount = levelDetails[1];
            pattern = levelDetails[2];
            monsterType = levelDetails[3];

//            if(spawnCount > 1 && pattern == 1){ // enemy bridge
//                if(type == 1){
//                    groundEnemies.add(new GroundEnemy(monsterType, spawnCount));
//                }else if(type == 2){
//                    flyingEnemies.add(new FlyingEnemy(monsterType, spawnCount));
//                }
//                spawnCount = 1;
//            }else{
                for(int i = 1; i <= spawnCount; i++){
                    if(type == 1){
                        groundEnemies.add(new GroundEnemy(monsterType));
                    }else if(type == 2){
                        flyingEnemies.add(new FlyingEnemy(monsterType));
                    }
                }
//            }

            if(type == 1) {
                //only set position for new spawned ground enemies
                for(int i = 1; i <= spawnCount; i++){
                    newGroundEnemies.add(groundEnemies.pop());
                }
                positionEnemy(newGroundEnemies, levelDetails);
                for(int i = 1; i <= spawnCount; i++){
                    groundEnemies.add(newGroundEnemies.pop());
                }
            }else if(type == 2){
                //only set position for new spawned flying enemies
                for(int i = 1; i <= spawnCount; i++){
                    newFlyingEnemies.add(flyingEnemies.pop());
                }
                positionEnemy(newFlyingEnemies, levelDetails);
                for(int i = 1; i <= spawnCount; i++){
                    flyingEnemies.add(newFlyingEnemies.pop());
                }
            }
            level.updatePattern();
    }

    private void positionEnemy(Array<? extends Enemy> enemies, int[] levelDetails){
        boolean enemyBridge = false, isVertical = false;
        int counter = 0;
        int spawnCount = levelDetails[1];
        Vector2 spawnPosition;

        for(Enemy enemy : enemies){
            spawnPosition = enemySpawnPosition(counter, enemy, levelDetails);
            if(levelDetails[1] > 1 && levelDetails[2] == 1) {
                enemy.isBridge = true;
                enemyBridge = true;
                enemy.setBridgeCount(levelDetails[1]);
            }else if(levelDetails[1] > 1 && levelDetails[2] == 2){
                isVertical = true;
            }else{
                enemy.isBridge = false;
            }

            enemy.setPosition(spawnPosition);
            enemy.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
            enemy.createOnTopBounds();
            enemy.isSpawned = true;
            if(counter == spawnCount - 1) {
                break;
            }
            counter = counter + 1;
        }

        spawnMarkerDistance(levelDetails[1], enemyBridge, isVertical, levelDetails[5]);
    }

    private void renderEnemy(Array<? extends Enemy> enemies, float delta){
        for (Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > enemy.getPosition().x + enemy.getTextureWidth())) {
                    enemy.stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = enemy.animation.getKeyFrame(enemy.stateTime, true);
                    enemy.update(delta);
//                    if(enemy.isBridge) {
//                        for(int x = 0; x < enemy.getBridgeCount(); x++){
//                            game.batch.draw(currentFrame, enemy.getPosition().x + (x * 32), enemy.getPosition().y);
//                        }
//                    } else {
                        game.batch.draw(currentFrame, enemy.getPosition().x, enemy.getPosition().y);
//                    }

                    enemy.checkCollision(runner);
                } else {
                    enemy.isSpawned = false; //unrender enemy when off camera
                }
            }
        }
    }

    private void spawnMarkerDistance(int spawnCount, boolean enemyBridge, boolean isVertical, int enemyDistance){
        if(enemyBridge && enemyDistance < 1) {
            spawnMarker += (230);
        }else if(isVertical && enemyDistance < 1){
            spawnMarker += 210;
        }else if(spawnCount > 1 && enemyDistance < 1) {
            spawnMarker += (240);
        }else if(spawnCount == 1 && enemyDistance < 1){
            if(level.getLevelKey() == 3){
                spawnMarker += 130;
            }else{
                spawnMarker += 150;
            }
        }else if(enemyDistance > 0){
            spawnMarker += ((enemyDistance - 1) * 32);
//            if(enemyDistance == 2){
//                spawnMarker += 31;
//            }else if(enemyDistance == 1){
//                spawnMarker += 0;
//            }
        }
    }

    //TODO refactor enemy spawn position
    private Vector2 enemySpawnPosition(int counter, Enemy enemy, int[] levelDetails){
        Vector2 spawnPosition = new Vector2();
        float heightAdjust = levelDetails[4];
        switch(levelDetails[2]){
            case 1:
//                pattern 1 horizontal + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + (counter * (enemy.getTextureWidth()));
                spawnPosition.y = STARTING_Y  + (heightAdjust * enemy.getTextureHeight());
                break;
            case 2:
//                pattern 2 vertical + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X;
                spawnPosition.y = STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + (counter * (enemy.getTextureHeight()));
                break;
            case 3:
//                pattern 3 diagonal leaning right + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + (counter * (enemy.getTextureWidth())) ;
                spawnPosition.y = STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + (counter * (enemy.getTextureHeight()));
                break;
            case 4:
//                pattern 4 diagonal leaning left + on ground
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + (counter * (enemy.getTextureWidth()));
                spawnPosition.y = (STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + enemy.getTextureHeight() * 2) - (counter * (enemy.getTextureHeight()));
                break;
            case 5:
//                pattern 5 diagonal leaning right + on ground ungrouped
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + ((3 * counter) * (enemy.getTextureWidth())) ;
                spawnPosition.y = STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + (counter * (enemy.getTextureHeight()));
                break;
            case 6:
//                pattern 6 diagonal leaning left + on ground ungrouped
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + ((3 * counter) * (enemy.getTextureWidth()));
                spawnPosition.y = (STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + enemy.getTextureHeight() * 2) - (counter * (enemy.getTextureHeight()));
                break;
            default:
                throw new IllegalArgumentException("No such pattern");
        }

        return spawnPosition;
    }

    private void spawnPowerUp(float delta){
        if(runner.getPosition().x > powerUpMarker ){
            int num = rand.nextInt(3) + 1;
            for(int i = 1; i <= num; i++){
                powerUps.add(new PowerUp());
            }

            for(int i = 1; i <= num; i++){
                newPowerUps.add(powerUps.pop());
            }

            int yFluc = rand.nextInt(130) + 1;
            int ctr = 1;
            for(PowerUp newPowerUp : newPowerUps){
                Vector2 spawnPowerUpPosition = new Vector2();
                newPowerUp.randomPowerUp();
                spawnPowerUpPosition.x = powerUpMarker + newPowerUp.SPAWN_OFFSET_X + (ctr * 25); //not grouped
                spawnPowerUpPosition.y = STARTING_Y + (yFluc > 32 ? yFluc : 0); //default y
                newPowerUp.setPosition(spawnPowerUpPosition, delta);
                newPowerUp.createBounds();//call setPosition() before createBounds(), cannot create bounds without position
                newPowerUp.isSpawned = true;
                ctr += 1;
            }

            for(int i = 1; i <= num; i++){
                powerUps.add(newPowerUps.pop());
            }

            if(num == 1){
                powerUpMarker += 130;
            }else if(num == 2) {
                powerUpMarker += 200;
            }else {
                powerUpMarker += 350;
            }
        }

        //render power up
        for(PowerUp powerUp : powerUps){
            if(powerUp.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > powerUp.getPosition().x + powerUp.getTextureRegion().getRegionWidth())) {
                    game.batch.draw(powerUp.getTextureRegion(), powerUp.getPosition().x, powerUp.getPosition().y);
                    runner.checkPowerUpCollision(powerUp);
                } else {
                    powerUp.isSpawned = false; //unrender powerup when off camera
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
        for(PowerUp pUp : powerUps){
            pUp.dispose();
        }
        for(GroundEnemy newGroundEnemy : newGroundEnemies){
            newGroundEnemy.dispose();
        }
        for(FlyingEnemy newFlyingEnemy : newFlyingEnemies){
            newFlyingEnemy.dispose();
        }
        for(PowerUp newPowerUp : newPowerUps){
            newPowerUp.dispose();
        }
    }
}
