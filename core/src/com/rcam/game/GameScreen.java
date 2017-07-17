package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StringBuilder;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Lava;
import com.rcam.game.sprites.PowerUp;
import com.rcam.game.sprites.Runner;
import com.rcam.game.sprites.Smoke;
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
    static Preferences prefs;

    OrthographicCamera cam, bgCam;
    Runner runner;
    Ground ground1, ground2;
    Lava lava1, lava2;
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
    boolean isPause, groundRendered, groundDispose, lavaMode;
    Vector2 spawnPosition;
    TextureAtlas.AtlasRegion bg;
    Skin arcadeSkin;
    Smoke smoke;
    float viewportDiv2, viewportDiv4 ,tapRunnerWidthDiv2, tapRunnerHeightDiv2 ;
    StringBuilder distanceValue;
    BitmapFont distance;
    BitmapFontCache cacheFontDistance;

    GlyphLayout glyphLayout;

    public GameScreen(final TapRunner gam){
        this.game = gam;

        bg = GameAssetLoader.bg;

        runner = new Runner();

        bgCam = new OrthographicCamera();
        bgCam.setToOrtho(false, TapRunner.WIDTH, TapRunner.HEIGHT);

        tapRunnerWidthDiv2 = TapRunner.WIDTH * 0.5f;
        tapRunnerHeightDiv2 = TapRunner.HEIGHT * 0.5f;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, tapRunnerWidthDiv2, tapRunnerHeightDiv2 + 50);
        viewportDiv2 = cam.viewportWidth * 0.5f;
        viewportDiv4 = cam.viewportWidth * 0.25f;


        isPause = false;

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

        arcadeSkin = GameAssetLoader.arcadeSkin;

        prefs = Gdx.app.getPreferences("TapRunner");

        if (!prefs.contains("GameMode")) {
            prefs.putString("GameMode", "Normal");
            prefs.flush();
        }

        gameMode = prefs.getString("GameMode");
        if(gameMode.equals("The Ground Is Lava")) {
            lavaMode = true;
        }else{
            lavaMode = false;
        }

        if(lavaMode){
            groundRendered = false;
            groundDispose = false;
            lava1 = new Lava(new Lava().getTextureLava().getRegionWidth() * 2);
            lava2 = new Lava(new Lava().getTextureLava().getRegionWidth() * 3);
            smoke = new Smoke();
        }

        ground1 = new Ground(0);
        ground2 = new Ground(new Ground().getTextureGround().getRegionWidth());
        hud = new Hud(gam, runner, this);
