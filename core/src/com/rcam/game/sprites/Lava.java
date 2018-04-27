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
    float damage;
    Rectangle bounds;
    boolean touched;
    TextureAtlas.AtlasRegion lava;
    Sound lavaBurnSound;
    Preferences prefs;
    public final float DEFAULT_DAMAGE = 5;
    public final float HEART_DAMAGE = 1;
    public final float DAMAGE_INCREASE = 5;

    public Lava(){
        lava = GameAssetLoader.lava;
    }

    public Lava(float x){
        lava = GameAssetLoader.lava;
        lavaBurnSound = GameAssetLoader.lavaBurn;
        posLava = new Vector2(x, 0);
        touched = false;
        bounds = new Rectangle(0, 0, lava.getRegionWidth(), lava.getRegionHeight() + BOUNDS_TOP_OFFSET);
        prefs = Gdx.app.getPreferences("TapRunner");
        if(prefs.getString("GameMode").equals("My Heart Will Go On") || prefs.getString("GameMode").equals("Burn Baby Burn")) {
            damage = HEART_DAMAGE;
        } else {
            damage = DEFAULT_DAMAGE;
        }
    }

    public void update(){
        bounds.setPosition(posLava.x, 0);
    }

    public void repositionLava(float x){
        posLava.add(x, 0);
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
        damage += DAMAGE_INCREASE;
    }

    public void checkLavaCollision(Runner runner, Hud hud){
        if(getBounds().overlaps(runner.getBounds())){
//            if(runner.health > 0 && !runner.lavaInvulnerable && !runner.isDead){
            if(runner.health > 0 && !runner.damageInvulnerable && !runner.isDead){
                if(prefs.getBoolean("SoundOn")) {
                    lavaBurnSound.play();
                }
                runner.health -= getDamage();
                runner.setDamageStatus(Runner.Damage.TAKE);
                if(prefs.getString("GameMode").equals("Burn Baby Burn")){
                    runner.setHeartStatus(Runner.Heart.REMOVE);
                    hud.removeHeart();
                } else {
                    hud.healthUpdate();
                }
                runner.isSmoking = true;

                touched = true;
                runner.damageInvulnerable = true;
                runner.setDamageTimeStart(millis());
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
