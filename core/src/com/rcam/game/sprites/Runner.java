package com.rcam.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

/**
 * Created by Rod on 4/14/2017.
 */

public class Runner {
    static final float FRICTION = -1.5f;
//    static final float GRAVITY = -15;
    static final float GRAVITY = -20;
    static final float MAX_SPEED = 150;
//    static final float MAX_HEIGHT = 400;
    static final float MAX_HEIGHT = 450;
    static final int STARTING_HEALTH = 50;
    public static final float CONTACT_BOUNDS_OFFSET_Y = 4;
    public static final float CONTACT_BOUNDS_OFFSET_X = 1;
    public float health;
    private long startingTime, lavaDamageTimeStart;
    public boolean isMaintainHighSpeed, isOnGround, isJumping, isDead, animatingDeath, isFalling, isOnTopEnemy, isTouched, invulnerable, isIdle;
    Texture runnerTexture;
    Vector2 position, velocity, speed;
    TextureAtlas atlas;
    TextureAtlas.AtlasRegion regionStand, regionJump, regionDeath;
//    TextureRegion regionStand, regionJump, regionDeath, regionRun;
    Array<TextureAtlas.AtlasRegion> regionRun;
    public float groundLevel, tempGround;
    private Rectangle bounds, intersectionBounds;
    static Preferences prefs;
    public Animation<TextureRegion> animationSlow, animationNormal, animationFast;
    public float stateTime;

    public Runner(float x, float y){
        position = new Vector2(x, y);
        groundLevel = position.y;
        tempGround = groundLevel;
        velocity = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        isMaintainHighSpeed = false;
        isOnGround = true;
        isFalling = false;
        isTouched = false;
        invulnerable = false;
        isOnTopEnemy = false;
        isIdle = false;

        atlas = new TextureAtlas("packedimages/runner32.atlas");
        regionStand = atlas.findRegion("stand");
        regionRun = atlas.findRegions("run");
        regionJump = atlas.findRegion("jump");
        regionDeath = atlas.findRegion("death");
        bounds = new Rectangle(x - CONTACT_BOUNDS_OFFSET_X, y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight());
        intersectionBounds = new Rectangle(x - CONTACT_BOUNDS_OFFSET_X, y - CONTACT_BOUNDS_OFFSET_Y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight() - CONTACT_BOUNDS_OFFSET_Y); //intersection bounds

        animationSlow = new Animation<TextureRegion>(0.1f, regionRun);
        animationNormal = new Animation<TextureRegion>(0.08f, regionRun);
        animationFast = new Animation<TextureRegion>(0.03f, regionRun);

//        runnerTexture = new Texture("bird.png");
//        bounds = new Rectangle(x, y, runnerTexture.getWidth(), runnerTexture.getHeight());
//        intersectionBounds = new Rectangle(x, y - CONTACT_BOUNDS_OFFSET, runnerTexture.getWidth(), runnerTexture.getHeight()); //intersection bounds

        health = STARTING_HEALTH;
        startingTime = millis();

        prefs = Gdx.app.getPreferences("TapRunner");

        if (!prefs.contains("BestDistanceNormalMode")) {
            prefs.putInteger("BestDistanceNormalMode", 0);
            prefs.flush();
        }

        if (!prefs.contains("BestDistanceLavaMode")) {
            prefs.putInteger("BestDistanceLavaMode", 0);
            prefs.flush();
        }
    }

    public void setHighScoreNormalMode(int val) {
        prefs.putInteger("BestDistanceNormalMode", val);
        prefs.flush();
    }

    public void setHighScoreLavaMode(int val) {
        prefs.putInteger("BestDistanceLavaMode", val);
        prefs.flush();
    }

    public int getHighScoreNormalMode() {
        return prefs.getInteger("BestDistanceNormalMode");
    }

    public int getHighScoreLavaMode() {
        return prefs.getInteger("BestDistanceLavaMode");
    }

