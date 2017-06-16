package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.sprites.Ground;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy{
    protected final static int FRAME_COLS = 4;
    protected final static int FRAME_ROWS = 1;
    public final static float ON_TOP_CONTACT_OFFSET = 5;
    public GroundEnemy(int type){
        enemyTexture = new Texture(selectTexture(type));
        textureWidth = enemyTexture.getWidth() / FRAME_COLS;
        textureHeight = enemyTexture.getHeight() - ON_TOP_CONTACT_OFFSET;
    }

    public GroundEnemy(int monsterType, Vector2 pos, int[] levelDetails){
        super(pos, levelDetails);
        damage = 7;
        enemyTexture = new Texture(selectTexture(monsterType));
        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture, FRAME_ROWS, FRAME_COLS));
        stateTime = 0f;
        textureWidth = enemyTexture.getWidth() / FRAME_COLS;
        textureHeight = enemyTexture.getHeight() - ON_TOP_CONTACT_OFFSET;
        createBounds(pos.x, pos.y, textureWidth, textureHeight);
        createOnTopBounds(pos.x, pos.y, textureWidth, textureHeight);
    }

//    public GroundEnemy(int type, int spawnCount){ // enemy bridge
//        super();
//        damage = 7 * (spawnCount / 2);
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
                textureString = "Jellymonster_32x32_green_move_L2.png";
                break;
            case 2:
                textureString = "Jellymonster_32x32_yellow_move_L2.png";
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
