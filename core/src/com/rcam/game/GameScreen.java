package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
    float levelMarker = 50;
    float powerUpMarker = 350;
    float lastLavaPos = 0;
    float lastGroundPos = 0;
    static int levelCounter = 1;
    static Preferences prefs;
    boolean noLava = false;

    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds, lavas;
    Hud hud;
    Array<GroundEnemy> groundEnemies, newGroundEnemies;
    Array<FlyingEnemy> flyingEnemies, newFlyingEnemies;
    Random rand;
    Level level;
    Array<PowerUp> powerUps, newPowerUps;
    PowerUp powUp;
    long startingTime;
    KeyboardInput keys;
    String gameMode;


    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(STARTING_X, STARTING_Y);
        cam = new OrthographicCamera();
//        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        cam.setToOrtho(false, TapRunner.WIDTH, TapRunner.HEIGHT);
        cam.update();

        grounds = new Array<Ground>();
        lavas = new Array<Ground>();
        groundEnemies = new Array<GroundEnemy>();
        newGroundEnemies = new Array<GroundEnemy>();
        flyingEnemies = new Array<FlyingEnemy>();
        newFlyingEnemies = new Array<FlyingEnemy>();
        powerUps = new Array<PowerUp>();
        newPowerUps = new Array<PowerUp>();
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
            lavas.add(new Ground(new Ground().getTexture().getWidth() * 2, gameMode.equals("The Ground Is Lava")));
            lavas.add(new Ground(new Ground().getTexture().getWidth() * 3, gameMode.equals("The Ground Is Lava")));
        }

        grounds.add(new Ground(0));
        grounds.add(new Ground(new Ground().getTexture().getWidth()));

        hud = new Hud(runner);
        keys = new KeyboardInput(runner);

    }

    public void handleInput() {
        Gdx.input.setInputProcessor(keys);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        cam.position.set(runner.getPosition().x + 100, 200, 0);
        cam.position.x = runner.getPosition().x + 100;
        cam.update();

        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);

        //spawn power up
        renderPowerUp();
        if(runner.getPosition().x > powerUpMarker ) {
            spawnPowerUp(delta);
        }

        //set ground enemy position and render ground enemy
        if(runner.getPosition().x > levelMarker) {
            if(level.getLevelKey() == levelCounter){
                spawnEnemy();
            }else if(levelCounter == 4){
                //shuffle level patterns
                //drain life
                //increase enemy damage
                levelCounter = 1;
            }else{
                levelMarker = spawnMarker;
                levelCounter ++;
            }
        }


        //render lava
        if(gameMode.equals("The Ground Is Lava")){
            //render ground
            for(Ground ground : grounds){
                game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
            }

            //render lava
            for (Ground lava : lavas) {
                game.batch.draw(lava.getLavaTexture(), lava.getPosLava().x, lava.getPosLava().y);
            }

            //levelMarker changes when runner not moving in beginning
            //System.out.println(runner.getPosition().x +" "+ levelMarker + 30);
            if(runner.getPosition().x > levelMarker) {
                noLava = true;
                //set ground position

                //ground.getPosGround().x is the same for each ground because of lastLavaPos, fix: remove cam.position condition?
                int groundCount = 0;
                for(Ground ground: grounds){
                    if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() ) {
                        ground.repositionGround(lastLavaPos + (groundCount * ground.getTexture().getWidth()) - ground.getTexture().getWidth() );
                        lastGroundPos = lastLavaPos + (groundCount * ground.getTexture().getWidth() - ground.getTexture().getWidth());
                        groundCount = groundCount + 1;
                System.out.println("ground lastGroundPos: " +lastGroundPos+ " lastLavaPos: " +lastLavaPos + " ground.getPosGround().x: " +ground.getPosGround().x+" levelMarker: "+levelMarker);
                    }
                }
            }else{
                int lavaCount = 0;
                if(noLava){
                    noLava = false;
                    for (Ground lava : lavas) {
                        lava.positionGroundX(lastGroundPos + lava.getLavaTexture().getWidth() + (lavaCount * lava.getLavaTexture().getWidth()));
                        lavaCount = lavaCount + 1;
                    }
                }

                for (Ground lava : lavas) {
                    if (cam.position.x - (cam.viewportWidth / 2) > lava.getPosLava().x + lava.getLavaTexture().getWidth()) {
                        lava.repositionLava(lava.getPosLava().x + (lava.getLavaTexture().getWidth() * 2));
                        lastLavaPos = lava.getPosLava().x + (lava.getLavaTexture().getWidth() * 2) ;
                System.out.println("lava lastGroundPos: "+lastGroundPos + " lastLavaPos: " + lastLavaPos+" lava.getPosLava().x: "+lava.getPosLava().x+" levelMarker: "+levelMarker);
                    }
                }
            }
        }else{
            //render ground
            for(Ground ground : grounds){
                game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
            }

            //set ground position
            for(Ground ground: grounds){
                if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() ) {
                    ground.repositionGround(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
                    System.out.println(ground.getPosGround().x);
                }
            }
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
//                    game.setScreen(new GameOverScreen(this.game, runner));
                    game.setScreen(new GameScreen(this.game));
                    dispose();
                }
            }
        }

        game.batch.end();
        hud.meter.update(runner.getSpeed().x);
        hud.health.update();
        hud.distance.update();
        hud.render();
        handleInput();
    }

    private void spawnEnemy(){
        int[] levelDetails;
        int type, spawnCount, pattern, monsterType, distance;
        boolean enemyBridge = false, isVertical = false;
        levelDetails = level.getLevelPattern(level.getPattern());
        type = levelDetails[0];
        spawnCount = levelDetails[1];
        pattern = levelDetails[2];
        monsterType = levelDetails[3];
        distance = levelDetails[5];

        for(int i = 0; i <= spawnCount - 1; i++){
            Vector2 spawnPosition;
            if(type == 1){
                spawnPosition = enemySpawnPosition(i, new GroundEnemy(monsterType), levelDetails);
                groundEnemies.add(new GroundEnemy(monsterType, spawnPosition, levelDetails));
            }else if(type == 2){
                spawnPosition = enemySpawnPosition(i, new FlyingEnemy(monsterType), levelDetails);
                flyingEnemies.add(new FlyingEnemy(monsterType, spawnPosition, levelDetails));
            }else if (type == 0){
                spawnMarker += 200;
            }
        }

        if(spawnCount > 1 && pattern == 1) {
            enemyBridge = true;
        }else if(spawnCount > 1 && pattern == 2){
            isVertical = true;
        }

        level.updatePattern();
        spawnMarkerDistance(spawnCount, enemyBridge, isVertical, distance);
    }


    private void renderEnemy(Array<? extends Enemy> enemies, float delta){
        for (Enemy enemy : enemies) {
            if (enemy.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > enemy.getPosition().x + enemy.getTextureWidth())) {
                    enemy.stateTime += Gdx.graphics.getDeltaTime();
                    TextureRegion currentFrame = enemy.animation.getKeyFrame(enemy.stateTime, true);
                    game.batch.draw(currentFrame, enemy.getPosition().x, enemy.getPosition().y);
                    enemy.update(delta);
                    enemy.checkCollision(runner);
                } else {
                    enemy.isSpawned = false; //unrender enemy when off camera
                    enemy.dispose();
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
//                pattern 3 diagonal leaning right
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + (counter * (enemy.getTextureWidth())) ;
                spawnPosition.y = STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + (counter * (enemy.getTextureHeight()));
                break;
            case 4:
//                pattern 4 diagonal leaning left
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + (counter * (enemy.getTextureWidth()));
                spawnPosition.y = (STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + enemy.getTextureHeight() * 2) - (counter * (enemy.getTextureHeight()));
                break;
            case 5:
//                pattern 5 diagonal leaning right ungrouped
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + ((3 * counter) * (enemy.getTextureWidth())) ;
                spawnPosition.y = STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + (counter * (enemy.getTextureHeight()));
                break;
            case 6:
//                pattern 6 diagonal leaning left ungrouped
                spawnPosition.x = spawnMarker + enemy.SPAWN_OFFSET_FROM_CAM_X + ((3 * counter) * (enemy.getTextureWidth()));
                spawnPosition.y = (STARTING_Y + (heightAdjust * enemy.getTextureHeight()) + enemy.getTextureHeight() * 2) - (counter * (enemy.getTextureHeight()));
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
            Vector2 spawnPowerUpPosition = new Vector2();
            spawnPowerUpPosition.x = (powerUpMarker + powUp.SPAWN_OFFSET_X + (ctr * 25)); //not grouped
            spawnPowerUpPosition.y = (STARTING_Y + (yFluc > 32 ? yFluc : 0)); //default y
            powerUps.add(new PowerUp(spawnPowerUpPosition.x, spawnPowerUpPosition.y));
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
        for(PowerUp powerUp : powerUps){
            if(powerUp.isSpawned) {
                if (!(cam.position.x - (cam.viewportWidth / 2) > powerUp.getPosition().x + powerUp.getTextureRegion().getRegionWidth())) {
                    runner.checkPowerUpCollision(powerUp);
                    game.batch.draw(powerUp.getTextureRegion(), powerUp.getPosition().x, powerUp.getPosition().y);
                } else {
                    powerUp.isSpawned = false; //unrender powerup when off camera
                    powerUp.dispose();
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
