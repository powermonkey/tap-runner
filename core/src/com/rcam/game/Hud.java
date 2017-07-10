package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;


/**
 * Created by Rod on 4/18/2017.
 */

public class Hud{
    Stage stage;
    Table rootTable, indicatorstable, controlsTable;
    Skin cleanCrispySkin, arcadeSkin;
    PauseButton pauseButton;
    JumpButton jumpButton;
    public Health health;
    Label healthLabel, speedMeterLabel;
    GameScreen gameScreen;
    TextureAtlas.AtlasRegion blockYellow, blockYellowGreen;
    NinePatchDrawable patchDrawableGreen, patchDrawableYellow;
    Sound blipSelectSound, newGameblipSound, jumpSound, speedAdjustSound;
    Preferences prefs;
    NinePatch patchGreen, patchYellow;
    BitmapFont labelFont, buttonFonts;
    StringBuilder distanceValue;
    PauseMenu pauseMenu;

    public Hud(final TapRunner tapRunner, final Runner runner, final GameScreen gameScreen){
//        setBounds(0, 0, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
//        setClip(true);
        this.gameScreen = gameScreen;
        blockYellow = GameAssetLoader.blockYellow;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        patchDrawableGreen = new NinePatchDrawable(patchGreen);
        patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        patchDrawableYellow = new NinePatchDrawable(patchYellow);
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        blipSelectSound = GameAssetLoader.blipSelect;
        jumpSound = GameAssetLoader.jump;
        newGameblipSound = GameAssetLoader.newGameblip;
        buttonFonts = GameAssetLoader.buttonFonts;
        rootTable = new Table();
        indicatorstable = new Table();
        controlsTable = new Table();
        rootTable.setFillParent(true);
        stage = new Stage(new FitViewport(480, 800));
        distanceValue = new StringBuilder();
        health = new Health(runner);
        pauseButton = new PauseButton(tapRunner, gameScreen);
        pauseMenu = new PauseMenu(tapRunner, pauseButton);
        jumpButton = new JumpButton(runner);
        labelFont = arcadeSkin.getFont("screen");
        Label.LabelStyle fontStyle = new Label.LabelStyle(labelFont, null);
        healthLabel = new Label("ENERGY", fontStyle);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);
        prefs = Gdx.app.getPreferences("TapRunner");

//        stage.setDebugAll(true);

        Gdx.input.setInputProcessor(stage);

        indicatorstable.add(healthLabel).width(80).left().padLeft(2).padBottom(2);
        indicatorstable.row();
        indicatorstable.add(health.getHealthBar()).padTop(2);
        indicatorstable.row();

        controlsTable.add(indicatorstable).left().padLeft(20);
        controlsTable.add(pauseButton.getPauseButton()).height(30).width(60).expandX().padLeft(30);
        controlsTable.add(jumpButton.getJumpButton()).width(85).height(85).right().padRight(25).expandX();
        controlsTable.row();
        controlsTable.setBackground(patchDrawableGreen);

