package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.utils.TimeUtils.millis;

/**
 * Created by Rod on 6/13/2017.
 */

public class Lava {
    static float BOUNDS_TOP_OFFSET = 1;
    Texture lavaTexture;
    Vector2 posLava;
    float damage;
    Rectangle bounds;
    boolean touched;

    public Lava(){
        lavaTexture = new Texture("lava.png");
    }

    public Lava(float x){
        lavaTexture = new Texture("lava.png");
        posLava = new Vector2(x, 0);
        damage = 5;
        touched = false;
        bounds = new Rectangle(0, 0, lavaTexture.getWidth(), lavaTexture.getHeight() + BOUNDS_TOP_OFFSET);
    }

    public void update(){
        bounds.setPosition(posLava.x, 0);
    }

    public void repositionLava(float x){
        posLava.set(x, 0);
    }

    public Texture getTexture() {
        return lavaTexture;
    }

    public float getDamage() {
        return damage;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void checkLavaCollision(Runner runner){
        if(getBounds().overlaps(runner.getBounds())){
            if(runner.health > 0 && !touched && !runner.isDead && runner.isOnGround){
                runner.health -= getDamage();
                touched = true;
                runner.invulnerable = true;
                runner.setLavaDamageTimeStart(millis());
                runner.lavaBounce();
            } else if (runner.health <= 0) {
                runner.isDead = true;
            }
        }else{
            touched = false;
        }
    }

    public Vector2 getPosLava() {
        return posLava;
    }

    public void dispose(){
        lavaTexture.dispose();
    }
}
