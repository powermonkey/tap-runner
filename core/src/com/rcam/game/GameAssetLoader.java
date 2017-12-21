package com.rcam.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rod on 7/1/2017.
 */

public class GameAssetLoader {
    public static TextureAtlas atlas;
    public static TextureAtlas.AtlasRegion bg, regionStand, regionJump, regionDeath, ground, lava, blockYellow, blockYellowGreen,
        pause, forward, powerupApple, powerupCherry, powerupBanana, powerupGrapes, powerupStrawberry, powerupOrange, audioOn, audioOff, star;
    public static Array<TextureAtlas.AtlasRegion> regionRun, regionSmoke, birdBlue, birdRed, jellyGreen, jellyYellow;
    public static Skin cleanCrispySkin, arcadeSkin;
    public static Sound blipSelect, newGameblip, jump, hurt, powerUp, lavaBurn;
    public static BitmapFont fonts, buttonFonts;
    static GameAssetManager manager;

    public static void load() {
        manager = new GameAssetManager();
        manager.loadImages();
        manager.loadSkin();
        manager.loadSounds();
        manager.loadFonts();
    }

    public static void getLoadedAssets(){
        atlas = manager.manager.get("packedimages/runner.atlas");
        bg = atlas.findRegion("background");
        regionStand = atlas.findRegion("stand");
        regionRun = atlas.findRegions("run");
        regionJump = atlas.findRegion("jump");
        regionDeath = atlas.findRegion("death");
        regionSmoke = atlas.findRegions("smoke");
        ground = atlas.findRegion("ground");
        lava = atlas.findRegion("lava");
        birdBlue = atlas.findRegions("bird_blue");
        birdRed = atlas.findRegions("bird_red");
        jellyGreen = atlas.findRegions("jelly_green");
        jellyYellow = atlas.findRegions("jelly_yellow");
        blockYellow = atlas.findRegion("Block_Type2_Yellow");
        blockYellowGreen = atlas.findRegion("Block_Type2_YellowGreen");
        pause = atlas.findRegion("pause");
        forward = atlas.findRegion("forward");
        audioOn = atlas.findRegion("musicOn");
        audioOff = atlas.findRegion("musicOff");
        powerupApple = atlas.findRegion("powerup_apple");
        powerupCherry = atlas.findRegion("powerup_cherry");
        powerupBanana = atlas.findRegion("powerup_banana");
        powerupGrapes = atlas.findRegion("powerup_grapes");
        powerupStrawberry = atlas.findRegion("powerup_strawberry");
        powerupOrange = atlas.findRegion("powerup_orange");
        star = atlas.findRegion("star");

        cleanCrispySkin = manager.manager.get("skin/clean-crispy-ui/clean-crispy-ui.json");
        arcadeSkin = manager.manager.get("skin/arcade-ui/arcade-ui.json");

        fonts = manager.manager.get("fonts/comfortaa.fnt");
        buttonFonts = manager.manager.get("fonts/prstart.fnt");

        blipSelect = manager.manager.get("sounds/EC_Collect.wav");
        newGameblip = manager.manager.get("sounds/Randomize54.wav");
        jump = manager.manager.get("sounds/jump_07.wav");
        hurt = manager.manager.get("sounds/SFX_Powerup_21.wav");
        powerUp = manager.manager.get("sounds/Score.wav");
        lavaBurn = manager.manager.get("sounds/Explosion.wav");
    }

    public static boolean update(){
        return manager.manager.update();
    }

    public static void dispose() {
        manager.dispose();
        fonts.dispose();
        buttonFonts.dispose();
        atlas.dispose();
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
        blipSelect.dispose();
        newGameblip.dispose();
        jump.dispose();
        hurt.dispose();
        powerUp.dispose();
        lavaBurn.dispose();
    }
}
