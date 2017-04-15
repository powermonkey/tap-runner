package com.rcam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/14/2017.
 */

public class Runner {
    static final int FRICTION = -5;
    static final int GRAVITY = -5;
    static final int HIGH_SPEED = 1000;
    static final int SPEED_BUFFER = 2000;
    boolean isMaintainHighSpeed;
    boolean isOnGround;
    Texture runner;
    Vector2 position, velocity, speed;
    float groundLevel;

    public Runner(int x, int y){
        position = new Vector2(x, y);
        groundLevel = position.y;
        velocity = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        runner = new Texture("bird.png");
        isMaintainHighSpeed = false;
        isOnGround = true;
    }

    public void update(float dt){
        //slow down runner
        speed.add(FRICTION, 0);

        //make runner come back to the ground
        speed.add(0, GRAVITY);

        //make runner stop when reaching 0 speed
        if(speed.x < 0 )
            speed.x = 0;


        //maintain high speed
        speed.add(velocity.x, velocity.y);
        if(!isMaintainHighSpeed) {
            position.add(speed.x * dt, speed.y * dt);
        }else{
            if(speed.x > SPEED_BUFFER)
                speed.x = SPEED_BUFFER;
            position.add(HIGH_SPEED * dt, speed.y * dt);
        }

        //make runner land on ground
        if(position.y < groundLevel){
            position.y = groundLevel;
            isOnGround = true;
            speed.y = 0;
        }

        if(speed.x > HIGH_SPEED)
            isMaintainHighSpeed = true;
        else
            isMaintainHighSpeed = false;

        System.out.println(speed.y);
        //reset value of velocity x and y
        velocity.x = 0;
        velocity.y = 0;
    }

    public Texture getTexture() {
        return runner;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void run(){
        if(isMaintainHighSpeed)
            velocity.x = 500;
        else
            velocity.x = 120;
    }

    public void jump(){
        velocity.y = 250;
        isOnGround = false;
    }

    public void dispose(){
        runner.dispose();
    }
}
