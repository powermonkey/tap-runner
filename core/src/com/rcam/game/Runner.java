package com.rcam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/14/2017.
 */

public class Runner {
    static final int FRICTION = -5;
    static final int HIGH_SPEED = 1000;
    static final int SPEED_BUFFER = 2000;
    boolean isMaintainHighSpeed;
    Texture runner;
    Vector2 position, velocity, speed;

    public Runner(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        speed = new Vector2(0, 0);
        runner = new Texture("bird.png");
        isMaintainHighSpeed = false;
    }

    public void update(float dt){
        speed.x += FRICTION;
        if(speed.x < 0 )
            speed.x = 0;

        if(!isMaintainHighSpeed) {
            speed.x += velocity.x;
            position.add(speed.x * dt, 0);
        }else{
            speed.x += (velocity.x);
            if(speed.x > SPEED_BUFFER)
                speed.x = SPEED_BUFFER;
            position.add(HIGH_SPEED * dt, 0);
        }

        if(speed.x > HIGH_SPEED)
            isMaintainHighSpeed = true;
        else
            isMaintainHighSpeed = false;

        velocity.x = 0;
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

    public void dispose(){
        runner.dispose();
    }
}