    public void update(float dt){
//        drainHealth();

        //slow down runner
//        speed.add(FRICTION, 0);

       //make runner come back to the ground
            speed.add(0, GRAVITY);

        //remove invulnerability
        if(invulnerable){
            if(timeSinceMillis(lavaDamageTimeStart) > 1000){
                lavaDamageTimeStart = millis();
                invulnerable = false;
            }
        }

        if(speed.x > 0) {
            isIdle = false;
        }else if(speed.x <= 0 && speed.y ==  -20){
            isIdle = true;
        }

       // make runner stop when reaching 0 speed
        if(speed.x < 0 ){
            speed.x = 0;
        }

        //if dead set velocity to immediate stop
        if(isDead){
            velocity.x = -600;
        }

        //maintain high speed
        speed.add(velocity.x, velocity.y);

        //determine falling state
        if(!isJumping) {
            velocity.y = 0;
        }
        if (speed.y < -40) {
            isJumping = false;
            isFalling = true;
        }
        //minimum fall height before isOnGround is set; allows jumping when on top enemy bridge
        if (speed.y <= -40) {
            isJumping = false;
            isOnGround = false;
        }
        //limit jump height
        if(speed.y > MAX_HEIGHT){
            speed.y = MAX_HEIGHT;
            isJumping = false;
        }

//        if(!isMaintainHighSpeed) {
        //limit speed to max speed
        if(speed.x >= MAX_SPEED){
            speed.x = MAX_SPEED;
        }

        //prevent speed.x from going negative
        if(speed.x < 0) {
            speed.x = 0;
        }
        position.add(speed.x * dt, speed.y * dt);

        //make runner land on ground
        if(position.y < tempGround && !isDead){
            position.y = tempGround;
            isOnGround = true;
            isJumping = false;
            isFalling = false;
            speed.y = 0;
        }else if(isDead){
            isOnGround = false;
        }

        //reset value of velocity x and y
        velocity.x = 0;
        if(!isJumping)
            velocity.y = 0;
        bounds.setPosition(position.x  - CONTACT_BOUNDS_OFFSET_X, position.y);
        intersectionBounds.setPosition(position.x  - CONTACT_BOUNDS_OFFSET_X, position.y - CONTACT_BOUNDS_OFFSET_Y);
    }

    private void drainHealth(){
        if(timeSinceMillis(startingTime) > 1000){
            startingTime = millis();
            health -= 1f;
            if(health <= 0){
                isDead = true;
            }
        }
    }

    public Texture getTexture() {
        return runnerTexture;
    }

    public TextureAtlas.AtlasRegion getRegionStand() {
        return regionStand;
    }

    public TextureAtlas.AtlasRegion getRegionDeath() {
        return regionDeath;
    }

    public TextureAtlas.AtlasRegion getRegionJump() {
        return regionJump;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPositionY(float y) {
        position.y = y;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocityX(float x) {
        velocity.x = x;
    }

    public void setVelocityY(float y) {
        velocity.y = y;
    }

    public void setLavaDamageTimeStart(long time) {
        lavaDamageTimeStart = time;
    }

    public void run(){
            velocity.x = 50;
    }

    public void slowDown(){
        velocity.x = -50;
    }

    public void jump(){
        velocity.y = 55;
        isJumping = true;
        isOnGround = false;
        isFalling = false;
    }

    public void lavaBounce(){
        velocity.y = 50;
        isJumping = true;
        isOnGround = false;
        isFalling = false;
    }

    public void death(){
        animatingDeath = true;
        velocity.y = 60;
        velocity.x = 0;
        isJumping = true;
        isOnGround = false;
    }

    public TextureRegion[] createFrames(Texture runner, int rows, int cols){
        TextureRegion[][] tmp = TextureRegion.split(runner,
                runner.getWidth() / cols,
                runner.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return frames;
    }

    public Vector2 getSpeed(){ return speed; }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getIntersectionBounds(){
        return intersectionBounds;
    }

    public float getHealth() {
        return health;
    }

    public int indicatePosition(){
        int s = Math.round(getPosition().x / 100);

        return s;
    }

    public void dispose(){
        atlas.dispose();
    }
}
