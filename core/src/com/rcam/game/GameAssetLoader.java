package com.rcam.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by Rod on 7/1/2017.
 */

public class GameAssetLoader {
    public static TextureAtlas atlas;
    public static Skin cleanCrispySkin, arcadeSkin;

    public static void load() {
        GameAssetManager manager = new GameAssetManager();
        manager.loadImages();
        manager.loadSkin();
        manager.manager.finishLoading();

        atlas = manager.manager.get("packedimages/runner.atlas");
        cleanCrispySkin = manager.manager.get("skin/clean-crispy-ui/clean-crispy-ui.json");
        arcadeSkin = manager.manager.get("skin/arcade-ui/arcade-ui.json");
    }

    public static void dispose() {
        atlas.dispose();
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
    }
}
