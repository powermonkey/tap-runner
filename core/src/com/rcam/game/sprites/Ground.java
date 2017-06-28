package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/14/2017.
 */

public class Ground {
    Texture groundTexture, lavaTexture;
    Vector2 posGround, posLava;
    Boolean isLava;

    public Ground(){
        groundTexture = new Texture("ground.png");
    }

    public Ground(float x){
        groundTexture = new Texture("ground.png");
        posGround = new Vector2(x, 0);
    }

    public void repositionGround(float x){
        posGround.set(x, 0);
    }


    public Texture getTexture() {
        return groundTexture;
    }


    public Vector2 getPosGround() {
        return posGround;
    }

    public void setPosGround(Vector2 pos) {
        posGround = pos;
    }

    public void dispose(){
        groundTexture.dispose();
    }
}
