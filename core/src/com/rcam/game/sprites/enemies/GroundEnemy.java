package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.lang.String;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy{
    public final static int GROUND_ENEMY_GAP = 32;
    Texture groundEnemyTexture;
    public boolean isSpawned;

    public GroundEnemy(int type){
        super();
        groundEnemyTexture = new Texture(selectTexture(type));
        damage = 2;
    }

    public Texture getTexture() {
        return groundEnemyTexture;
    }

    public void update(float dt){
        position.add(super.SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
    }

    public void createBounds(){
        bounds = new Rectangle(super.getPosition().x, super.getPosition().y, groundEnemyTexture.getWidth(), groundEnemyTexture.getHeight());
    }

    public String selectTexture(int type){
        String textureString;

        switch(type){
            case 1:
                textureString = "Turtle_32x32_green_stand_L.png";
                break;
            case 2:
                textureString = "Turtle_32x32_red_stand_L.png";
                break;
            default:
                throw new IllegalArgumentException("No such texture");
        }

        return textureString;
    }

    public void dispose(){
        groundEnemyTexture.dispose();
    }

}
