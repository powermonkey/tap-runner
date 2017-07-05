package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Lava;
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
    final float ENEMY_OFFSET_Y = 5;
    float spawnMarker = 50;
    float powerUpMarker = 350;
    float lastLavaPos = 0;
    float lastGroundPos = 0;
    static Preferences prefs;

    OrthographicCamera cam, bgCam;
    Runner runner;
    Array<Ground> grounds;
    Array<Lava> lavas;
    Hud hud;
    final Array<GroundEnemy> activeGroundEnemies;
    final Array<FlyingEnemy> activeFlyingEnemies;
    final Pool<GroundEnemy> groundEnemyPool;
    final Pool<FlyingEnemy> flyingEnemyPool;
    Random rand;
    Level level;
    final Array<PowerUp> powerUps;
    final Pool<PowerUp> powerUpPool;
    PowerUp powUp;
    long startingTime;
    KeyboardInput keys;
    String gameMode;
    private GroundEnemy groundEnemyOject;
    private FlyingEnemy flyingEnemyOject;
    Enemy enemyObject;
    boolean isPause, allLava;
    Vector2 spawnPosition;
    TextureAtlas.AtlasRegion bg;

    public GameScreen(final TapRunner gam){
        this.game = gam;

        bg = GameAssetLoader.atlas.findRegion("background");

        runner = new Runner();

        bgCam = new OrthographicCamera();
        bgCam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        bgCam.update();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        cam.update();

        isPause = false;

        grounds = new Array<Ground>();
        lavas = new Array<Lava>();

        enemyObject = new Enemy();
        groundEnemyOject = new GroundEnemy();
        flyingEnemyOject = new FlyingEnemy();

        activeGroundEnemies = new Array<GroundEnemy>();
        activeFlyingEnemies = new Array<FlyingEnemy>();

        groundEnemyPool = new Pool<GroundEnemy>() {
            @Override
            protected GroundEnemy newObject() {
                return new GroundEnemy();
            }
        };

        flyingEnemyPool = new Pool<FlyingEnemy>() {
            @Override
            protected FlyingEnemy newObject() {
                return new FlyingEnemy();
            }
        };

        powerUps = new Array<PowerUp>();
        powerUpPool = new Pool<PowerUp>() {
            @Override
            protected PowerUp newObject() {
                return new PowerUp();
            }
        };

        powUp = new PowerUp();
        rand = new Random();
        level = new Level();

        prefs = Gdx.app.getPreferences("TapRunner");

        if (!prefs.contains("GameMode")) {
            prefs.putString("GameMode", "Normal");
            prefs.flush();
        }

        gameMode = prefs.getString("GameMode");

        if(gameMode.equals("The Ground Is Lava")){
            allLava = false;
            lavas.add(new Lava(new Lava().getTextureLava().getRegionWidth() * 2));
            lavas.add(new Lava(new Lava().getTextureLava().getRegionWidth() * 3));
        }

        lastLavaPos = new Lava().getTextureLava().getRegionWidth() * 4;
        grounds.add(new Ground(0));
        grounds.add(new Ground(new Ground().getTextureGround().getRegionWidth()));
        lastGroundPos = new Ground().getTextureGround().getRegionWidth();
        hud = new Hud(gam, runner, this);
        keys = new KeyboardInput(runner);

        spawnPosition = new Vector2();
    }

    public void handleKeyboardInput() {
        Gdx.input.setInputProcessor(keys);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bgCam.update();

        cam.position.x = (int)runner.getPosition().x + 100;
        cam.update();

        //static background image
        game.batch.setProjectionMatrix(bgCam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 459);
        game.batch.end();

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();

        //spawn power up
        renderPowerUp();

        //render enemy
        renderEnemy(activeGroundEnemies);
        renderEnemy(activeFlyingEnemies);

        if (gameMode.equals("The Ground Is Lava")) {
            //render ground
            if(!allLava) {
                for (Ground ground : grounds) {
                    if (cam.position.x - (cam.viewportWidth / 2) < ground.getPosGround().x + ground.getTextureGround().getRegionWidth()) {
                        game.batch.draw(ground.getTextureGround(), (int) ground.getPosGround().x, (int) ground.getPosGround().y);
                    }
                }
            }

            //render lava
            for (Lava lava : lavas) {
                game.batch.draw(lava.getTextureLava(), (int)lava.getPosLava().x, (int)lava.getPosLava().y);
                //update lava bounds
                lava.update();
                //check runner landing on lava
                lava.checkLavaCollision(runner);
                if (cam.position.x - (cam.viewportWidth / 2) > lava.getPosLava().x + lava.getTextureLava().getRegionWidth()) {
                    allLava = true;
                    lava.repositionLava(lastLavaPos);
                    lastLavaPos = lava.getPosLava().x + (lava.getTextureLava().getRegionWidth());
                }
            }

//            for (Lava lava : lavas) {
//                if (cam.position.x - (cam.viewportWidth / 2) > lava.getPosLava().x + lava.getTextureLava().getRegionWidth()) {
//                    allLava = true;
//                    lava.repositionLava(lastLavaPos);
//                    lastLavaPos = lava.getPosLava().x + (lava.getTextureLava().getRegionWidth());
//                }
//            }
        } else {
            //render ground
            for (Ground ground : grounds) {
                game.batch.draw(ground.getTextureGround(), (int)ground.getPosGround().x, (int)ground.getPosGround().y);
                if (cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTextureGround().getRegionWidth()) {
                    ground.repositionGround(ground.getPosGround().x + (ground.getTextureGround().getRegionWidth() * 2));
                }
            }

            //set ground position
//            for (Ground ground : grounds) {
//                if (cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTextureGround().getRegionWidth()) {
//                    ground.repositionGround(ground.getPosGround().x + (ground.getTextureGround().getRegionWidth() * 2));
//                }
//            }
        }

        //render runner
        runner.stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentRunnerFrame;
        currentRunnerFrame = runner.animationFast.getKeyFrame(runner.stateTime, true);

        if(runner.isDead){
            game.batch.draw(runner.getRegionDeath(), (int)runner.getPosition().x, (int)runner.getPosition().y);
        }else if(runner.isIdle && !runner.isJumping){
            game.batch.draw(runner.getRegionStand(), (int)runner.getPosition().x, (int)runner.getPosition().y);
        }else if(!runner.isOnGround) {
            game.batch.draw(runner.getRegionJump(),(int) runner.getPosition().x, (int)runner.getPosition().y);
        }else if(!runner.isIdle) {
            game.batch.draw(currentRunnerFrame, (int)runner.getPosition().x, (int)runner.getPosition().y);
        }

        if(!isPause) {
            //set power up position and render power up
            if (runner.getPosition().x > powerUpMarker) {
                spawnPowerUp(delta);
            }

            //set enemy position and render enemy
            if (runner.getPosition().x > spawnMarker) {
                if(level.getLevelKey() == 1 && level.isEndOfLevel){
                    if(gameMode.equals("Normal")) {
                        runner.increaseSpeed();
                        runner.increaseJump();
                        runner.increaseGravity();
                        runner.run();
                    }else{
                        for (Lava lava : lavas) {
                            lava.increaseDamage();
                        }
                    }
                }
                spawnEnemy();
            }

            //update enemies
            updateEnemies(activeGroundEnemies, delta);
            updateEnemies(activeFlyingEnemies, delta);

            //render first then logic, fixes shaking texture ??
            runner.update(delta);

            //if runner ran out of health
            if (runner.isDead) {
                if (!runner.animatingDeath) {
                    startingTime = millis();
                    runner.death();
                    if (gameMode.equals("The Ground Is Lava")) {
                        if (runner.indicatePosition() > runner.getHighScoreLavaMode()) {
                            runner.setHighScoreLavaMode(runner.indicatePosition());
                        }
                    } else {
                        if (runner.indicatePosition() > runner.getHighScoreNormalMode()) {
                            runner.setHighScoreNormalMode(runner.indicatePosition());
                        }
                    }
                } else {
                    if (timeSinceMillis(startingTime) > 2000) {
                        game.setScreen(new GameOverScreen(this.game, runner));
                    }
                }
            }
//            hud.meter.update(runner.getSpeed().x);
            hud.health.update();
            hud.distance.update();

        }
        game.batch.end();
        hud.render();
//        handleKeyboardInput();
    }

    private void spawnEnemy(){
        int[] levelDetails;
        int type, spawnCount, monsterType, distance;
        levelDetails = level.getLevelPattern(level.getPattern());
        type = levelDetails[0];
        spawnCount = levelDetails[1];
        monsterType = levelDetails[3];
        distance = levelDetails[5];

        for(int i = 0; i <= spawnCount - 1; i++){
            Vector2 sPawnPos;
            if(type == 1){
                sPawnPos = enemySpawnPosition(i, enemyObject.SPAWN_OFFSET_FROM_CAM_X, groundEnemyOject.textureWidth, groundEnemyOject.textureHeight, levelDetails);
                GroundEnemy item = groundEnemyPool.obtain();
                item.init(monsterType, sPawnPos);
                activeGroundEnemies.add(item);
            }else if(type == 2){
                sPawnPos = enemySpawnPosition(i, enemyObject.SPAWN_OFFSET_FROM_CAM_X, flyingEnemyOject.getTextureWidth(), flyingEnemyOject.getTextureHeight(), levelDetails);
                FlyingEnemy item = flyingEnemyPool.obtain();
                item.init(monsterType, sPawnPos);
                activeFlyingEnemies.add(item);
            }else if (type == 0){
                spawnMarker += 200;
            }
            spawnPosition.set(0,0);
        }

        level.updatePattern();
        spawnMarkerDistance(distance);
    }

    private void updateEnemies(Array<? extends Enemy> enemies, float delta){
        for (Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (cam.position.x + cam.viewportWidth > enemy.getPosition().x + enemy.getTextureWidth()
                        && enemy.getPosition().x + enemy.getTextureWidth() > cam.position.x - cam.viewportWidth ) {
                    enemy.update(delta);
                }
            }
        }
    }

    private void renderEnemy(Array<? extends Enemy> enemies){
        for (Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (cam.position.x + cam.viewportWidth  > enemy.getPosition().x + enemy.getTextureWidth()
                        && cam.position.x - (cam.viewportWidth / 2) < enemy.getPosition().x + enemy.getTextureWidth()) {
                    enemy.stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = enemy.animation.getKeyFrame(enemy.stateTime, true);
                    game.batch.draw(currentFrame, (int)enemy.getPosition().x, (int)enemy.getPosition().y);
                    enemy.checkCollision(runner);
                } else if(cam.position.x - (cam.viewportWidth / 2) > enemy.getPosition().x + enemy.getTextureWidth()) {
                    enemy.isSpawned = false; //unspawn enemy when off camera
                }
            }
        }

        //TODO: refactor, put in a function;
        GroundEnemy groundEnemyItem;
        int groundEnemylen = activeGroundEnemies.size;
        for(int i = groundEnemylen; --i >= 0;){
            groundEnemyItem = activeGroundEnemies.get(i);
            if(!groundEnemyItem.isSpawned){
                activeGroundEnemies.removeIndex(i);
                groundEnemyPool.free(groundEnemyItem);
                activeGroundEnemies.removeValue(groundEnemyItem, true);
            }
        }

        FlyingEnemy flyingEnemyItem;
        int flyingEnemylen = activeFlyingEnemies.size;
        for(int i = flyingEnemylen; --i >= 0;){
            flyingEnemyItem = activeFlyingEnemies.get(i);
            if(!flyingEnemyItem.isSpawned){
                activeFlyingEnemies.removeIndex(i);
                flyingEnemyPool.free(flyingEnemyItem);
                activeFlyingEnemies.removeValue(flyingEnemyItem, true);
            }
        }
    }

    private void spawnMarkerDistance(int enemyDistance){
        spawnMarker += ((enemyDistance - 1) * 32);
    }

    //TODO refactor enemy spawn position
    private Vector2 enemySpawnPosition(int counter, float offset, float width, float height, int[] levelDetails){
        float heightAdjust = levelDetails[4];

        switch(levelDetails[2]){
            case 1:
//                pattern 1 horizontal (used for ground enemies mostly)
                spawnPosition.x = spawnMarker + offset + (counter * (width));
                spawnPosition.y = runner.STARTING_Y + (heightAdjust * (height + ENEMY_OFFSET_Y));
                break;
            case 2:
//                pattern 2 vertical
                spawnPosition.x = spawnMarker + offset;
                spawnPosition.y = runner.STARTING_Y + ENEMY_OFFSET_Y + (heightAdjust * height) + (counter * (height));
                break;
            case 3:
//                pattern 3 diagonal leaning right
                spawnPosition.x = spawnMarker + offset + (counter * (width));
                spawnPosition.y = runner.STARTING_Y + ENEMY_OFFSET_Y + (heightAdjust * height) + (counter * (height));
                break;
            case 4:
//                pattern 4 diagonal leaning left
                spawnPosition.x = spawnMarker + offset + (counter * (width));
                spawnPosition.y = (runner.STARTING_Y + ENEMY_OFFSET_Y + (heightAdjust * height) + height * 2) - (counter * (height));
                break;
            case 5:
//                pattern 5 diagonal leaning right ungrouped
                spawnPosition.x = spawnMarker + offset + ((2 * counter) * (width)) ;
                spawnPosition.y = runner.STARTING_Y + ENEMY_OFFSET_Y + (heightAdjust * height) + (counter * (height));
                break;
            case 6:
//                pattern 6 diagonal leaning left ungrouped
                spawnPosition.x = spawnMarker + offset + ((2 * counter) * (width));
                spawnPosition.y = (runner.STARTING_Y  + ENEMY_OFFSET_Y + (heightAdjust * height) + height * 2) - (counter * (height));
                break;
            default:
                throw new IllegalArgumentException("No such pattern");
        }

        return spawnPosition;
    }

    private void spawnPowerUp(float delta){
        int num = rand.nextInt(3) + 1;
        int yFluc = rand.nextInt(130) + 1;
        int ctr = 1;
        for(int i = 1; i <= num; i++){
            float x = (powerUpMarker + powUp.SPAWN_OFFSET_X + (ctr * 25));
            float y = (runner.STARTING_Y + (yFluc > 32 ? yFluc : 0));
            PowerUp powerUpItem = powerUpPool.obtain();
            powerUpItem.init(x,y);
            powerUps.add(powerUpItem);
            ctr += 1;
        }

        if(num == 1){
            powerUpMarker += 130;
        }else if(num == 2) {
            powerUpMarker += 200;
        }else {
            powerUpMarker += 350;
        }
    }

    public void renderPowerUp(){
        PowerUp powerUpItem;

        for(PowerUp powerUp : powerUps){
            if(powerUp.isSpawned) {
                if (cam.position.x + cam.viewportWidth > powerUp.getPosition().x + powerUp.getAtlasRegion().getRegionWidth()
                    && cam.position.x - (cam.viewportWidth / 2) < powerUp.getPosition().x + powerUp.getAtlasRegion().getRegionWidth()) {
                    powerUp.checkPowerUpCollision(runner);
                    game.batch.draw(powerUp.getAtlasRegion(), (int)powerUp.getPosition().x, (int)powerUp.getPosition().y);
                } else if(cam.position.x  - (cam.viewportWidth / 2) > powerUp.getPosition().x + powerUp.getAtlasRegion().getRegionWidth()){
                    powerUp.isSpawned = false; //unrender powerup when off camera
                }
            }
        }

        int len = powerUps.size;
        for(int i = len; --i >= 0;){
            powerUpItem = powerUps.get(i);
            if(!powerUpItem.isSpawned){
                powerUps.removeIndex(i);
                powerUpPool.free(powerUpItem);
                powerUps.removeValue(powerUpItem, true);
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
        GameAssetLoader.dispose();
        runner.dispose();
        hud.dispose();
    }
}
