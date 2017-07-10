package com.rcam.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.GameAssetLoader;
import com.rcam.game.Hud;

import static com.badlogic.gdx.utils.TimeUtils.millis;

/**
 * Created by Rod on 6/13/2017.
 */

public class Lava {
    static float BOUNDS_TOP_OFFSET = 2;
    Vector2 posLava;
    float damage, damageIncrease;
    Rectangle bounds;
    boolean touched;
    TextureAtlas.AtlasRegion lava;
    Sound lavaBurnSound;
    Preferences prefs;

    public Lava(){
        lava = GameAssetLoader.lava;
    }

    public Lava(float x){
        lava = GameAssetLoader.lava;
        lavaBurnSound = GameAssetLoader.lavaBurn;
        posLava = new Vector2(x, 0);
        damage = 5;
        damageIncrease = 5;
        touched = false;
        bounds = new Rectangle(0, 0, lava.getRegionWidth(), lava.getRegionHeight() + BOUNDS_TOP_OFFSET);
        prefs = Gdx.app.getPreferences("TapRunner");
    }

    public void update(){
        bounds.setPosition(posLava.x, 0);
    }

    public void repositionLava(float x){
        posLava.set(x, 0);
    }

    public TextureAtlas.AtlasRegion getTextureLava() {
        return lava;
    }

    public float getDamage() {
        return damage;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void increaseDamage(){
        damage += damageIncrease;
    }

    public void checkLavaCollision(Runner runner, Hud.Health hud){
        if(getBounds().overlaps(runner.getBounds())){
            if(runner.health > 0 && !runner.lavaInvulnerable && !runner.isDead){
                if(prefs.getBoolean("SoundOn")) {
                    lavaBurnSound.play();
                }
                runner.health -= getDamage();
                runner.isSmoking = true;
                hud.update();
                touched = true;
                runner.lavaInvulnerable = true;
                runner.setLavaDamageTimeStart(millis());
//                runner.lavaBounce();
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
//        GameAssetLoader.dispose();
    }
}
