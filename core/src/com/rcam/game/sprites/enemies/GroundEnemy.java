package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy{
    public GroundEnemy(int type){
        super();
        damage = 2;
        enemyTexture = new Texture(selectTexture(type));
        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture));
        stateTime = 0f;
        textureWidth = enemyTexture.getWidth() / 4;
        textureHeight = enemyTexture.getHeight();
    }

    private String selectTexture(int type){
        String textureString;

        switch(type){
            case 1:
                textureString = "Jellymonster_32x32_green_move_L.png";
                break;
            case 2:
                textureString = "Jellymonster_32x32_yellow_move_L.png";
                break;
            default:
                throw new IllegalArgumentException("No such texture");
        }

        return textureString;
    }

    public void dispose(){
        enemyTexture.dispose();
    }

}
