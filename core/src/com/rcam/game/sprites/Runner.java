package com.rcam.game.sprites;

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
    public boolean isMaintainHighSpeed;
    public boolean isOnGround;
    public boolean isJumping;
    Texture runnerTexture;
    Vector2 position, velocity, speed;
    float groundLevel;
    private Rectangle bounds;

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
    }

    public void update(float dt){
        if(timeSinceMillis(startingTime) > 1000){
            startingTime = millis();
            health -= .5f;
            if(health <= 0)
                System.out.println("runner dead");
        }

        //slow down runner
        speed.add(FRICTION, 0);

       //make runner come back to the ground
        speed.add(0, GRAVITY);

       // make runner stop when reaching 0 speed
        if(speed.x < 0 ){
            speed.x = 0;
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
        if(position.y < groundLevel){
            position.y = groundLevel;
            isOnGround = true;
            isJumping = false;
            speed.y = 0;
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
                velocity.x = -400;
            }else if (health <= 0)
                System.out.println("runner dead");
        }else{
            enemy.touched = false;
        }
    }

    public void dispose(){
        runnerTexture.dispose();
    }
}
