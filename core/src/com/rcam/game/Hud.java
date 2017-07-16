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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;


/**
 * Created by Rod on 4/18/2017.
 */

public class Hud extends Table{
    Stage stage;
    Table rootTable, indicatorstable, controlsTable;
    Skin cleanCrispySkin, arcadeSkin;
    PauseButton pauseButton;
    public Health health;
    Label healthLabel, speedMeterLabel;
    GameScreen gameScreen;
    TextureAtlas.AtlasRegion blockYellow, blockYellowGreen;
    NinePatchDrawable patchDrawableGreen, patchDrawableYellow;
    Sound blipSelectSound, newGameblipSound, jumpSound;
    Preferences prefs;
    NinePatch patchGreen, patchYellow;
    BitmapFont labelFont, buttonFonts;
//    StringBuilder distanceValue;
    PauseMenu pauseMenu;
    Hint hint;
    boolean soundOn;
//    Distance distance;

    public Hud(final TapRunner tapRunner, final Runner runner, final GameScreen gameScreen){
        setBounds(0, 0, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        setClip(true);
        this.gameScreen = gameScreen;
        prefs = Gdx.app.getPreferences("TapRunner");
        soundOn = prefs.getBoolean("SoundOn");
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
//        distanceValue = new StringBuilder();
//        distance = new Distance(runner);
        health = new Health(runner);
        pauseButton = new PauseButton(tapRunner);
        pauseMenu = new PauseMenu(tapRunner, pauseButton);
        hint = new Hint();
        labelFont = arcadeSkin.getFont("screen");
        Label.LabelStyle fontStyle = new Label.LabelStyle(labelFont, null);
        healthLabel = new Label("ENERGY", fontStyle);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);

//        stage.setDebugAll(true);

        Gdx.input.setInputProcessor(stage);

        indicatorstable.add(healthLabel).width(80).left().padLeft(2).padBottom(2);
        indicatorstable.row();
        indicatorstable.add(health.getHealthBar()).padTop(2);
        indicatorstable.row();

        controlsTable.add(indicatorstable).center().padBottom(10);
        controlsTable.row();
        controlsTable.add(pauseButton.getPauseButton()).height(30).width(60).center().expandX();
        controlsTable.setBackground(patchDrawableGreen);

        rootTable.add(controlsTable).width(200).padBottom(50).height(100);
        rootTable.row();
        rootTable.center().bottom();

        stage.addActor(rootTable);
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause && !event.getTarget().hasParent()) {
                    if (runner.isOnGround) {
                        if(soundOn) {
                            jumpSound.play();
                        }
                        runner.jump();
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause && !event.getTarget().hasParent()) {
                    runner.isJumping = false;
                }
            }
        });
    }

    public class Hint{
        Table rtable, table;
        Label hint;
        TextButton okay;
        CheckBox hideHint;

        public Hint(){
            table = new Table();
            rtable = new Table();
            rtable.setFillParent(true);
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.up = patchDrawableYellow;
            buttonStyle.down = patchDrawableYellow;
            buttonStyle.font = arcadeSkin.getFont("screen");

            hint = new Label("Tap on screen to jump", arcadeSkin);
            hideHint = new CheckBox("Never show this again", cleanCrispySkin, "default");

            gameScreen.isPause = true;

            if (!prefs.contains("HideHint")) {
                prefs.putBoolean("HideHint", false);
                prefs.flush();
            }

            if(prefs.getBoolean("HideHint")){
                table.setVisible(false);
                gameScreen.isPause = false;
            }else{
                table.setVisible(true);
            }

            okay = new TextButton("Okay", buttonStyle);
            okay.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(soundOn) {
                        blipSelectSound.play();
                    }

                    prefs.putBoolean("HideHint", hideHint.isChecked());
                    prefs.flush();

                    table.setVisible(false);
                    gameScreen.isPause = false;

                    return true;
                }
            });

            table.add(hint).pad(5);
            table.row();
            table.add(hideHint).pad(25);
            table.row();
            table.add(okay).width(70).height(40).pad(10);
            table.row();
            table.setBackground(patchDrawableGreen);

            rtable.add(table).center().center();
            rtable.row();
            stage.addActor(rtable);
        }

    }

//    public class Distance{
//        Label indicator;
//        Runner runr;
//        Table distanceTable, rootTable;
//
//        public Distance(final Runner runner){
//            this.runr = runner;
//            indicator = new Label(getText(), arcadeSkin, "default");
//            distanceTable = new Table();
//            rootTable = new Table();
//            rootTable.setFillParent(true);
//
//            distanceTable.add(indicator).colspan(2);
//            distanceTable.row();
//
//            rootTable.add(distanceTable);
//            rootTable.row();
//            rootTable.setPosition(0, 200, Align.center);
//
//            stage.addActor(rootTable);
//        }
//
//        public StringBuilder getText(){
//            distanceValue.delete(0, distanceValue.length());
//            distanceValue.append(runr.indicatePosition()).append(" m");
//            return distanceValue;
//        }
//
//        public void update(){
//            indicator.setText(getText());
//        }
//    }

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

        public PauseButton(TapRunner game){
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
            pauseButtonListener(pauseButton);
        }

        public void pauseButtonListener(final Button button){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(!gameScreen.isPause) {
                        if(soundOn) {
                            blipSelectSound.play(1.0f);
                        }
                        pauseButton.setStyle(unpauseStyle);
                        gameScreen.isPause = true;
                        pauseMenu.pauseTable.setVisible(true);
                    }else{
                        if(soundOn) {
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

            pauseRootTable.add(pauseTable).width(TapRunner.WIDTH * 0.5f).center().center();
            pauseRootTable.row();
            stage.addActor(pauseRootTable);
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
                    if(soundOn) {
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
                    if(soundOn) {
                        newGameblipSound.play();
                    }
//                    showInterstitialAd();
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
                    if(soundOn) {
                        blipSelectSound.play();
                    }
                    showInterstitialAd();
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
