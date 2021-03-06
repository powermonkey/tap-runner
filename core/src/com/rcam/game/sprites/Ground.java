package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.GameAssetLoader;

/**
 * Created by Rod on 4/14/2017.
 */

public class Ground {
    Vector2 posGround;
    TextureAtlas.AtlasRegion ground;

    public Ground(){
        ground = GameAssetLoader.ground;
    }

    public Ground(float x){
        ground = GameAssetLoader.ground;
        posGround = new Vector2(x, 0);
    }

    public void repositionGround(float x){
        posGround.add(x, 0);
    }


    public TextureAtlas.AtlasRegion getTextureGround() {
        return ground;
    }


    public Vector2 getPosGround() {
        return posGround;
    }

    public void setPosGround(Vector2 pos) {
        posGround = pos;
    }

    public void dispose(){
//        GameAssetLoader.dispose();
    }
}
