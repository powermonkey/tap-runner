package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy1 extends Enemy{
    Texture groundEnemy1Texture;
    public boolean isSpawned;

    public GroundEnemy1(){
        super();
        groundEnemy1Texture = new Texture("Turtle_32x32_green_stand_L.png");
        damage = 2;
    }

    public Texture getTexture() {
        return groundEnemy1Texture;
    }

    public void update(float dt){
        position.add(super.SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
    }

    public void createBounds(){
        bounds = new Rectangle(super.getPosition().x, super.getPosition().y, groundEnemy1Texture.getWidth(), groundEnemy1Texture.getHeight());
    }

    public void dispose(){
        groundEnemy1Texture.dispose();
    }

}
