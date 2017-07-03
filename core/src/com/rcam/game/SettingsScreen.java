package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    CheckBox normalMode, groundLavaMode, enemyTouchSlows;
    TextButton okay;
    static Preferences prefs;
    TextureAtlas.AtlasRegion bg, blockYellow;
    Sound blipSelectSound;

    public SettingsScreen(final TapRunner gam){
        this.game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        blipSelectSound = GameAssetLoader.blipSelect;

        stage = new Stage(new FitViewport(480, 800));
        prefs = Gdx.app.getPreferences("TapRunner");

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
        enemyTouchSlows = new CheckBox("Enemy Touch Slows", cleanCrispySkin, "default");
        okay = new TextButton("Okay", cleanCrispySkin, "default");
        okayButtonListener(okay);

        if (prefs.contains("EnemyTouchSlows")) {
            enemyTouchSlows.setChecked(prefs.getBoolean("EnemyTouchSlows"));
        }

        gameModeGroup = new ButtonGroup(normalMode, groundLavaMode);
        gameModeGroup.setChecked(prefs.getString("GameMode"));
        gameModeGroup.setMaxCheckCount(1);
        gameModeGroup.setMinCheckCount(1);
        gameModeGroup.setUncheckLast(true);

        bg = GameAssetLoader.atlas.findRegion("background");
        blockYellow = GameAssetLoader.atlas.findRegion("Block_Type2_Yellow");

        ground = new Ground(cam.position.x - (cam.viewportWidth / 2));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);

        settingsLabelTable.add(settingsLabel).expandX().center().uniform();
        settingsLabelTable.row();
        settingsLabelTable.center().center().pad(10);
        settingsLabelTable.setBackground(new NinePatchDrawable(patch));


        gameModetable.setBackground(new NinePatchDrawable(patch));
        gameModetable.add(gameModeLabel).colspan(2).expandX().center().uniform();
        gameModetable.row();
        gameModetable.add(normalMode).colspan(2).padTop(20).expandX().center().uniform();
        gameModetable.row();
        gameModetable.add(groundLavaMode).colspan(2).padTop(20).padBottom(20).expandX().center().uniform();
        gameModetable.row();
        gameModetable.center().center().pad(20);
        gameModetable.setBackground(new NinePatchDrawable(patch));

        optionsTable.add(otherOptionsLabel).colspan(2).expandX().center().uniform();
        optionsTable.row();
        optionsTable.add(enemyTouchSlows).colspan(2).padTop(20).padBottom(20).expandX().center().uniform();
        optionsTable.row();
        optionsTable.center().center().pad(20);
        optionsTable.setBackground(new NinePatchDrawable(patch));

        buttonsTable.add(okay).width(150).height(50).expandX();
        buttonsTable.row();
        buttonsTable.center().center().pad(20);
        buttonsTable.setBackground(new NinePatchDrawable(patch));

        rootTable.add(settingsLabelTable).width(TapRunner.WIDTH / 2);
        rootTable.row();
        rootTable.add(gameModetable).width(TapRunner.WIDTH / 2);
        rootTable.row();
        rootTable.add(optionsTable).width(TapRunner.WIDTH / 2);
        rootTable.row();
        rootTable.add(buttonsTable).width(TapRunner.WIDTH / 2);
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
                blipSelectSound.play();
                prefs.putString("GameMode", gameModeGroup.getChecked().getChildren().get(1).toString().replace("Label: ", ""));
                prefs.putBoolean("EnemyTouchSlows", enemyTouchSlows.isChecked());
                prefs.flush();
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 459);
        game.batch.draw(ground.getTextureGround(), 0, 0);
        game.batch.end();
        stage.act();
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
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
        stage.dispose();
        GameAssetLoader.atlas.dispose();
        ground.dispose();
    }
}
