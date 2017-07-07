package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Rod on 4/14/2017.
 */

public class MainMenuScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Table rootTable, table, titleTable, titleTableInner;
    Skin cleanCrispySkin, arcadeSkin;
    TextButton exit, newGame, options, records, credits;
    Stage stage;
    static Preferences prefs;
    TextureAtlas.AtlasRegion ground, bg, blockYellow, blockGreen;
    TextureRegion runnerJump;
    Sound blipSelectSound, newGameblipSound;
    Boolean soundOn;
    Label title;
    Image runner;
    BitmapFont myFont, buttonFonts;

    public MainMenuScreen(final TapRunner gam){
        game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        bg = GameAssetLoader.atlas.findRegion("background");
        ground = GameAssetLoader.atlas.findRegion("ground");
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        blockYellow = GameAssetLoader.atlas.findRegion("Block_Type2_Yellow");
        blockGreen = GameAssetLoader.atlas.findRegion("Block_Type2_YellowGreen");
        blipSelectSound = GameAssetLoader.blipSelect;
        newGameblipSound = GameAssetLoader.newGameblip;
        runnerJump = GameAssetLoader.atlas.findRegion("jump");
        myFont = GameAssetLoader.fonts;
        buttonFonts = GameAssetLoader.buttonFonts;

        stage = new Stage(new FitViewport(480, 800));
        Gdx.input.setInputProcessor(stage);
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        titleTable = new Table();
        titleTableInner = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = myFont;

        runner = new Image(runnerJump);

        title = new Label("Happy Runnings", labelStyle);
        title.setAlignment(Align.center);
        NinePatch patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        NinePatch patchGreen = new NinePatch(blockGreen, 4, 4, 4, 4);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        NinePatchDrawable patchDrawableYellow = new NinePatchDrawable(patchYellow);

        buttonStyle.up = patchDrawableYellow;
        buttonStyle.down = patchDrawableYellow;
        buttonStyle.font = buttonFonts;

        newGame = new TextButton("New Game", buttonStyle);
        options = new TextButton("Options", buttonStyle);
        records = new TextButton("Records", buttonStyle);
        credits = new TextButton("Credits", buttonStyle);
        exit = new TextButton("Exit", buttonStyle);

        newGameButtonListener(newGame);
        settingsButtonListener(options);
        recordsButtonListener(records);
        creditsButtonListener(credits);
        exitButtonListener(exit);

//        stage.setDebugAll(true);

        titleTableInner.add(runner);
        titleTableInner.row();
        titleTableInner.add(title).fill().center();
        titleTableInner.row();
        titleTableInner.setBackground(patchDrawableYellow);
        titleTableInner.pad(15, 20, 20 ,20);

        titleTable.add(titleTableInner);
        titleTable.row();
        titleTable.setBackground(patchDrawableGreen);
        titleTable.pad(20);

        table.add(newGame).center().uniform().width(200).height(50).expandX().padTop(10);
        table.row();
        table.add(options).center().uniform().width(200).height(50).expandX().padTop(10);
        table.row();
        table.add(records).center().uniform().width(200).height(50).expandX().padTop(10);
        table.row();
        table.add(credits).center().uniform().width(200).height(50).expandX().padTop(10);
        table.row();
        table.add(exit).center().uniform().width(200).height(50).expandX().padTop(10);
        table.row();
        table.center().center();
        table.pad(20);
        table.setBackground(patchDrawableGreen);

        rootTable.add(titleTable);
        rootTable.row();
        rootTable.add(table).width(TapRunner.WIDTH / 2).pad(20);
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);

        prefs = Gdx.app.getPreferences("TapRunner");
        //set default game mode to normal
        if (!prefs.contains("GameMode")) {
            prefs.putString("GameMode", "Normal");
            prefs.flush();
        }
        if (!prefs.contains("SoundOn")) {
            prefs.putBoolean("SoundOn", true);
            prefs.flush();
        }

        soundOn = prefs.getBoolean("SoundOn");
    }

    public void newGameButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    newGameblipSound.play();
                }
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
    }

    public void settingsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    blipSelectSound.play();
                }
                game.setScreen(new SettingsScreen(game));
                return true;
            }
        });
    }

    public void recordsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    blipSelectSound.play();
                }
                game.setScreen(new RecordsScreen(game));
                return true;
            }
        });
    }

    public void creditsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    blipSelectSound.play();
                }
                game.setScreen(new CreditsScreen(game));
                return true;
            }
        });
    }

    public void exitButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Gdx.app.exit();
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
        game.batch.draw(ground, 0,0);
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
