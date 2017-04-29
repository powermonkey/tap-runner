package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rod on 4/18/2017.
 */

public class Enemy {
    final static float SPEED = -50;
    protected final static int FRAME_COLS = 4;
    protected final static int FRAME_ROWS = 1;
    public final static float SPAWN_OFFSET_X = 300;
    public boolean touched;
    float damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds;
    Texture enemyTexture;

    public boolean isSpawned;
    public Animation<TextureRegion> animation;
    public float stateTime;

    public Enemy(){
        velocity = new Vector2(SPEED, 0);
        speed = new Vector2(SPEED, 0);
        touched = false;
    }

    public void update(float dt){
        position.add(SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
    }

    public void createBounds(){
        bounds = new Rectangle(getPosition().x, getPosition().y, enemyTexture.getWidth() / FRAME_COLS, enemyTexture.getHeight());
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public float getDamage() {
        return damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return enemyTexture;
    }

    public TextureRegion[] createFrames(Texture groundEnemyTexture){
        TextureRegion[][] tmp = TextureRegion.split(groundEnemyTexture,
                groundEnemyTexture.getWidth() / FRAME_COLS,
                groundEnemyTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return frames;
    }
}
