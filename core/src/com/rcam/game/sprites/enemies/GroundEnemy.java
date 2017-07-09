package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.rcam.game.GameAssetLoader;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy implements Pool.Poolable{
    public final float ON_TOP_CONTACT_OFFSET = 5;
    public final float CONTACT_BOUNDS_OFFSET_X = 10;

    public GroundEnemy(){
        super();
        Array<TextureAtlas.AtlasRegion> jellyGreen = GameAssetLoader.jellyGreen;
        Array<TextureAtlas.AtlasRegion> jellyYellow = GameAssetLoader.jellyYellow;
        // fixed value since textures are currently the same size; refactor in future
        this.textureWidth = jellyGreen.get(0).getRegionWidth();
        this.textureHeight = jellyGreen.get(0).getRegionWidth() - ON_TOP_CONTACT_OFFSET;
        animationMonster1 = new Animation<TextureRegion>(0.1f, jellyGreen);
        animationMonster2 = new Animation<TextureRegion>(0.1f, jellyYellow);
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
        setBounds(pos.x - CONTACT_BOUNDS_OFFSET_X, pos.y, textureWidth - CONTACT_BOUNDS_OFFSET_X, textureHeight);
        setOnTopBounds(pos.x - CONTACT_BOUNDS_OFFSET_X, pos.y, textureWidth - CONTACT_BOUNDS_OFFSET_X, textureHeight);
    }

    public void dispose() {
        GameAssetLoader.dispose();
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
