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
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Ground;

/**
 * Created by Rod on 6/5/2017.
 */

public class SettingsScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Skin cleanCrispySkin,arcadeSkin;
    Stage stage;
    Table gameModetable, optionsTable, rootTable, settingsLabelTable, buttonsTable;
    Ground ground;
    Label gameModeLabel, otherOptionsLabel, settingsLabel;
    ButtonGroup gameModeGroup;
    CheckBox normalMode, groundLavaMode, enemyTouchSlows, soundOn;
    TextButton okay;
    static Preferences prefs;
    TextureAtlas.AtlasRegion bg, blockYellow, blockYellowGreen;
    Sound blipSelectSound;
    BitmapFont buttonFonts;
    boolean soundTurnedOn;

    public SettingsScreen(final TapRunner gam){
        this.game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH * 0.5f, TapRunner.HEIGHT * 0.5f + 50);
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        blipSelectSound = GameAssetLoader.blipSelect;
        bg = GameAssetLoader.bg;
        blockYellow = GameAssetLoader.blockYellow;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        buttonFonts = GameAssetLoader.buttonFonts;

        stage = new Stage(new FitViewport(480, 800), game.batch);
        prefs = Gdx.app.getPreferences("TapRunner");
        soundTurnedOn = prefs.getBoolean("SoundOn");

        gameModetable = new Table();
        buttonsTable = new Table();
        optionsTable = new Table();
        settingsLabelTable = new Table();
        rootTable = new Table();
        rootTable.setFillParent(true);

        gameModeLabel = new Label("Game Mode", arcadeSkin, "default");
        otherOptionsLabel = new Label("Extras", arcadeSkin, "default");
        settingsLabel = new Label("Options", arcadeSkin, "default");

        normalMode = new CheckBox("Normal", cleanCrispySkin, "radio");
        groundLavaMode = new CheckBox("The Ground Is Lava", cleanCrispySkin, "radio");
//        enemyTouchSlows = new CheckBox("Enemy Touch Slows", cleanCrispySkin, "default");
        soundOn = new CheckBox("Sound On", cleanCrispySkin, "default");


        NinePatch patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        NinePatch patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableYellow = new NinePatchDrawable(patchYellow);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = patchDrawableGreen;
        buttonStyle.down = patchDrawableGreen;
        buttonStyle.font = buttonFonts;
        okay = new TextButton("Okay", buttonStyle);
        okayButtonListener(okay);

//        if (prefs.contains("EnemyTouchSlows")) {
//            enemyTouchSlows.setChecked(prefs.getBoolean("EnemyTouchSlows"));
//        }
        if (prefs.contains("SoundOn")) {
            soundOn.setChecked(prefs.getBoolean("SoundOn"));
        }

        gameModeGroup = new ButtonGroup(normalMode, groundLavaMode);
        gameModeGroup.setChecked(prefs.getString("GameMode"));
        gameModeGroup.setMaxCheckCount(1);
        gameModeGroup.setMinCheckCount(1);
        gameModeGroup.setUncheckLast(true);

        ground = new Ground(cam.position.x - (cam.viewportWidth * 0.5f));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        settingsLabelTable.add(settingsLabel).expandX().center().uniform();
        settingsLabelTable.row();
        settingsLabelTable.center().center().pad(10);
        settingsLabelTable.setBackground(patchDrawableYellow);

        gameModetable.add(gameModeLabel).colspan(2).expandX().center().uniform();
        gameModetable.row();
        gameModetable.add(normalMode).colspan(2).padTop(20).expandX().center().uniform();
        gameModetable.row();
        gameModetable.add(groundLavaMode).colspan(2).padTop(20).padBottom(20).expandX().center().uniform();
        gameModetable.row();
        gameModetable.center().center().pad(20);
        gameModetable.setBackground(patchDrawableYellow);

        optionsTable.add(otherOptionsLabel).colspan(2).expandX().center().uniform();
        optionsTable.row();
//        optionsTable.add(enemyTouchSlows).colspan(2).padTop(20).padBottom(20).expandX().center().uniform();
//        optionsTable.row();
        optionsTable.add(soundOn).colspan(2).padTop(20).padBottom(20).expandX().center().uniform();
        optionsTable.row();
        optionsTable.center().center().pad(20);
        optionsTable.setBackground(patchDrawableYellow);

        buttonsTable.add(okay).width(150).height(50).expandX();
        buttonsTable.row();
        buttonsTable.center().center().pad(20);
        buttonsTable.setBackground(patchDrawableYellow);

        rootTable.add(settingsLabelTable).width(TapRunner.WIDTH * 0.5f);
        rootTable.row();
        rootTable.add(gameModetable).width(TapRunner.WIDTH * 0.5f);
        rootTable.row();
        rootTable.add(optionsTable).width(TapRunner.WIDTH * 0.5f);
        rootTable.row();
        rootTable.add(buttonsTable).width(TapRunner.WIDTH * 0.5f);
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);
    }

    @Override
    public void show() {

    }

    public void okayButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                prefs.putString("GameMode", gameModeGroup.getChecked().getChildren().get(1).toString().replace("Label: ", ""));
//                prefs.putBoolean("EnemyTouchSlows", enemyTouchSlows.isChecked());
                prefs.putBoolean("SoundOn", soundOn.isChecked());
                prefs.flush();
                if(soundTurnedOn) {
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
        game.batch.draw(ground.getTextureGround(), 0, 0);
        game.batch.end();
//        stage.act();
        stage.draw();
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
