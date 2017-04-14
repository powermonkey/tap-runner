package com.rcam.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/14/2017.
 */

public class Ground {
    Texture grnd;
    Vector2 posGround;

    public Ground(float x){
        grnd = new Texture("ground.png");
        posGround = new Vector2(x, 0);
    }

    public void reposition(float x){
        posGround.set(x, 0);
    }

    public Texture getTexture() {
        return grnd;
    }

    public Vector2 getPosGround() {
        return posGround;
    }

    public void dispose(){
        grnd.dispose();
    }
}
