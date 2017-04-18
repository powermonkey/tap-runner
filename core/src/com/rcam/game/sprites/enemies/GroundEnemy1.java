package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy1 extends Enemy{
    Texture groundEnemy1Texture;
    public boolean isSpawned;

    public GroundEnemy1(){
        super();
        groundEnemy1Texture = new Texture("Turtle_32x32_green_stand_L.png");
    }

    public Texture getTexture() {
        return groundEnemy1Texture;
    }

    public void update(float dt){
        position.add(super.SPEED * dt, 0);
    }

    public void dispose(){
        groundEnemy1Texture.dispose();
    }

}
