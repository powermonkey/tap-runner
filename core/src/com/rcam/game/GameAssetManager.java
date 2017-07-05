package com.rcam.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Rod on 7/1/2017.
 */

public class GameAssetManager {
    public final AssetManager manager = new AssetManager();
    public final String imagesPack = "packedimages/runner.atlas";
    public final String cleanCrispySkin = "skin/clean-crispy-ui/clean-crispy-ui.json";
    public final String arcadeSkin = "skin/arcade-ui/arcade-ui.json";
    public final String fonts = "fonts/comfortaa.fnt";

    public final String blipSelect = "sounds/EC_Collect.wav";
    public final String newGameblip = "sounds/Randomize54.wav";
    public final String jump = "sounds/jump_07.wav";
    public final String hurt = "sounds/SFX_Powerup_21.wav";
    public final String powerUp = "sounds/Score.wav";
    public final String lavaBurn = "sounds/Explosion.wav";
    public final String speedAdjust = "sounds/Health_Up.wav";

    public void loadImages(){
        manager.load(imagesPack, TextureAtlas.class);
    }

    public void loadSkin(){
        SkinLoader.SkinParameter paramsCleanCrispySkin = new SkinLoader.SkinParameter("skin/clean-crispy-ui/clean-crispy-ui.atlas");
        SkinLoader.SkinParameter paramsArcadeSkin = new SkinLoader.SkinParameter("skin/arcade-ui/arcade-ui.atlas");

        manager.load(cleanCrispySkin, Skin.class, paramsCleanCrispySkin);
        manager.load(arcadeSkin, Skin.class, paramsArcadeSkin);
    }

    public void loadSounds(){
        manager.load(blipSelect, Sound.class);
        manager.load(newGameblip, Sound.class);
        manager.load(jump, Sound.class);
        manager.load(hurt, Sound.class);
        manager.load(powerUp, Sound.class);
        manager.load(lavaBurn, Sound.class);
        manager.load(speedAdjust, Sound.class);
    }

    public void loadFonts(){
        manager.load(fonts, BitmapFont.class);
    }

    public void dispose(){
        manager.dispose();
    }
}
