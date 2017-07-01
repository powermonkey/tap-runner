package com.rcam.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
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

    public void loadImages(){
        manager.load(imagesPack, TextureAtlas.class);
    }

    public void loadSkin(){
        SkinLoader.SkinParameter paramsCleanCrispySkin = new SkinLoader.SkinParameter("skin/clean-crispy-ui/clean-crispy-ui.atlas");
        SkinLoader.SkinParameter paramsArcadeSkin = new SkinLoader.SkinParameter("skin/arcade-ui/arcade-ui.atlas");

        manager.load(cleanCrispySkin, Skin.class, paramsCleanCrispySkin);
        manager.load(arcadeSkin, Skin.class, paramsArcadeSkin);
    }
}