package com.rcam.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Rod on 4/30/2017.
 */

public class PowerUp {
    protected Rectangle bounds;
    public Vector2 position;
    Texture texture;
    float heal;
    public final static float SPAWN_OFFSET_X = 300;
    public final static int RANDOM_POWERUP = 5;
    public boolean touched;
    public boolean isSpawned;
    TextureRegion[] textureRegions;
    int powerUpType;
    Random rand;

    public PowerUp(){

    }

    public PowerUp(float x, float y){
        texture = new Texture("M484GoodFruits.png");
        textureRegions = new TextureRegion[5];
        textureRegions[0] = new TextureRegion(texture, 7, 4, 25, 25); //cherry
        textureRegions[1] = new TextureRegion(texture, 63, 4, 25, 25); //grapes
        textureRegions[2] = new TextureRegion(texture, 34, 91, 25, 25); //orange
        textureRegions[3] = new TextureRegion(texture, 144, 91, 25, 25); //banana
        textureRegions[4] = new TextureRegion(texture, 92, 60, 25, 25); //apple
        touched = false;
        randomPowerUp();
        position = new Vector2(x, y);
        createBounds(x, y, 25, 25);
        isSpawned = true;
    }

    public void createBounds(float x, float y , float width, float height){
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void randomPowerUp(){
        rand = new Random();
        powerUpType = rand.nextInt(RANDOM_POWERUP);
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
                heal = 3;
                break;
            default:
                throw new IllegalArgumentException("No such power up type");
        }
    }

    public TextureRegion getTextureRegion(){
        return textureRegions[powerUpType];
    }

    public float getHeal() {
        return heal;
    }

    public void dispose(){
        texture.dispose();
    }
}
