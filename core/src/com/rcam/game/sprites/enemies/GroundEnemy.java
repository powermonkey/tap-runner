package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy implements Pool.Poolable{
    protected final static int FRAME_COLS = 4;
    protected final static int FRAME_ROWS = 1;
    public final static float ON_TOP_CONTACT_OFFSET = 5;
    public static final float CONTACT_BOUNDS_OFFSET_X = 10;

    public GroundEnemy(){
        super();
        this.enemyTexture[0] = new Texture("Jellymonster_32x32_green_move_L2.png");
        this.enemyTexture[1] = new Texture("Jellymonster_32x32_yellow_move_L2.png");

        // fixed value since textures are currently the same size; refactor in future
        this.textureWidth = enemyTexture[0].getWidth() / FRAME_COLS;
        this.textureHeight = enemyTexture[0].getHeight() - ON_TOP_CONTACT_OFFSET;
        animationMonster1 = new Animation<TextureRegion>(0.1f, createFrames(this.enemyTexture[0], FRAME_ROWS, FRAME_COLS));
        animationMonster2 = new Animation<TextureRegion>(0.1f, createFrames(this.enemyTexture[1], FRAME_ROWS, FRAME_COLS));
    }

    public void init(int monsterType, Vector2 pos){
        monsterType = monsterType - 1;
        super.init(pos);
        damage = 7;
        if(monsterType == 0){
            animation = animationMonster1;
        }else{
            animation = animationMonster2;
        }
        stateTime = 0f;
        textureWidth = enemyTexture[monsterType].getWidth() / FRAME_COLS;
        textureHeight = enemyTexture[monsterType].getHeight() - ON_TOP_CONTACT_OFFSET;
        setBounds(pos.x - CONTACT_BOUNDS_OFFSET_X, pos.y, textureWidth - CONTACT_BOUNDS_OFFSET_X, textureHeight);
        setOnTopBounds(pos.x - CONTACT_BOUNDS_OFFSET_X, pos.y, textureWidth - CONTACT_BOUNDS_OFFSET_X, textureHeight);
    }

    public void dispose() {
        enemyTexture[0].dispose();
        enemyTexture[1].dispose();
    }

    @Override
    public void reset() {
        super.velocity.set(0, 0);
        super.speed.set(0, 0);
        super.touched = false;
        super.runnerOntop = false;
        super.position.set(0, 0);
        super.isSpawned = false;
        super.bounds.set(0,0,0,0);
        super.onTopBounds.set(0,0,0,0);
        super.intersection.set(0,0,0,0);
        super.intersectionBounds.set(0,0,0,0);
        super.intersectionOnTop.set(0,0,0,0);
        super.SPEED = -20;
    }
}
