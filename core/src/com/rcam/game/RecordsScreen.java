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
import com.rcam.game.sprites.Lava;

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
    Label bestLabel, bestNormalLabel, bestLavaLabel, recordsLabel, bestNormalDistance, bestLavaDistance, bestOneHitWonderLabel, bestOneHitWonderDistance, bestFirstDegreeBurnLabel, bestFirstDegreeBurnDistance,
            bestMyHeartWillGoOnLabel, bestBurnBabyBurnLabel, bestMyHeartWillGoOnDistance, bestBurnBabyBurnDistance;
    static Preferences prefs;
    TextureAtlas.AtlasRegion bg, blockYellow, blockYellowGreen, ground;
    Sound blipSelectSound;
    BitmapFont buttonFonts;
    boolean soundOn;
    StringBuilder bestNormalValue, bestLavaValue, bestOneHitWonderValue, bestFirstDegreeBurnValue, bestMyHeartWillGoOnValue, bestBurnBabyBurnValue;
    Lava lava;
    String gameMode;

    public RecordsScreen(final TapRunner gam){
        this.game = gam;
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        bg = GameAssetLoader.bg;
        ground = GameAssetLoader.ground;
        blockYellow = GameAssetLoader.blockYellow;
        blipSelectSound = GameAssetLoader.blipSelect;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        buttonFonts = GameAssetLoader.buttonFonts;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH * 0.5f, TapRunner.HEIGHT * 0.5f + 50);
        stage = new Stage(new FitViewport(480, 800), game.batch);
        rootTable = new Table();
        table = new Table();
        goBackButtonTable = new Table();
        labelTable = new Table();
        rootTable.setFillParent(true);

        bestNormalValue = new StringBuilder();
        bestLavaValue = new StringBuilder();
        bestOneHitWonderValue = new StringBuilder();
        bestFirstDegreeBurnValue = new StringBuilder();
        bestMyHeartWillGoOnValue = new StringBuilder();
        bestBurnBabyBurnValue = new StringBuilder();

        prefs = Gdx.app.getPreferences("TapRunner");
        soundOn = prefs.getBoolean("SoundOn");

        lava = new Lava(cam.position.x - (cam.viewportWidth * 0.5f));

        gameMode = prefs.getString("GameMode");

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);
        NinePatch patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableYellow = new NinePatchDrawable(patch);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);

        recordsLabel = new Label("Records", arcadeSkin, "default");
        bestLabel = new Label("Best Distances", arcadeSkin, "default");

        bestNormalLabel = new Label("Normal Mode: ", cleanCrispySkin, "default");
        bestNormalDistance = new Label(bestNormalValue.append(prefs.getInteger("BestDistanceNormalMode")).append(" m"), arcadeSkin, "default");
        bestLavaLabel = new Label("The Ground is Lava: ", cleanCrispySkin, "default");
        bestLavaDistance = new Label(bestLavaValue.append(prefs.getInteger("BestDistanceLavaMode")).append(" m"), arcadeSkin, "default");
        bestOneHitWonderLabel = new Label("One Hit Wonder: ", cleanCrispySkin, "default");
        bestOneHitWonderDistance = new Label(bestOneHitWonderValue.append(prefs.getInteger("BestDistanceOneHitWonderMode")).append(" m"), arcadeSkin, "default");
        bestFirstDegreeBurnLabel = new Label("First Degree Burn: ", cleanCrispySkin, "default");
        bestFirstDegreeBurnDistance = new Label(bestFirstDegreeBurnValue.append(prefs.getInteger("BestDistanceFirstDegreeBurnMode")).append(" m"), arcadeSkin, "default");
        bestMyHeartWillGoOnLabel = new Label("My Heart Will Go On: ", cleanCrispySkin, "default");
        bestMyHeartWillGoOnDistance = new Label(bestMyHeartWillGoOnValue.append(prefs.getInteger("BestDistanceMyHeartWillGoOnMode")).append(" m"), arcadeSkin, "default");
        bestBurnBabyBurnLabel = new Label("Burn Baby Burn: ", cleanCrispySkin, "default");
        bestBurnBabyBurnDistance = new Label(bestBurnBabyBurnValue.append(prefs.getInteger("BestDistanceBurnBabyBurnMode")).append(" m"), arcadeSkin, "default");

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
        labelTable.setBackground(patchDrawableYellow);

        table.add(bestLabel).colspan(2).expandX().center().center().uniform().padBottom(15);
        table.row();
        table.add(bestNormalLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestNormalDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestLavaLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestLavaDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestOneHitWonderLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestOneHitWonderDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestFirstDegreeBurnLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestFirstDegreeBurnDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestMyHeartWillGoOnLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestMyHeartWillGoOnDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestBurnBabyBurnLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestBurnBabyBurnDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.pad(20, 20, 30, 20);
        table.setBackground(patchDrawableYellow);

        goBackButtonTable.add(goBackButton).colspan(2).width(200).height(50).expandX().pad(15);
        goBackButtonTable.row();
        goBackButtonTable.center().center();
        goBackButtonTable.setBackground(patchDrawableYellow);


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
                if(soundOn) {
                    blipSelectSound.play();
                }
                game.setScreen(new MainMenuScreen(game));
                dispose();
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
        if(gameMode.equals("The Ground Is Lava") || gameMode.equals("First Degree Burn")){
            game.batch.draw(lava.getTextureLava(), 0, 0);
        }else{
            game.batch.draw(ground, 0, 0);
        }
        game.batch.end();
//        stage.act();
        game.batch.setProjectionMatrix(stage.getCamera().combined);
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
        buttonFonts.dispose();
//        GameAssetLoader.dispose();
    }
}
