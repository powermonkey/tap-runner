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
    static float gravity;
    static float gravityIncrease = -9;
    public float maxSpeed = 150;
    static float runSpeed = 50;
    static float speedIncrease = 50;
    static float jumpValue = 65;
    static float jumpIncrease = 10;
    static float jumpHeightIncrease = 50;
    public final float STARTING_X = 30;
    public final float STARTING_Y = 112;
    static float jumpHeight = 525;
    static final int STARTING_HEALTH = 50;
    public static final float CONTACT_BOUNDS_OFFSET_Y = 4;
    public static final float CONTACT_BOUNDS_OFFSET_X = 1;
    public float health;
    private long startingTime, lavaDamageTimeStart;
    public boolean isMaintainHighSpeed, isOnGround, isJumping, isDead, animatingDeath, isFalling, isOnTopEnemy, isTouched, lavaInvulnerable, isIdle, isSmoking;
    Vector2 position, velocity, speed;
    TextureAtlas atlas;
    TextureAtlas.AtlasRegion regionStand, regionJump, regionDeath;
    Array<TextureAtlas.AtlasRegion> regionRun;
    public float groundLevel, tempGround;
    private Rectangle bounds, intersectionBounds;
    static Preferences prefs;
    public Animation<TextureRegion> animationFast;
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
        gravity = -23; //reset gravity
        jumpHeight = 525; //reset jump height
        jumpValue = 71; //reset jump
        regionStand = GameAssetLoader.regionStand;
        regionRun = GameAssetLoader.regionRun;
        regionJump = GameAssetLoader.regionJump;
        regionDeath = GameAssetLoader.regionDeath;
        bounds = new Rectangle(STARTING_X - CONTACT_BOUNDS_OFFSET_X, STARTING_Y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight());
        intersectionBounds = new Rectangle(STARTING_X - CONTACT_BOUNDS_OFFSET_X, STARTING_Y - CONTACT_BOUNDS_OFFSET_Y, regionStand.getRegionWidth() - CONTACT_BOUNDS_OFFSET_X, regionStand.getRegionHeight() - CONTACT_BOUNDS_OFFSET_Y); //intersection bounds

        animationFast = new Animation<TextureRegion>(0.07f, regionRun);

        health = STARTING_HEALTH;
        startingTime = millis();

        prefs = Gdx.app.getPreferences("TapRunner");

        if(prefs.getString("GameMode").equals("The Ground Is Lava")){
            isSmoking = false;
        }

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

    public void increaseSpeed(){
        maxSpeed += speedIncrease;
    }

    public void increaseJump(){
        jumpValue += jumpIncrease;
        jumpHeight += jumpHeightIncrease;
    }

    public void increaseGravity(){
        gravity = gravity + gravityIncrease;
    }

    public void update(float dt){
//        drainHealth();

        //slow down runner
//        speed.add(FRICTION, 0);

       //make runner come back to the ground
        speed.add(0, gravity);

        //remove invulnerability
        if(lavaInvulnerable){
            if(timeSinceMillis(lavaDamageTimeStart) > 1000){
                lavaDamageTimeStart = millis();
                lavaInvulnerable = false;
                isSmoking = false;
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
        if(speed.x <= maxSpeed ){
            speed.x = maxSpeed;
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
        if (speed.y < (gravity * 2)) {
            isJumping = false;
            isFalling = true;
        }
        //minimum fall height before isOnGround is set; allows jumping when on top enemy bridge
        if (speed.y <= (gravity * 2)) {
            isJumping = false;
            isOnGround = false;
        }
        //limit jump height
        if(speed.y > jumpHeight){
            speed.y = jumpHeight;
            isJumping = false;
        }

        //limit speed to max speed
        if(speed.x >= maxSpeed){
            speed.x = maxSpeed;
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

//    private void drainHealth(){
//        if(timeSinceMillis(startingTime) > 1000){
//            startingTime = millis();
//            health -= 1f;
//            if(health <= 0){
//                isDead = true;
//            }
//        }
//    }

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

    public void setLavaDamageTimeStart(long time) {
        lavaDamageTimeStart = time;
    }

    public void run(){
            velocity.x = runSpeed;
    }

    public void slowDown(){
        velocity.x = -runSpeed;
    }

    public void jump(){
        velocity.y = jumpValue;
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
