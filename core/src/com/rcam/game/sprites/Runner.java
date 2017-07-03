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
import com.rcam.game.GameAssetLoader;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

/**
 * Created by Rod on 4/14/2017.
 */

public class Runner {
    static final float FRICTION = -1.5f;
    static final float GRAVITY = -23;
    public float MAX_SPEED = 150;
    public float MIN_SPEED = 50;
    static float RUN_SPEED = 50;
    public final static float STARTING_X = 30;
    public final static float STARTING_Y = 112;
    static final float JUMP_HEIGHT = 525;
    static final int STARTING_HEALTH = 50;
    public static final float CONTACT_BOUNDS_OFFSET_Y = 4;
    public static final float CONTACT_BOUNDS_OFFSET_X = 1;
    public float health;
    private long startingTime, lavaDamageTimeStart;
    public boolean isMaintainHighSpeed, isOnGround, isJumping, isDead, animatingDeath, isFalling, isOnTopEnemy, isTouched, lavaInvulnerable, isIdle;
    Texture runnerTexture;
    Vector2 position, velocity, speed;
    TextureAtlas atlas;
    TextureAtlas.AtlasRegion regionStand, regionJump, regionDeath;
    Array<TextureAtlas.AtlasRegion> regionRun;
    public float groundLevel, tempGround;
    private Rectangle bounds, intersectionBounds;
    static Preferences prefs;
    public Animation<TextureRegion> animationSlow, animationNormal, animationFast;
    public float stateTime;

    public Runner(){
        position = new Vector2(STARTING_X, STARTING_Y);
        groundLevel = position.y;
        tempGround = groundLevel;
        velocity = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        isMaintainHighSpeed = false;
        isOnGround = true;
        isFalling = false;
        isTouched = false;
        lavaInvulnerable = false;
        isOnTopEnemy = false;
        isIdle = false;

        atlas = new TextureAtlas("packedimages/runner.atlas");
        regionStand = atlas.findRegion("stand");
        regionRun = atlas.findRegions("run");
        regionJump = atlas.findRegion("jump");
        regionDeath = atlas.findRegion("death");
        bounds = new Rectangle(STARTING_X - CONTACT_BOUNDS_OFFSET_X, STARTING_Y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight());
        intersectionBounds = new Rectangle(STARTING_X - CONTACT_BOUNDS_OFFSET_X, STARTING_Y - CONTACT_BOUNDS_OFFSET_Y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight() - CONTACT_BOUNDS_OFFSET_Y); //intersection bounds

        animationSlow = new Animation<TextureRegion>(0.1f, regionRun);
        animationNormal = new Animation<TextureRegion>(0.08f, regionRun);
        animationFast = new Animation<TextureRegion>(0.07f, regionRun);

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

    public void increaseSpeed(int speed){
        MAX_SPEED += speed;
        MIN_SPEED += speed;
    }

    public void update(float dt){
//        drainHealth();

        //slow down runner
//        speed.add(FRICTION, 0);

       //make runner come back to the ground
        speed.add(0, GRAVITY);

        //remove invulnerability
        if(lavaInvulnerable){
            if(timeSinceMillis(lavaDamageTimeStart) > 1000){
                lavaDamageTimeStart = millis();
                lavaInvulnerable = false;
            }
        }

        if(speed.x > 0) {
            isIdle = false;
        }else if(speed.x <= 0 && speed.y == -23 && (!isJumping || !isFalling)){
            isIdle = true;
        }

       // make runner stop when reaching 0 speed
        if(speed.x < 0 ){
            speed.x = 0;
        }

        // runner cannot stop; min speed
        if(speed.x <= MIN_SPEED ){
            speed.x = MIN_SPEED;
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
        if (speed.y < -46) {
            isJumping = false;
            isFalling = true;
        }
        //minimum fall height before isOnGround is set; allows jumping when on top enemy bridge
        if (speed.y <= -46) {
            isJumping = false;
            isOnGround = false;
        }
        //limit jump height
        if(speed.y > JUMP_HEIGHT){
            speed.y = JUMP_HEIGHT;
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
            velocity.x = RUN_SPEED;
    }

    public void slowDown(){
        velocity.x = -RUN_SPEED;
    }

    public void jump(){
        velocity.y = 71;
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
        velocity.y = 1000;
        velocity.x = 0;
        isJumping = true;
        isOnGround = false;
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
        GameAssetLoader.dispose();
    }
}
