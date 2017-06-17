package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Rod on 4/21/2017.
 */

public class FlyingEnemy extends Enemy implements Pool.Poolable{
    protected final static int FRAME_COLS = 8;
    protected final static int FRAME_ROWS = 1;
    public final static float ON_TOP_CONTACT_OFFSET = 5;

    public FlyingEnemy(){
        super();
        this.enemyTexture[0] = new Texture("Bird_32x32_blue_fly_L2.png");
        this.enemyTexture[1] = new Texture("Bird_32x32_red_fly_L2.png");

        // fixed value since textures are currently the same size; refactor in future
        this.textureWidth = enemyTexture[0].getWidth() / FRAME_COLS;
        this.textureHeight = enemyTexture[0].getHeight() - ON_TOP_CONTACT_OFFSET;
    }

    public void init(int monsterType, Vector2 pos){
        monsterType = monsterType - 1;
        super.init(monsterType, pos);
        damage = 7;
        animation = new Animation<TextureRegion>(0.1f, createFrames(enemyTexture[monsterType], FRAME_ROWS, FRAME_COLS));
        stateTime = 0f;
        textureWidth = enemyTexture[monsterType].getWidth() / FRAME_COLS;
        textureHeight = enemyTexture[monsterType].getHeight() - ON_TOP_CONTACT_OFFSET;
        createBounds(pos.x, pos.y, textureWidth, textureHeight);
        createOnTopBounds(pos.x, pos.y, textureWidth, textureHeight);
    }

    public void dispose(){
        enemyTexture[0].dispose();
        enemyTexture[1].dispose();
    }

    @Override
    public void reset() {
        super.velocity.set(0, 0);
        super.speed.set(0, 0);
        super.touched = false;
        super.runnerOntop = false;
        super.position = new Vector2(0, 0);
        super.isSpawned = false;
        super.bounds.set(0,0,0,0);
        super.onTopBounds.set(0,0,0,0);
        super.intersection.set(0,0,0,0);
        super.intersectionBounds.set(0,0,0,0);
        super.intersectionOnTop.set(0,0,0,0);
    }
}
