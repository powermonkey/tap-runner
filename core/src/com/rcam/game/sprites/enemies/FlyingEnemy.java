package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Rod on 4/21/2017.
 */

public class FlyingEnemy extends Enemy{
    public FlyingEnemy(int type){
        super();
        damage = 3;
        enemyTexture = new Texture(selectTexture(type));
        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture));
        stateTime = 0f;
    }

    private String selectTexture(int type){
        String textureString;

        switch(type){
            case 1:
                textureString = "Flyball_32x32_purple_flyV2_L.png";
                break;
            case 2:
                textureString = "Flyball_32x32_red_flyV2_L.png";
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
