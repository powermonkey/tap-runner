package com.rcam.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Rod on 7/1/2017.
 */

public class GameAssetLoader {
    public static TextureAtlas atlas;
    public static Skin cleanCrispySkin, arcadeSkin;
    public static Sound blipSelect, newGameblip, jump, hurt, powerUp, speedAdjust, lavaBurn;
    static GameAssetManager manager;

    public static void load() {
        manager = new GameAssetManager();
        manager.loadImages();
        manager.loadSkin();
        manager.loadSounds();
        manager.manager.finishLoading();

        atlas = manager.manager.get("packedimages/runner.atlas");
        cleanCrispySkin = manager.manager.get("skin/clean-crispy-ui/clean-crispy-ui.json");
        arcadeSkin = manager.manager.get("skin/arcade-ui/arcade-ui.json");

        blipSelect = manager.manager.get("sounds/EC_Collect.wav");
        newGameblip = manager.manager.get("sounds/Randomize54.wav");
        jump = manager.manager.get("sounds/jump_07.wav");
        hurt = manager.manager.get("sounds/SFX_Powerup_21.wav");
        powerUp = manager.manager.get("sounds/Score.wav");
        lavaBurn = manager.manager.get("sounds/Explosion.wav");
        speedAdjust = manager.manager.get("sounds/Health_Up.wav");
    }

    public static void dispose() {
        manager.dispose();
        atlas.dispose();
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
        blipSelect.dispose();
        newGameblip.dispose();
        jump.dispose();
        hurt.dispose();
        powerUp.dispose();
        lavaBurn.dispose();
        speedAdjust.dispose();
    }
}
