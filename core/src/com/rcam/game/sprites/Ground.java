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
        groundTexture = new Texture("newground.png");
    }

    public Ground(float x){
        groundTexture = new Texture("newground.png");
        posGround = new Vector2(x, 0);
    }

    public Ground(float x, boolean isLava){
        if(isLava){
            this.isLava = isLava;
            lavaTexture = new Texture("lava.png");
            posLava = new Vector2(x, 0);
        }
    }

    public void repositionGround(float x){
        posGround.set(x, 0);
    }

    public void repositionLava(float x){
        posLava.set(x, 0);
    }

    public Texture getTexture() {
        return groundTexture;
    }

    public Texture getLavaTexture() {
        return lavaTexture;
    }

    public Vector2 getPosGround() {
        return posGround;
    }

    public Vector2 getPosLava() {
        return posLava;
    }

    public void dispose(){
        groundTexture.dispose();
    }
}
