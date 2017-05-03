package com.rcam.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.sprites.enemies.Enemy;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

/**
 * Created by Rod on 4/14/2017.
 */

public class Runner {
    static final float FRICTION = -1.5f;
    static final float GRAVITY = -15;
    static final float HIGH_SPEED = 200;
    static final float SPEED_BUFFER = 600;
    static final float MAX_JUMP_HEIGHT = 400;
    static final int STARTING_HEALTH = 50;
    float health;
    long startingTime;
    public boolean isMaintainHighSpeed, isOnGround, isJumping, isDead, animatingDeath;
    Texture runnerTexture;
    Vector2 position, velocity, speed;
    float groundLevel;
    private Rectangle bounds;
    static Preferences prefs;

    public Runner(float x, float y){
        position = new Vector2(x, y);
        groundLevel = position.y;
        velocity = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        runnerTexture = new Texture("bird.png");
        isMaintainHighSpeed = false;
        isOnGround = true;
        bounds = new Rectangle(x, y, runnerTexture.getWidth(), runnerTexture.getHeight());
        health = STARTING_HEALTH;
        startingTime = millis();

        prefs = Gdx.app.getPreferences("TapRunner");

        if (!prefs.contains("BestDistance")) {
            prefs.putInteger("BestDistance", 0);
            prefs.flush();
        }
    }

    public void setHighScore(int val) {
        prefs.putInteger("BestDistance", val);
        prefs.flush();
    }

    public int getHighScore() {
        return prefs.getInteger("BestDistance");
    }

    public void update(float dt){
        if(timeSinceMillis(startingTime) > 1000){
            startingTime = millis();
            health -= 1f;
            if(health <= 0){
                isDead = true;
            }
        }

        //slow down runner
        speed.add(FRICTION, 0);

       //make runner come back to the ground
        speed.add(0, GRAVITY);

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

        //limit jump height
        if(speed.y > MAX_JUMP_HEIGHT){
            speed.y = MAX_JUMP_HEIGHT;
            isJumping = false;
        }

        if(!isMaintainHighSpeed) {
            //prevent speed.x from going negative
            if(speed.x < 0)
                speed.x = 0;
            position.mulAdd(speed, dt);
        }else{
            if(speed.x > SPEED_BUFFER)
                speed.x = SPEED_BUFFER;
            position.add(HIGH_SPEED * dt, speed.y * dt);
        }

        //make runner land on ground
        if(position.y < groundLevel && !isDead){
            position.y = groundLevel;
            isOnGround = true;
            isJumping = false;
            speed.y = 0;
        }else if(isDead){
            isOnGround = false;
        }

        if(speed.x > HIGH_SPEED)
            isMaintainHighSpeed = true;
        else
            isMaintainHighSpeed = false;
//System.out.println(position.x);
        //reset value of velocity x and y
        velocity.x = 0;
        if(!isJumping)
            velocity.y = 0;
        bounds.setPosition(position.x, position.y);
    }

    public Texture getTexture() {
        return runnerTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void run(){
        if(isMaintainHighSpeed)
            velocity.x = 200;
        else
            velocity.x = 50;
    }

    public void jump(){
        velocity.y = 60;
        isJumping = true;
        isOnGround = false;
    }

    public void death(){
        animatingDeath = true;
        velocity.y = 100;
        velocity.x = 0;
        isJumping = true;
        isOnGround = false;
    }

    public Vector2 getSpeed(){ return speed; }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getHealth() {
        return health;
    }

    public void checkCollision(Enemy enemy){
        if(enemy.getBounds().overlaps(getBounds()) ){
            if(health > 0 && !enemy.touched){
                health -= enemy.getDamage();
                enemy.touched = true;
                if(velocity.x  < 25)
                    velocity.x = -25;
                else if(velocity.x  < 50)
                    velocity.x = -50;
                else if(velocity.x  < 75)
                    velocity.x = -75;
                else if(velocity.x  < 100)
                    velocity.x = -100;
                else if(velocity.x  < 125)
                    velocity.x = -125;
                else if(velocity.x  < 150)
                    velocity.x = -150;
                else if(velocity.x  < 175)
                    velocity.x = -175;
                else if(velocity.x  < 200)
                    velocity.x = -200;
                else if(velocity.x  < 257)
                    velocity.x = -257;
                else if(velocity.x  < 314)
                    velocity.x = -314;
                else if(velocity.x  < 371)
                    velocity.x = -371;
                else if(velocity.x  < 428)
                    velocity.x = -400;
                else if(velocity.x  < 485)
                    velocity.x = -400;
                else if(velocity.x  < 542)
                    velocity.x = -400;
                else if(velocity.x  < 600)
                    velocity.x = -400;
            }else if (health <= 0){
                isDead = true;
            }
        }else{
            enemy.touched = false;
        }
    }

    public void checkPowerUpCollision(PowerUp powerUp){
        if(powerUp.getBounds().overlaps(getBounds()) ){
            if(!(health >= STARTING_HEALTH) && !powerUp.touched){
                health += powerUp.getHeal();
                powerUp.touched = true;
                powerUp.isSpawned = false;
                //TODO optional speed boost and double jump for power up
//                velocity.x = 400; //activates speed boost and grants double jump for one use
            }
        }else{
            powerUp.touched = false;
        }
    }

    public int indicatePosition(){
        int s = Math.round(getPosition().x / 100);

        return s;
    }

    public void dispose(){
        runnerTexture.dispose();
    }
}
