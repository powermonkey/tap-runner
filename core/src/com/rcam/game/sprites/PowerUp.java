package com.rcam.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.rcam.game.GameAssetLoader;

import java.util.Random;

/**
 * Created by Rod on 4/30/2017.
 */

public class PowerUp implements Pool.Poolable{
    protected Rectangle bounds;
    public Vector2 position;
    private float heal;
    public final float SPAWN_OFFSET_X = 300;
    private final int RANDOM_POWER_UP = 6;
    private boolean touched;
    public boolean isSpawned;
    private int powerUpType;
    private Random rand;
    private TextureAtlas.AtlasRegion[] powerUpAtlasRegions;
    Sound powerUpSound;
    Preferences prefs;

    public PowerUp(){
        powerUpSound = GameAssetLoader.powerUp;
        TextureAtlas.AtlasRegion apple = GameAssetLoader.atlas.findRegion("powerup_apple");
        TextureAtlas.AtlasRegion cherry = GameAssetLoader.atlas.findRegion("powerup_cherry");
        TextureAtlas.AtlasRegion banana = GameAssetLoader.atlas.findRegion("powerup_banana");
        TextureAtlas.AtlasRegion grapes = GameAssetLoader.atlas.findRegion("powerup_grapes");
        TextureAtlas.AtlasRegion strawberry = GameAssetLoader.atlas.findRegion("powerup_strawberry");
        TextureAtlas.AtlasRegion orange = GameAssetLoader.atlas.findRegion("powerup_orange");

        powerUpAtlasRegions = new TextureAtlas.AtlasRegion[6];
        powerUpAtlasRegions[0] = apple;
        powerUpAtlasRegions[1] = cherry;
        powerUpAtlasRegions[2] = banana;
        powerUpAtlasRegions[3] = grapes;
        powerUpAtlasRegions[4] = strawberry;
        powerUpAtlasRegions[5] = orange;

        this.position = new Vector2();
        this.rand = new Random();
        this.touched = false;
        this.isSpawned = true;
        this.bounds = new Rectangle();

        prefs = Gdx.app.getPreferences("TapRunner");
    }

    public void init(float x, float y){
        touched = false;
        isSpawned = true;
        randomPowerUp();
        position.set(x, y);
        //width, height is 24
        setBounds(x, y, 24, 24);
    }

    public void setBounds(float x, float y , float width, float height){
        bounds.set(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void randomPowerUp(){
        powerUpType = rand.nextInt(RANDOM_POWER_UP);
        selectPowerUp(powerUpType);
    }

    public void selectPowerUp(int type){
        switch(type){
            case 0:
            case 1:
            case 2:
                heal = 2;
                break;
            case 3:
            case 4:
            case 5:
                heal = 3;
                break;
            default:
                throw new IllegalArgumentException("No such power up type");
        }
    }

    public void checkPowerUpCollision(Runner runner){
        if(getBounds().overlaps(runner.getBounds()) && !runner.isDead ){
            if(prefs.getBoolean("SoundOn")) {
                powerUpSound.play();
            }
            if(!(runner.health >= runner.STARTING_HEALTH) && !touched && !runner.isDead){
                runner.health += getHeal();
                touched = true;
                isSpawned = false;
                //TODO optional speed boost and double jump for power up
//                velocity.x = 400; //activates speed boost and grants double jump for one use
            }
            isSpawned = false;
        }else{
            touched = false;
        }
    }

    public TextureAtlas.AtlasRegion getAtlasRegion(){
        return powerUpAtlasRegions[powerUpType];
    }

    public float getHeal() {
        return heal;
    }

    public void dispose(){
        GameAssetLoader.dispose();
    }

    @Override
    public void reset() {
        isSpawned = false;
        touched = false;
        position.set(0,0);
        bounds.set(0,0,0,0);
        powerUpType = 0;
    }
}