//        keys = new KeyboardInput(runner);

        spawnPosition = new Vector2();
        distanceValue = new StringBuilder();
        distance = arcadeSkin.getFont("font");
        cacheFontDistance = new BitmapFontCache(distance);
        glyphLayout = new GlyphLayout();
    }

    public void handleKeyboardInput() {
        Gdx.input.setInputProcessor(keys);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.position.set((int)runner.getPosition().x + 100, 225, 0);
        cam.update();

//        game.batch.totalRenderCalls = 0;
        //static background image
        game.batch.setProjectionMatrix(bgCam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 199, TapRunner.WIDTH + 80, TapRunner.HEIGHT - 193.2f); //float for height; really odd; no pixelating from menu to game screen on desktop
        //update and render distance indicator
        cacheFontDistance.clear();
//        glyphLayout = cacheFontDistance.setText(getText(), 0 ,0);
        glyphLayout.setText(distance, getText());
        cacheFontDistance.setText(glyphLayout, (TapRunner.WIDTH - glyphLayout.width) * .5f, tapRunnerHeightDiv2 + 180);
//        glyphLayout.setText(cacheFontDistance, getText());
//        distance.draw(game.batch, glyphLayout, (TapRunner.WIDTH - glyphLayout.width) * .5f, tapRunnerHeightDiv2 + 180 );
        cacheFontDistance.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.enableBlending();
        //spawn power up
        renderPowerUp();

        //render enemy
        renderEnemy(delta);

        if (lavaMode) {
            //render ground
            if(!groundRendered) {
                //render ground
                if(cam.position.x - viewportDiv2 > ground1.getPosGround().x + ground1.getTextureGround().getRegionWidth()) {
                    ground1.repositionGround(ground1.getTextureGround().getRegionWidth() * 2);
                }
                if(cam.position.x - viewportDiv2 > ground2.getPosGround().x + ground2.getTextureGround().getRegionWidth()) {
                    ground2.repositionGround(ground2.getTextureGround().getRegionWidth() * 2);
                }
                game.batch.draw(ground1.getTextureGround(), ground1.getPosGround().x, ground1.getPosGround().y);
                game.batch.draw(ground2.getTextureGround(), ground2.getPosGround().x, ground2.getPosGround().y);
                groundDispose = true;
            }else if(groundDispose){
                //remove ground after showing
                ground1 = null;
                ground2 = null;
                groundDispose = false;
            }

            //render lava
            if(!isPause) {
                lava1.update();
                lava1.checkLavaCollision(runner, hud.health);

                lava2.update();
                lava2.checkLavaCollision(runner, hud.health);
            }

            if (cam.position.x - (viewportDiv2) > lava1.getPosLava().x + lava1.getTextureLava().getRegionWidth()) {
                groundRendered = true;
                lava1.repositionLava(lava1.getTextureLava().getRegionWidth() * 2);
            }

            if (cam.position.x - (viewportDiv2) > lava2.getPosLava().x + lava2.getTextureLava().getRegionWidth()) {
                groundRendered = true;
                lava2.repositionLava(lava2.getTextureLava().getRegionWidth() * 2);
            }

            game.batch.draw(lava1.getTextureLava(), lava1.getPosLava().x, lava1.getPosLava().y);
            game.batch.draw(lava2.getTextureLava(), lava2.getPosLava().x, lava2.getPosLava().y);
        } else {
            //render ground
            if(cam.position.x - viewportDiv2 > ground1.getPosGround().x + ground1.getTextureGround().getRegionWidth()) {
                ground1.repositionGround(ground1.getTextureGround().getRegionWidth() * 2);
            }
            if(cam.position.x - viewportDiv2 > ground2.getPosGround().x + ground2.getTextureGround().getRegionWidth()) {
                ground2.repositionGround(ground2.getTextureGround().getRegionWidth() * 2);
            }
            game.batch.draw(ground1.getTextureGround(), ground1.getPosGround().x, ground1.getPosGround().y);
            game.batch.draw(ground2.getTextureGround(), ground2.getPosGround().x, ground2.getPosGround().y);
        }

        //render runner
        runner.stateTime += delta;
        TextureRegion currentRunnerFrame = runner.animationFast.getKeyFrame(runner.stateTime, true);

        if(runner.isDead){
            game.batch.draw(runner.getRegionDeath(), (int)runner.getPosition().x, (int)runner.getPosition().y);
        }else if(runner.isIdle && !runner.isJumping){
            game.batch.draw(runner.getRegionStand(), (int)runner.getPosition().x, (int)runner.getPosition().y);
        }else if(!runner.isOnGround) {
            game.batch.draw(runner.getRegionJump(), (int)runner.getPosition().x, (int)runner.getPosition().y);
        }else if(!runner.isIdle) {
            game.batch.draw(currentRunnerFrame, (int)runner.getPosition().x, (int)runner.getPosition().y);
        }

        if(runner.isSmoking) {
            renderSmoke(delta);
        }

        if(!isPause) {
            //set power up position and render power up
            if (runner.getPosition().x > powerUpMarker) {
                spawnPowerUp();
            }

            //set enemy position and render enemy
            if (runner.getPosition().x > spawnMarker) {
                if(level.getLevelKey() == 1 && level.isEndOfLevel){
                    if(!lavaMode) {
                        runner.increaseSpeed();
                        runner.increaseJump();
                        runner.increaseGravity();
                        runner.run();
                    }else{
                        lava1.increaseDamage();
                        lava2.increaseDamage();
                    }
                }
                spawnEnemy();
            }

            //render first then logic, fixes shaking texture ??
            runner.update(delta);

            //if runner ran out of health
            if (runner.isDead) {
                if (!runner.animatingDeath) {
                    startingTime = millis();
                    runner.death();
                    if (lavaMode) {
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
                        dispose();
                    }
                }
            }
        }
        game.batch.end();

        hud.render(delta);
//        System.out.println(game.batch.maxSpritesInBatch);
//        handleKeyboardInput();
    }

    private void spawnEnemy(){
        int[] levelDetails;
        int type, spawnCount, monsterType, distance , pattern, heightAdjust;
        levelDetails = level.getLevelPattern(level.getPattern());
        type = levelDetails[0];
        spawnCount = levelDetails[1];
        pattern = levelDetails[2];
        monsterType = levelDetails[3];
        heightAdjust = levelDetails[4];
        distance = levelDetails[5];

        for(int i = 0; i <= spawnCount - 1; i++){
            Vector2 sPawnPos;
            if(type == 1){
                sPawnPos = enemySpawnPosition(i, enemyObject.SPAWN_OFFSET_FROM_CAM_X, groundEnemyOject.getTextureWidth(), groundEnemyOject.getTextureHeight(), pattern, heightAdjust);
                GroundEnemy item = groundEnemyPool.obtain();
                item.init(monsterType, sPawnPos);
                activeGroundEnemies.add(item);
            }else if(type == 2){
                sPawnPos = enemySpawnPosition(i, enemyObject.SPAWN_OFFSET_FROM_CAM_X, flyingEnemyOject.getTextureWidth(), flyingEnemyOject.getTextureHeight(), pattern, heightAdjust);
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

    private void renderEnemy(float delta){
        GroundEnemy groundEnemyRenderItem;
        for(int i = activeGroundEnemies.size; --i >= 0;){
            groundEnemyRenderItem = activeGroundEnemies.get(i);
            if(groundEnemyRenderItem.isSpawned){
                if (cam.position.x + 20 + viewportDiv2 > groundEnemyRenderItem.getPosition().x + groundEnemyRenderItem.getTextureWidth()
                        && cam.position.x - 50 - (viewportDiv4) < groundEnemyRenderItem.getPosition().x + groundEnemyRenderItem.getTextureWidth()) {
                    groundEnemyRenderItem.stateTime += delta;
                    TextureRegion currentFrame = groundEnemyRenderItem.animation.getKeyFrame(groundEnemyRenderItem.stateTime, true);
                    game.batch.draw(currentFrame, (int)groundEnemyRenderItem.getPosition().x, (int)groundEnemyRenderItem.getPosition().y);
                    groundEnemyRenderItem.checkCollision(runner, hud.health);
                    if(!isPause) {
                        groundEnemyRenderItem.update(delta);
                    }
                } else if(cam.position.x - 50 - (viewportDiv4) > groundEnemyRenderItem.getPosition().x + groundEnemyRenderItem.getTextureWidth()) {
                    groundEnemyRenderItem.isSpawned = false; //unspawn enemy when off camera
                }
            }
        }
        FlyingEnemy flyingEnemyRenderItem;
        for(int i = activeFlyingEnemies.size; --i >= 0;){
            flyingEnemyRenderItem = activeFlyingEnemies.get(i);
            if(flyingEnemyRenderItem.isSpawned){
                if (cam.position.x + 20 + viewportDiv2 > flyingEnemyRenderItem.getPosition().x + flyingEnemyRenderItem.getTextureWidth()
                        && cam.position.x - 50 - (viewportDiv4) < flyingEnemyRenderItem.getPosition().x + flyingEnemyRenderItem.getTextureWidth()) {
                    flyingEnemyRenderItem.stateTime += delta;
                    TextureRegion currentFrame = flyingEnemyRenderItem.animation.getKeyFrame(flyingEnemyRenderItem.stateTime, true);
                    game.batch.draw(currentFrame, (int)flyingEnemyRenderItem.getPosition().x, (int)flyingEnemyRenderItem.getPosition().y);
                    flyingEnemyRenderItem.checkCollision(runner, hud.health);
                    if(!isPause) {
                        flyingEnemyRenderItem.update(delta);
                    }
                } else if(cam.position.x - 50 - (viewportDiv4) > flyingEnemyRenderItem.getPosition().x + flyingEnemyRenderItem.getTextureWidth()) {
                    flyingEnemyRenderItem.isSpawned = false; //unspawn enemy when off camera
                }
            }
        }

        //TODO: refactor, put in a function;
        GroundEnemy groundEnemyItem;
        for(int i = activeGroundEnemies.size; --i >= 0;){
            groundEnemyItem = activeGroundEnemies.get(i);
            if(!groundEnemyItem.isSpawned){
                activeGroundEnemies.removeIndex(i);
                groundEnemyPool.free(groundEnemyItem);
            }
        }

        FlyingEnemy flyingEnemyItem;
        for(int i = activeFlyingEnemies.size; --i >= 0;){
            flyingEnemyItem = activeFlyingEnemies.get(i);
            if(!flyingEnemyItem.isSpawned){
                activeFlyingEnemies.removeIndex(i);
                flyingEnemyPool.free(flyingEnemyItem);
            }
        }

    }

    private void spawnMarkerDistance(int enemyDistance){
        spawnMarker += ((enemyDistance - 1) * 32);
    }

    //TODO refactor enemy spawn position
    private Vector2 enemySpawnPosition(int counter, float offset, float width, float height, int pattern, float heightAdjust){
        switch(pattern){
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

    private void spawnPowerUp(){
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

        for(int i = powerUps.size; --i >= 0;){
            powerUpItem = powerUps.get(i);
            if(powerUpItem.isSpawned){
                if (cam.position.x + 20 + viewportDiv2> powerUpItem.getPosition().x + powerUpItem.getAtlasRegion().getRegionWidth()
                        && cam.position.x - 50 - (viewportDiv4) < powerUpItem.getPosition().x + powerUpItem.getAtlasRegion().getRegionWidth()) {
                    powerUpItem.checkPowerUpCollision(runner, hud.health);
                    game.batch.draw(powerUpItem.getAtlasRegion(), powerUpItem.getPosition().x, powerUpItem.getPosition().y);
                } else if(cam.position.x  - 50 - (viewportDiv4) > powerUpItem.getPosition().x + powerUpItem.getAtlasRegion().getRegionWidth()){
                    powerUpItem.isSpawned = false; //unrender powerup when off camera
                }
            }
        }

        for(int i = powerUps.size; --i >= 0;){
            powerUpItem = powerUps.get(i);
            if(!powerUpItem.isSpawned){
                powerUps.removeIndex(i);
                powerUpPool.free(powerUpItem);
            }
        }

    }

    public void renderSmoke(float delta){
        smoke.stateTime += delta;
        TextureRegion currentSmokeFrameSlow = smoke.smokeAnimationSlow.getKeyFrame(smoke.stateTime, true);
        TextureRegion currentSmokeFrameFast = smoke.smokeAnimationFast.getKeyFrame(smoke.stateTime, true);
        game.batch.draw(currentSmokeFrameSlow, (int)runner.getPosition().x - 20, (int)runner.getPosition().y - 10);
        game.batch.draw(currentSmokeFrameFast, (int)runner.getPosition().x - 30, (int)runner.getPosition().y + 20);
    }

    public StringBuilder getText(){
        distanceValue.delete(0, distanceValue.length());
        distanceValue.append(runner.indicatePosition()).append(" m");
        return distanceValue;
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
        distance.dispose();
        hud.dispose();
    }
}
