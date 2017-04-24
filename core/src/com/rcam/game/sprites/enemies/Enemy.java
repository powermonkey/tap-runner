package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/18/2017.
 */

public class Enemy {
    final static float SPEED = -50;
    public final static float SPAWN_OFFSET_X = 300;
    public final static float SPAWN_DISTANCE = 1000;
    public boolean touched;

    int damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds;

    public Enemy(){
        velocity = new Vector2(SPEED, 0);
        speed = new Vector2(SPEED, 0);
        touched = false;
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public int getDamage() {
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

    public Rectangle getBounds() {
        return bounds;
    }
}
