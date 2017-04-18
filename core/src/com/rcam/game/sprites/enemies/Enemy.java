package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/18/2017.
 */

public class Enemy {
    final static float SPEED = -50;
    Float damage;
    public Vector2 position, velocity, speed;

    public Enemy(){
        velocity = new Vector2(SPEED, 0);
        speed = new Vector2(SPEED, 0);
//        position = new Vector2(1150, 112);
    }

    public void spawnEnemy(){


    }

    public void setPosition(Vector2 position){ this.position = position; }

    public Float getDamage() {
        return damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getSpeed() {
        return speed;
    }
}
