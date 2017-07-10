package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.rcam.game.GameAssetLoader;

/**
 * Created by Rod on 7/9/2017.
 */

public class Smoke {
    Array<TextureAtlas.AtlasRegion> regionSmoke;
    public Animation<TextureRegion> smokeAnimationFast, smokeAnimationSlow;
    TextureAtlas atlas;
    public float stateTime;

    public Smoke(){
        regionSmoke = GameAssetLoader.regionSmoke;
        smokeAnimationFast = new Animation<TextureRegion>(0.06f, regionSmoke);
        smokeAnimationSlow = new Animation<TextureRegion>(0.05f, regionSmoke);
    }

    public void dispose(){
        GameAssetLoader.dispose();
    }

}
