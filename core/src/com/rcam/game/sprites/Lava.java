package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 6/13/2017.
 */

public class Lava {
    Texture groundTexture, lavaTexture;
    Vector2 posLava;

    public Lava(){
        lavaTexture = new Texture("lava.png");
    }

    public Lava(float x){
        lavaTexture = new Texture("lava.png");
        posLava = new Vector2(x, 0);
    }


    public void repositionLava(float x){
        posLava.set(x, 0);
    }

    public Texture getTexture() {
        return lavaTexture;
    }

    public Vector2 getPosLava() {
        return posLava;
    }

    public void dispose(){
        lavaTexture.dispose();
    }
}
