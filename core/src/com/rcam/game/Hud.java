package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;


/**
 * Created by Rod on 4/18/2017.
 */

public class Hud extends Table implements Disposable{
    Stage stage;
    Table rootTable, indicatorstable, controlsTable, pauseTable, pauseRootTable, rootHintTable, hintTable, audioStateTable, pauseButtonTable, heartRootTable;
    Skin cleanCrispySkin, arcadeSkin;
    Label healthLabel, hint;
    GameScreen gameScreen;
    TextureAtlas.AtlasRegion blockYellow, blockYellowGreen, pause, forward, audioOnImage, audioOffImage;
    NinePatchDrawable patchDrawableGreen, patchDrawableYellow;
    Sound blipSelectSound, newGameblipSound, jumpSound;
    Preferences prefs;
    NinePatch patchGreen, patchYellow;
    BitmapFont labelFont, buttonFonts;
    Actor screenActor;
    ImageButton pauseButton;
    ImageButton.ImageButtonStyle buttonStyle, unpauseStyle;
    TextButton continueButton, newGameButton, mainMenuButton, exitButton, okay;
    ProgressBar healthBar;
    Runner runner;
    TapRunner game;
    final TextButton.TextButtonStyle pauseButtonStyle, hintButtonStyle;
    CheckBox hideHint;
    ImageButton audioButton;
    ImageButton.ImageButtonStyle audioOnButtonStyle, audioOffButtonStyle;
    Image heart;

    public Hud(final TapRunner tapRunner, final Runner runner, final GameScreen gameScreen){
        setBounds(0, 0, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        setClip(true);
        this.game = tapRunner;
        this.gameScreen = gameScreen;
        this.runner = runner;
        prefs = Gdx.app.getPreferences("TapRunner");
        blockYellow = GameAssetLoader.blockYellow;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        pause = GameAssetLoader.pause;
        forward = GameAssetLoader.forward;
        blipSelectSound = GameAssetLoader.blipSelect;
        jumpSound = GameAssetLoader.jump;
        newGameblipSound = GameAssetLoader.newGameblip;
        buttonFonts = GameAssetLoader.buttonFonts;
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        audioOnImage = GameAssetLoader.audioOn;
        audioOffImage = GameAssetLoader.audioOff;
//        TextureAtlas.AtlasRegion heartRegion = GameAssetLoader.forward;
//        heart = new Image( new TextureRegion(heartRegion));

        stage = new Stage(new FitViewport(480, 800), game.batch);

        patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        patchDrawableGreen = new NinePatchDrawable(patchGreen);
        patchDrawableYellow = new NinePatchDrawable(patchYellow);

        hideHint = new CheckBox("Never show this again", cleanCrispySkin, "default");

        rootTable = new Table();
        indicatorstable = new Table();
        controlsTable = new Table();
        pauseRootTable = new Table();
//        heartRootTable = new Table();
        pauseTable = new Table();
        hintTable = new Table();
        rootHintTable = new Table();
        audioStateTable = new Table();
        pauseButtonTable = new Table();
        rootHintTable.setFillParent(true);
        rootTable.setFillParent(true);
        pauseRootTable.setFillParent(true);
//        heartRootTable.setFillParent(true);
        hintButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle = new TextButton.TextButtonStyle();

        gameScreen.isPause = true;

        labelFont = arcadeSkin.getFont("screen");
        Label.LabelStyle fontStyle = new Label.LabelStyle(labelFont, null);
        healthLabel = new Label("ENERGY", fontStyle);
        hint = new Label("Tap on screen to jump", arcadeSkin);

        screenActor = new Actor();
        healthBar = new ProgressBar(1, 50, 1, false, cleanCrispySkin, "default-horizontal");

        buttonStyle = new ImageButton.ImageButtonStyle();
        unpauseStyle = new ImageButton.ImageButtonStyle();
        pauseButton = new ImageButton(buttonStyle);

        pauseButtonStyle.up = patchDrawableYellow;
        pauseButtonStyle.down = patchDrawableYellow;
        pauseButtonStyle.font = buttonFonts;
        hintButtonStyle.up = patchDrawableYellow;
        hintButtonStyle.down = patchDrawableYellow;
        hintButtonStyle.font = arcadeSkin.getFont("screen");

        continueButton = new TextButton("Continue", pauseButtonStyle);
        newGameButton = new TextButton("New Game", pauseButtonStyle);
        mainMenuButton = new TextButton("Main Menu", pauseButtonStyle);
        exitButton = new TextButton("Exit", pauseButtonStyle);
        okay = new TextButton("Okay", hintButtonStyle);

        audioOnButtonStyle = new ImageButton.ImageButtonStyle();
        audioOffButtonStyle = new ImageButton.ImageButtonStyle();
        audioOnButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(audioOnImage));
        audioOnButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(audioOnImage));
        audioOffButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(audioOffImage));
        audioOffButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(audioOffImage));
        audioButton = new ImageButton(audioOnButtonStyle);

        setSoundImage();

        audioButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                prefs.putBoolean("SoundOn", !prefs.getBoolean("SoundOn"));
                prefs.flush();
                setSoundImage();
                return true;
            }
        });

        //touch screen to jump
        screenActor.setBounds(stage.getViewport().getScreenX(), stage.getViewport().getScreenY(), stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        screenActor.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause) {
                    if (runner.isOnGround) {
                        if (prefs.getBoolean("SoundOn")) {
                            jumpSound.play();
                        }
                        runner.jump();
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause) {
                    runner.isJumping = false;
                }
            }
        });
        stage.addActor(screenActor);

