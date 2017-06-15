package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/21/2017.
 */

public class FlyingEnemy extends Enemy{
    protected final static int FRAME_COLS = 8;
    protected final static int FRAME_ROWS = 1;
    public FlyingEnemy(int monsterType){
        enemyTexture = new Texture(selectTexture(monsterType));
        textureWidth = enemyTexture.getWidth() / FRAME_COLS;
        textureHeight = enemyTexture.getHeight();
    }

    public FlyingEnemy(int monsterType, Vector2 pos, int[] levelDetails){
        super(pos, levelDetails);
        damage = 8;
        enemyTexture = new Texture(selectTexture(monsterType));
        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture, FRAME_ROWS, FRAME_COLS));
        stateTime = 0f;
        textureWidth = enemyTexture.getWidth() / FRAME_COLS;
        textureHeight = enemyTexture.getHeight();
        createBounds(pos.x, pos.y, textureWidth, textureHeight);
        createOnTopBounds(pos.x, pos.y, textureWidth, textureHeight);
    }

//    public FlyingEnemy(int type, int spawnCount){ // enemy bridge
//        super();
//        damage = 8 * (spawnCount / 2);
//        enemyTexture = new Texture(selectTexture(type));
//        enemyTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
//        textureWidth = (enemyTexture.getWidth() / super.FRAME_COLS) * spawnCount;
//        textureHeight = enemyTexture.getHeight();
//        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture));
//        stateTime = 0f;
//    }

    private String selectTexture(int monsterType){
        String textureString;

        switch(monsterType){
            case 1:
                textureString = "Bird_32x32_blue_fly_L2.png";
                break;
            case 2:
                textureString = "Bird_32x32_red_fly_L2.png";
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
