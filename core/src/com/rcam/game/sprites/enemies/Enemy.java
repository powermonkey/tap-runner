package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 4/18/2017.
 */

public class Enemy {
    final static float SPEED = -50;
    protected final static int FRAME_COLS = 4;
    protected final static int FRAME_ROWS = 1;
    public final static float SPAWN_OFFSET_FROM_CAM_X = 300;
    public final static float ON_TOP_OFFSET = 18;
    public boolean touched, runnerOntop;
    float damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds, intersection, intersectionBounds, onTopBounds, intersectionOnTop;
    Texture enemyTexture;
    public float textureHeight, textureWidth;

    public boolean isSpawned, isBridge;
    public Animation<TextureRegion> animation;
    public float stateTime;

    public Enemy(){

    }

    public Enemy(Vector2 pos, int[] levelDetails){
        velocity = new Vector2(SPEED, 0);
        speed = new Vector2(SPEED, 0);
        touched = false;
        intersection = new Rectangle();
        intersectionBounds = new Rectangle();
        intersectionOnTop = new Rectangle();
        runnerOntop = false;
        position = new Vector2(pos.x, pos.y);
        isSpawned = true;
        if(levelDetails[1] > 1 && levelDetails[2] == 1) {
            isBridge = true;
        }else{
            isBridge = false;
        }
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
        onTopBounds = new Rectangle(x, y + ON_TOP_OFFSET, width, height + ON_TOP_OFFSET);
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

    public TextureRegion[] createFrames(Texture enemy){
        TextureRegion[][] tmp = TextureRegion.split(enemy,
                enemy.getWidth() / FRAME_COLS,
                enemy.getHeight() / FRAME_ROWS);

        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        return frames;
    }

    public void checkCollision(Runner runner) {
        Intersector.intersectRectangles(getBounds(), runner.getBounds(), intersection);
        Intersector.intersectRectangles(getOnTopBounds(), runner.getBounds(), intersectionOnTop);

        Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds);
        // make runner fall
        if(runner.isOnTopEnemy && runnerOntop && !Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds)) {
            runnerOntop = false;
            runner.tempGround = runner.groundLevel;
            runner.isOnGround = false;
            runner.isJumping = true;
            runner.isOnTopEnemy = false;
            runner.isFalling = true;
        }

        if ( getBounds().overlaps(runner.getBounds()) ) {
            if(runner.isFalling && Float.compare(intersectionOnTop.y, getOnTopBounds().y ) > 0 ) { // runner stays on top of enemies
                runner.setPositionY(getPosition().y + getTextureHeight());
                runner.tempGround = getPosition().y + getTextureHeight();
                runner.isOnGround = true;
                runner.isJumping = false;
                runner.isOnTopEnemy = true;
                runnerOntop = true;
                runner.isFalling = false;
            }else{
                if (runner.health > 0 && !touched && Float.compare((intersectionBounds.y), runner.getIntersectionBounds().y) > 0) {
                        runner.health -= getDamage();
                        touched = true;
//                        if (runner.getVelocity().x < 25)
//                            runner.setVelocityX(-25);
//                        else if (runner.getVelocity().x < 50)
//                            runner.setVelocityX(-50);
//                        else if (runner.getVelocity().x < 75)
//                            runner.setVelocityX(-75);
//                        else if (runner.getVelocity().x < 100)
//                            runner.setVelocityX(-100);
//                        else if (runner.getVelocity().x < 125)
//                            runner.setVelocityX(-125);
//                        else if (runner.getVelocity().x < 150)
//                            runner.setVelocityX(-150);
//                        else if (runner.getVelocity().x < 175)
//                            runner.setVelocityX(-175);
//                        else if (runner.getVelocity().x < 200)
//                            runner.setVelocityX(-200);
//                        else if (runner.getVelocity().x < 257)
//                            runner.setVelocityX(-257);
//                        else if (runner.getVelocity().x < 314)
//                            runner.setVelocityX(-314);
//                        else if (runner.getVelocity().x < 371)
//                            runner.setVelocityX(-371);
//                        else if (runner.getVelocity().x < 428)
//                            runner.setVelocityX(-400);
//                        else if (runner.getVelocity().x < 485)
//                            runner.setVelocityX(-400);
//                        else if (runner.getVelocity().x < 542)
//                            runner.setVelocityX(-400);
//                        else if (runner.getVelocity().x < 600)
//                            runner.setVelocityX(-400);
                } else if (runner.health <= 0) {
                    runner.isDead = true;
                }
            }
        }else{
            touched = false;
        }
    }
}