//        stage.setDebugAll(true);

        //hint
        hintTable.add(hint).pad(5);
        hintTable.row();
        hintTable.add(hideHint).pad(25);
        hintTable.row();
        hintTable.add(okay).width(70).height(40).pad(10);
        hintTable.row();
        hintTable.setBackground(patchDrawableGreen);
        hintTable.setVisible(false);
        rootHintTable.add(hintTable).center().center();
        rootHintTable.row();
        rootHintTable.setVisible(false);

        if (!prefs.contains("HideHint")) {
            prefs.putBoolean("HideHint", false);
            prefs.flush();
        }

        if(prefs.getBoolean("HideHint")){
            rootHintTable.remove();
            hintTable.setVisible(false);
            rootHintTable.setVisible(false);
            rootHintTable.remove();
            gameScreen.isPause = false;
        }else{
            hintTable.setVisible(true);
            rootHintTable.setVisible(true);
            stage.addActor(rootHintTable);
        }

        Gdx.input.setInputProcessor(stage);

        //health bar
        healthBar.setAnimateDuration(.5f);
        healthBar.setValue(runner.getHealth());

        //pause button
//        buttonStyle.up = patchDrawableYellow;
//        buttonStyle.down = patchDrawableYellow;
//        unpauseStyle.up = patchDrawableYellow;
//        unpauseStyle.down = patchDrawableYellow;

        buttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(pause));
        buttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(pause));
        unpauseStyle.imageUp = new TextureRegionDrawable(new TextureRegion(forward));
        unpauseStyle.imageDown = new TextureRegionDrawable(new TextureRegion(forward));

        pauseButton.setStyle(buttonStyle);
        pauseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause) {
                    if(prefs.getBoolean("SoundOn")) {
                        blipSelectSound.play(1.0f);
                    }
                    pauseButton.setStyle(unpauseStyle);
                    gameScreen.isPause = true;
                    pauseTable.setVisible(true);
                    pauseRootTable.setVisible(true);
                    stage.addActor(pauseRootTable);
                }else{
                    if(prefs.getBoolean("SoundOn")) {
                        blipSelectSound.play(1.0f);
                    }
                    pauseButton.setStyle(buttonStyle);
                    gameScreen.isPause = false;
                    pauseTable.setVisible(false);
                    pauseRootTable.setVisible(false);
                    pauseRootTable.remove();
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        okay.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    blipSelectSound.play();
                }

                prefs.putBoolean("HideHint", hideHint.isChecked());
                prefs.flush();

                hintTable.setVisible(false);
                gameScreen.isPause = false;

                return true;
            }
        });

        continueButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    blipSelectSound.play();
                }
                pauseButton.setStyle(buttonStyle);
                gameScreen.isPause = false;
                pauseRootTable.remove();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });


        newGameButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    newGameblipSound.play();
                }
//                    showInterstitialAd();
                pauseRootTable.remove();
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        mainMenuButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    blipSelectSound.play();
                }
//                showInterstitialAd();
                pauseRootTable.remove();
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });


        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                GameAssetLoader.dispose();
                Gdx.app.exit();
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
        });

        //pause menu
        pauseTable.add(continueButton).center().uniform().width(200).height(50).expandX().padTop(20);
        pauseTable.row();
        pauseTable.add(newGameButton).center().uniform().width(200).height(50).expandX().padTop(10);
        pauseTable.row();
        pauseTable.add(mainMenuButton).center().uniform().width(200).height(50).expandX().padTop(10);
        pauseTable.row();
        pauseTable.add(exitButton).center().uniform().width(200).height(50).expandX().padTop(10).padBottom(30);
        pauseTable.row();
        pauseTable.setBackground(patchDrawableGreen);
        pauseTable.setVisible(false);

        pauseRootTable.add(pauseTable).width(TapRunner.WIDTH * 0.5f).center().center();
        pauseRootTable.row();
        pauseRootTable.setVisible(false);

        //hud
        indicatorstable.add(healthLabel).width(80).left().padLeft(2).padBottom(2);
        indicatorstable.row();

        if(prefs.getString("GameMode").equals("My Heart Will Go On")) {
            //TODO:
        } else {
            indicatorstable.add(healthBar).padTop(2);
            indicatorstable.row();

        }

        controlsTable.add(indicatorstable).center().padBottom(10);
        controlsTable.row();
        controlsTable.setBackground(patchDrawableGreen);

        Table buttonsTable;

        buttonsTable = new Table();
        buttonsTable.setFillParent(true);

        audioStateTable.add(audioButton).center();
        audioStateTable.row();
        audioStateTable.pad(10);

        pauseButtonTable.add(pauseButton).center();
        pauseButtonTable.row();
        pauseButtonTable.pad(10);

        buttonsTable.add(audioStateTable);
        buttonsTable.row();
        buttonsTable.add(pauseButtonTable);
        buttonsTable.row();
        buttonsTable.bottom().left();

//        rootTable.add(buttonsTable).expandX().left();
//        rootTable.row();
        rootTable.add(controlsTable).width(180).height(80).expandX().center().pad(50);
        rootTable.row();
        rootTable.left().bottom();
        rootTable.setTouchable(Touchable.disabled);

        stage.addActor(buttonsTable);
        stage.addActor(rootTable);

    }

//    public void showInterstitialAd(){
//        if (game.adsController.isWifiConnected()) {
//            game.adsController.showInterstitialAd(new Runnable() {
//                @Override
//                public void run() {
//                    //show interstitial ad
//                }
//            });
//        }
//    }

    public void setSoundImage() {
        if(prefs.getBoolean("SoundOn")){
            audioButton.setStyle(audioOnButtonStyle);
        }else{
            audioButton.setStyle(audioOffButtonStyle);
        }
    }

    public void healthUpdate(){
        healthBar.setValue(runner.getHealth());
    }

    public void render(float delta){
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose(){
        stage.dispose();
        labelFont.dispose();
        buttonFonts.dispose();
    }
}
