package com.rcam.game.sprites.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.lang.String;

/**
 * Created by Rod on 4/18/2017.
 */

public class GroundEnemy extends Enemy{
    private final static int FRAME_COLS = 4;
    private final static int FRAME_ROWS = 1;
    public final static int GROUND_ENEMY_GAP = 32;
    Texture groundEnemyTexture;
    public boolean isSpawned;
    public Animation<TextureRegion> walkAnimation;

    public float stateTime;

    public GroundEnemy(int type){
        super();
        damage = 2;
        groundEnemyTexture = new Texture(selectTexture(type));

        TextureRegion[][] tmp = TextureRegion.split(groundEnemyTexture,
                groundEnemyTexture.getWidth() / FRAME_COLS,
                groundEnemyTexture.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.1f, walkFrames);

        stateTime = 0f;
    }

    public Texture getTexture() {
        return groundEnemyTexture;
    }

    public void update(float dt){
        position.add(super.SPEED * dt, 0);
        bounds.setPosition(position.x, position.y);
    }

    public void createBounds(){
        bounds = new Rectangle(super.getPosition().x, super.getPosition().y, groundEnemyTexture.getWidth() / FRAME_COLS, groundEnemyTexture.getHeight());
    }

    private String selectTexture(int type){
        String textureString;

        switch(type){
            case 1:
                textureString = "Jellymonster_32x32_green_move_L.png";
                break;
            case 2:
                textureString = "Jellymonster_32x32_yellow_move_L.png";
                break;
            default:
                throw new IllegalArgumentException("No such texture");
        }

        return textureString;
    }

    public void dispose(){
        groundEnemyTexture.dispose();
    }

}