        rootTable.add(controlsTable).width(TapRunner.WIDTH).padBottom(20).height(120).expandX();
        rootTable.row();
        rootTable.center().bottom();
        stage.addActor(rootTable);
    }

    public class Health{
        ProgressBar healthBar;
        Runner runr;

        public Health(final Runner runner){
            healthBar = new ProgressBar(1, 50, 1, false, getCleanCrispySkin(), "default-horizontal");
            healthBar.setAnimateDuration(.5f);
            this.runr = runner;
            healthBar.setValue(runr.getHealth());
        }

        public ProgressBar getHealthBar() {
            return healthBar;
        }

        public void update(){
            healthBar.setValue(runr.getHealth());
        }
    }

    public class PauseButton{
        ImageButton pauseButton;
        ImageButton.ImageButtonStyle buttonStyle, unpauseStyle;
        TapRunner game;

        public PauseButton(TapRunner game, GameScreen gameScreen){
            this.game = game;
            buttonStyle = new ImageButton.ImageButtonStyle();
            unpauseStyle = new ImageButton.ImageButtonStyle();

            Skin buttonSkin = new Skin(GameAssetLoader.atlas);

            pauseButton = new ImageButton(buttonStyle);

            buttonStyle.up = patchDrawableYellow;
            buttonStyle.down = patchDrawableYellow;
            buttonStyle.imageUp = buttonSkin.getDrawable("pause");
            buttonStyle.imageDown = buttonSkin.getDrawable("pause");

            unpauseStyle.up = patchDrawableYellow;
            unpauseStyle.down = patchDrawableYellow;
            unpauseStyle.imageUp = buttonSkin.getDrawable("forward");
            unpauseStyle.imageDown = buttonSkin.getDrawable("forward");

            pauseButton.setStyle(buttonStyle);
            pauseButtonListener(pauseButton, gameScreen);
        }

        public void pauseButtonListener(final Button button, final GameScreen gameScreen){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(!gameScreen.isPause) {
                        if(prefs.getBoolean("SoundOn")) {
                            blipSelectSound.play(1.0f);
                        }
                        pauseButton.setStyle(unpauseStyle);
                        gameScreen.isPause = true;
                        pauseMenu.pauseTable.setVisible(true);
                    }else{
                        if(prefs.getBoolean("SoundOn")) {
                            blipSelectSound.play(1.0f);
                        }
                        pauseButton.setStyle(buttonStyle);
                        gameScreen.isPause = false;
                        pauseMenu.pauseTable.setVisible(false);
                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                }
            });
        }

        public ImageButton getPauseButton(){
            return pauseButton;
        }
    }

    public class PauseMenu{
        Table pauseTable, pauseRootTable;
        TextButton continueButton, newGameButton, mainMenuButton, exitButton;
        TapRunner game;
        PauseButton pauseButton;

        public PauseMenu(TapRunner tapRunner, final PauseButton pauseButton){
            this.game = tapRunner;
            this.pauseButton = pauseButton;
            pauseRootTable = new Table();
            pauseRootTable.setFillParent(true);
            pauseTable = new Table();

            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.up = patchDrawableYellow;
            buttonStyle.down = patchDrawableYellow;
            buttonStyle.font = buttonFonts;

            continueButton = new TextButton("Continue", buttonStyle);
            continueButtonListener(continueButton);

            newGameButton = new TextButton("New Game", buttonStyle);
            newGameButtonListener(newGameButton);

            mainMenuButton = new TextButton("Main Menu", buttonStyle);
            mainMenuButtonListener(mainMenuButton);

            exitButton = new TextButton("Exit", buttonStyle);
            exitGameButtonListener(exitButton);

            pauseTable.setVisible(false);
            pauseTable.add(continueButton).center().uniform().width(200).height(50).expandX().padTop(20);
            pauseTable.row();
            pauseTable.add(newGameButton).center().uniform().width(200).height(50).expandX().padTop(10);
            pauseTable.row();
            pauseTable.add(mainMenuButton).center().uniform().width(200).height(50).expandX().padTop(10);
            pauseTable.row();
            pauseTable.add(exitButton).center().uniform().width(200).height(50).expandX().padTop(10).padBottom(30);
            pauseTable.row();
            pauseTable.setBackground(patchDrawableGreen);

            pauseRootTable.add(pauseTable).width(TapRunner.WIDTH / 2).center().center().expandX();
            pauseRootTable.row();
            stage.addActor(pauseRootTable);
        }

        public void loadInterstitialAd(){
            if (game.adsController.isWifiConnected()) {
                game.adsController.loadInterstitialAd(new Runnable() {
                    @Override
                    public void run() {
                        //load new interstitial ad after closing ad
                    }
                });
            }
        }

        public void showInterstitialAd(){
            if (game.adsController.isWifiConnected()) {
                game.adsController.showInterstitialAd(new Runnable() {
                    @Override
                    public void run() {
                        //show interstitial ad
                    }
                });
            }
        }

        public void continueButtonListener(final Button button){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(prefs.getBoolean("SoundOn")) {
                        blipSelectSound.play();
                    }
                    pauseButton.getPauseButton().setStyle(pauseButton.buttonStyle);
                    gameScreen.isPause = false;
                    pauseTable.setVisible(false);
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                }
            });
        }

        public void newGameButtonListener(final Button button){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(prefs.getBoolean("SoundOn")) {
                        newGameblipSound.play();
                    }
//                    showInterstitialAd();
//                    loadInterstitialAd();
                    pauseTable.setVisible(false);
                    game.setScreen(new GameScreen(game));
                    dispose();
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                }
            });
        }

        public void exitGameButtonListener(final Button button){
            button.addListener(new InputListener(){
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
        }

        public void mainMenuButtonListener(final Button button){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(prefs.getBoolean("SoundOn")) {
                        blipSelectSound.play();
                    }
                    showInterstitialAd();
                    loadInterstitialAd();
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                }
            });
        }

    }

    public class JumpButton{
        Button button;

        public JumpButton(Runner runner){
            button = new Button(getArcadeSkin(), "yellow");
            button.setSize(70, 70);
            buttonListener(button, runner);
        }

        public void buttonListener(final Button button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(!gameScreen.isPause) {
                        if (runner.isOnGround) {
                            if(prefs.getBoolean("SoundOn")) {
                                jumpSound.play();
                            }
                            runner.jump();
                        }
                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    runner.isJumping = false;
                }
            });
        }

        public Button getJumpButton(){
            return button;
        }

    }

    public Skin getArcadeSkin(){
        return arcadeSkin;
    }

    public Skin getCleanCrispySkin(){
        return cleanCrispySkin;
    }

    public void render(){
        stage.act();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        labelFont.dispose();
        buttonFonts.dispose();
    }
}
