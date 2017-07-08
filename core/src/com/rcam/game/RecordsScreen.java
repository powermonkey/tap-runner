package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 6/18/2017.
 */

public class RecordsScreen implements Screen{
    final TapRunner game;
    OrthographicCamera cam;
    Skin cleanCrispySkin,arcadeSkin;
    Stage stage;
    Table rootTable, table, labelTable, goBackButtonTable;
    TextButton goBackButton;
    Runner runner;
    Label bestLabel, bestNormalLabel, bestLavaLabel, recordsLabel, bestNormalDistance, bestLavaDistance;
    static Preferences prefs;
    TextureAtlas.AtlasRegion bg, blockYellow, blockGreen, ground;
    Sound blipSelectSound;
    BitmapFont buttonFonts;

    public RecordsScreen(final TapRunner gam){
        this.game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        stage = new Stage(new FitViewport(480, 800));
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        goBackButtonTable = new Table();
        labelTable = new Table();
        prefs = Gdx.app.getPreferences("TapRunner");
        bg = GameAssetLoader.atlas.findRegion("background");
        ground = GameAssetLoader.atlas.findRegion("ground");
        blockYellow = GameAssetLoader.atlas.findRegion("Block_Type2_Yellow");
        blipSelectSound = GameAssetLoader.blipSelect;

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);

        recordsLabel = new Label("Records", arcadeSkin, "default");
        bestLabel = new Label("Best Distance", arcadeSkin, "default");
        bestNormalLabel = new Label("Normal Mode: ", cleanCrispySkin, "default");
        bestNormalDistance = new Label(Integer.toString(prefs.getInteger("BestDistanceNormalMode")) + " m", arcadeSkin, "default");
        bestLavaLabel = new Label("The Ground is Lava: ", cleanCrispySkin, "default");
        bestLavaDistance = new Label(Integer.toString(prefs.getInteger("BestDistanceLavaMode")) + " m", arcadeSkin, "default");

        blockGreen = GameAssetLoader.atlas.findRegion("Block_Type2_YellowGreen");
        NinePatch patchGreen = new NinePatch(blockGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        buttonFonts = GameAssetLoader.buttonFonts;
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = patchDrawableGreen;
        buttonStyle.down = patchDrawableGreen;
        buttonStyle.font = buttonFonts;
        goBackButton = new TextButton("Main Menu", buttonStyle);
        goBackButtonListener(goBackButton);


        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        labelTable.add(recordsLabel).colspan(2).expandX().center().uniform().pad(5);
        labelTable.row();
        labelTable.setBackground(new NinePatchDrawable(patch));

        table.add(bestLabel).colspan(2).expandX().center().center().uniform().padBottom(15);
        table.row();
        table.add(bestNormalLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestNormalDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestLavaLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestLavaDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.pad(20, 20, 30, 20);
        table.setBackground(new NinePatchDrawable(patch));

        goBackButtonTable.add(goBackButton).colspan(2).width(200).height(50).expandX().pad(15);
        goBackButtonTable.row();
        goBackButtonTable.center().center();
        goBackButtonTable.setBackground(new NinePatchDrawable(patch));


        rootTable.add(labelTable).fillX();
        rootTable.row();
        rootTable.add(table);
        rootTable.row();
        rootTable.add(goBackButtonTable).fillX();
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);

    }

    public void goBackButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    blipSelectSound.play();
                }
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 459);
        game.batch.draw(ground, 0, 0);
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        GameAssetLoader.dispose();
    }
}
