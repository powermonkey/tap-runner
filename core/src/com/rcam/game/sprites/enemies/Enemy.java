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
    public final static float SPAWN_OFFSET_X = 300;
    public boolean touched;
    float damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds, contactBounds, intersection, interx;
    Texture enemyTexture;
    public float textureHeight, textureWidth;

    public boolean isSpawned;
    public Animation<TextureRegion> animation;
    public float stateTime;

    public Enemy(){
        velocity = new Vector2(SPEED, 0);
        speed = new Vector2(SPEED, 0);
        touched = false;
        intersection = new Rectangle();
        interx = new Rectangle();
    }

    public void update(float dt){
        position.add(SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
    }

    public void createBounds(){
        bounds = new Rectangle(getPosition().x, getPosition().y, textureWidth, textureHeight);
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

    public Rectangle getContactBounds() {
        return contactBounds;
    }

    public Texture getTexture() {
        return enemyTexture;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public float getTextureWidth() {
        return textureWidth;
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

    public void checkCollision(Runner runner) {
        Intersector.intersectRectangles(getBounds(), runner.getBounds(), intersection);

        if (getBounds().overlaps(runner.getBounds()) ) {
            if (Float.compare(intersection.y, getBounds().y) > 0) {
                System.out.println("intersection.y:" + intersection.y + " runner.getBounds().y: " + runner.getBounds().y + " getBounds().y: " + getBounds().y);
                runner.tempGround = getPosition().y + getTextureHeight(); //stops intetersection but flags isOntopEnemy
//                System.out.println("intersection.y:" + intersection.y + " enemy.getBounds().y: " + enemy.getBounds().y + " getBounds().y: " + getBounds().y + " compare: " + Float.compare(intersection.y, enemy.getBounds().y) + " getPosition().y: " + getPosition().y + " isOntopEnemy: " + isOntopEnemy);
                runner.isOnGround = true;
                runner.isJumping = false;
                speed.y = 0;
//                isOntopEnemy = true;
            } else {
                if (runner.health > 0 && !touched) {
                    runner.health -= getDamage();
                    touched = true;
                    if (velocity.x < 25)
                        velocity.x = -25;
                    else if (velocity.x < 50)
                        velocity.x = -50;
                    else if (velocity.x < 75)
                        velocity.x = -75;
                    else if (velocity.x < 100)
                        velocity.x = -100;
                    else if (velocity.x < 125)
                        velocity.x = -125;
                    else if (velocity.x < 150)
                        velocity.x = -150;
                    else if (velocity.x < 175)
                        velocity.x = -175;
                    else if (velocity.x < 200)
                        velocity.x = -200;
                    else if (velocity.x < 257)
                        velocity.x = -257;
                    else if (velocity.x < 314)
                        velocity.x = -314;
                    else if (velocity.x < 371)
                        velocity.x = -371;
                    else if (velocity.x < 428)
                        velocity.x = -400;
                    else if (velocity.x < 485)
                        velocity.x = -400;
                    else if (velocity.x < 542)
                        velocity.x = -400;
                    else if (velocity.x < 600)
                        velocity.x = -400;
                } else if (runner.health <= 0) {
                    runner.isDead = true;
                }
            }
        }else{
            touched = false;
        }
    }
}
