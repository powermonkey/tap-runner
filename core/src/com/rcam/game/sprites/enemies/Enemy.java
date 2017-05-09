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
    public boolean touched, runnerOntop;
    float damage;
    public Vector2 position, velocity, speed;
    protected Rectangle bounds, contactBounds, intersection, intersectionBounds;
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
        intersectionBounds = new Rectangle();
        runnerOntop = false;
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

//        Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds);
        if (runnerOntop && !Intersector.intersectRectangles(getBounds(), runner.getIntersectionBounds(), intersectionBounds)) {
            runnerOntop = false;
            runner.tempGround = runner.groundLevel;
        }

//        if(intersection.x > r1.x)
//            //Intersects with right side
//        if(intersection.y > r1.y)
//            //Intersects with top side
//        if(intersection.x + intersection.width < r1.x + r1.width)
//            //Intersects with left side
//        if(intersection.y + intersection.height < r1.y + r1.height)
//            //Intersects with bottom side
//        && Float.compare((intersection.y + intersection.height), getBounds().y + getBounds().height) < 0
//        && Float.compare(intersection.x, getBounds().x) < 0

        if (getBounds().overlaps(runner.getBounds())) {
            if (Float.compare(intersection.y, getBounds().y) > 0
                    && Float.compare((intersection.x + intersection.width), getBounds().x + getBounds().width) < 0
                    ) {
                runner.tempGround = getPosition().y + getTextureHeight(); //stops intetersection but flags isOntopEnemy
                runner.isOnGround = true;
                runner.isJumping = false;
                speed.y = 0;
                runnerOntop = true;
            } else {
                if (runner.health > 0 && !touched) {
                    runner.health -= getDamage();
                    touched = true;
                    if (runner.getVelocity().x < 25)
                        runner.setVelocityX(-25);
                    else if (runner.getVelocity().x < 50)
                        runner.setVelocityX(-50);
                    else if (runner.getVelocity().x < 75)
                        runner.setVelocityX(-75);
                    else if (runner.getVelocity().x < 100)
                        runner.setVelocityX(-100);
                    else if (runner.getVelocity().x < 125)
                        runner.setVelocityX(-125);
                    else if (runner.getVelocity().x < 150)
                        runner.setVelocityX(-150);
                    else if (runner.getVelocity().x < 175)
                        runner.setVelocityX(-175);
                    else if (runner.getVelocity().x < 200)
                        runner.setVelocityX(-200);
                    else if (runner.getVelocity().x < 257)
                        runner.setVelocityX(-257);
                    else if (runner.getVelocity().x < 314)
                        runner.setVelocityX(-314);
                    else if (runner.getVelocity().x < 371)
                        runner.setVelocityX(-371);
                    else if (runner.getVelocity().x < 428)
                        runner.setVelocityX(-400);
                    else if (runner.getVelocity().x < 485)
                        runner.setVelocityX(-400);
                    else if (runner.getVelocity().x < 542)
                        runner.setVelocityX(-400);
                    else if (runner.getVelocity().x < 600)
                        runner.setVelocityX(-400);
                } else if (runner.health <= 0) {
                    runner.isDead = true;
                }
            }
        }else{
            touched = false;
        }
    }
}
