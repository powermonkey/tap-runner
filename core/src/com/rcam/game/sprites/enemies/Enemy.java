package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.sprites.Runner;

import java.util.Vector;

/**
 * Created by Rod on 4/18/2017.
 */

public class Enemy {
    final static float SPEED = -20;
    public final static float SPAWN_OFFSET_FROM_CAM_X = 300;
    public final static float ON_TOP_OFFSET = 50;
    public boolean touched, runnerOntop, enemyTouchSlows;
    float damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds, intersection, intersectionBounds, onTopBounds, intersectionOnTop;
    public Texture[] enemyTexture;
    public float textureHeight, textureWidth;
    public boolean isSpawned;
    public Animation<TextureRegion> animation;
    public float stateTime;
    static Preferences prefs;

    public Enemy(){
        this.enemyTexture = new Texture[2];
        this.velocity = new Vector2();
        this.speed = new Vector2();
        this.touched = false;
        this.intersection = new Rectangle();
        this.intersectionBounds = new Rectangle();
        this.intersectionOnTop = new Rectangle();
        this.runnerOntop = false;
        this.position = new Vector2();
        this.isSpawned = true;

        prefs = Gdx.app.getPreferences("TapRunner");

        if (!prefs.contains("EnemyTouchSlows")) {
            prefs.putBoolean("EnemyTouchSlows", false);
            prefs.flush();
        }
        enemyTouchSlows = prefs.getBoolean("EnemyTouchSlows", false);
    }

    public void init(int type, Vector2 pos){
        velocity.set(SPEED, 0);
        speed.set(SPEED, 0);
        touched = false;
        runnerOntop = false;
        position = new Vector2(pos.x, pos.y);
        isSpawned = true;
    }

    public void update(float dt){
        position.add(SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
        onTopBounds.setPosition(position.x, position.y + ON_TOP_OFFSET);
    }

    public void createBounds(float x, float y, float width, float height){
        bounds = new Rectangle(x, y, width, height);
    }

    public void createOnTopBounds(float x, float y, float width, float height){
        onTopBounds = new Rectangle(x, y + ON_TOP_OFFSET , width, height + ON_TOP_OFFSET );
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

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getOnTopBounds() {
        return onTopBounds;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public float getTextureWidth() {
        return textureWidth;
    }

    public TextureRegion[] createFrames(Texture enemy, int rows, int cols){
        TextureRegion[][] tmp = TextureRegion.split(enemy,
                enemy.getWidth() / cols,
                enemy.getHeight() / rows);

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return frames;
    }

    public void checkCollision(Runner runner) {
//        Intersector.intersectRectangles(getBounds(), runner.getBounds(), intersection);
        Intersector.intersectRectangles(getOnTopBounds(), runner.getBounds(), intersectionOnTop);
        Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds);

        // make runner fall
        if(runner.isOnTopEnemy && runnerOntop && !Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds)) {
            runnerOntop = false;
            runner.tempGround = runner.groundLevel;
            runner.isOnTopEnemy = false;
//            runner.isOnGround = false;
//            runner.isJumping = true;
            runner.isFalling = true;
        }

        if ( getBounds().overlaps(runner.getBounds()) ) {
            if(!runner.isDead && runner.isFalling && (Float.compare(intersectionOnTop.y, getOnTopBounds().y ) > 0
                    || Float.compare((intersectionOnTop.y), getOnTopBounds().y) == 0) ) { // runner stays on top of enemies
                runner.setPositionY(getPosition().y + getTextureHeight());
                runner.tempGround = getPosition().y + getTextureHeight();
                runner.isOnGround = true;
                runner.isJumping = false;
                runner.isOnTopEnemy = true;
                runnerOntop = true;
                runner.isFalling = false;
            }else{
                if (runner.health > 0 && !touched && Float.compare((intersectionBounds.y), runner.getIntersectionBounds().y) > 0 && !runner.invulnerable) {
                        runner.health -= getDamage();
                        touched = true;
                        if(enemyTouchSlows) {
                            runner.isTouched = true;
                            runner.setVelocityX(runner.getVelocity().x - 50);
                        }
                } else if (runner.health <= 0) {
                    runner.isDead = true;
                }
            }
        }else{
            touched = false;
        }
    }

    public void dispose(){
        enemyTexture[0].dispose();
        enemyTexture[1].dispose();
    }
}
