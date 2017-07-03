package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;


/**
 * Created by Rod on 4/18/2017.
 */

public class Hud extends Table{
    Stage stage;
    Table rootTable, indicatorstable, distancetable, controlsTable;
    Skin cleanCrispySkin, arcadeSkin;
    Meter meter;
    RunButton runButton;
    SlowDownButton slowDownButton;
    PauseButton pauseButton;
    JumpButton jumpButton;
    Distance distance;
    Health health;
    Label healthLabel, speedMeterLabel;
    GameScreen gameScreen;
    TextureAtlas.AtlasRegion blockYellow;
    NinePatchDrawable patchDrawable;
    Sound blipSelectSound, newGameblipSound, jumpSound, speedAdjustSound;
    Preferences prefs;


    public Hud(final TapRunner tapRunner, final Runner runner, final GameScreen gameScreen){
        setBounds(0, 0, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        setClip(true);
        this.gameScreen = gameScreen;
        blockYellow = GameAssetLoader.atlas.findRegion("Block_Type2_Yellow");
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        blipSelectSound = GameAssetLoader.blipSelect;
        jumpSound = GameAssetLoader.jump;
        speedAdjustSound = GameAssetLoader.speedAdjust;
        newGameblipSound = GameAssetLoader.newGameblip;
        rootTable = new Table();
        indicatorstable = new Table();
        distancetable = new Table();
        controlsTable = new Table();
        rootTable.setFillParent(true);
        stage = new Stage(new FitViewport(480, 800));
        meter = new Meter(runner);
        distance = new Distance(runner);
        health = new Health(runner);
        runButton = new RunButton(runner);
        slowDownButton = new SlowDownButton(runner);
        pauseButton = new PauseButton(tapRunner, gameScreen);
        jumpButton = new JumpButton(runner);
        healthLabel = new Label("ENERGY", cleanCrispySkin);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);
        prefs = Gdx.app.getPreferences("TapRunner");


//        stage.setDebugAll(true);

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);
        patchDrawable = new NinePatchDrawable(patch);
        Gdx.input.setInputProcessor(stage);

        distancetable.add(distance.getIndicator()).padBottom(120).colspan(2).expand().center().center();
        distancetable.row();

        indicatorstable.add(healthLabel).width(80).padLeft(40);
        indicatorstable.add(health.getHealthBar()).padRight(20);
        indicatorstable.row();
        indicatorstable.add(speedMeterLabel).width(80).padLeft(40);
        indicatorstable.add(meter.getSpeedMeter()).left().padLeft(2);
        indicatorstable.row();

        controlsTable.add(slowDownButton.getSlowDownButton()).padLeft(20).padRight(20).left();
        controlsTable.add(runButton.getRunButton()).left();
        controlsTable.add(pauseButton.getPauseButton()).height(30).width(60).expandX().right();
        controlsTable.add(jumpButton.getJumpButton()).right().padRight(20).expandX();
        controlsTable.row();
        controlsTable.setBackground(patchDrawable);

        rootTable.add(distancetable).width(TapRunner.WIDTH).center().expand();
        rootTable.row();
        rootTable.add(indicatorstable).width(TapRunner.WIDTH).bottom().expandX();
        rootTable.row();
        rootTable.add(controlsTable).width(TapRunner.WIDTH).bottom().height(130).expandX();
        rootTable.row();
        stage.addActor(rootTable);
    }

    public class Meter{
        Label currentIndicator;
        Label.LabelStyle style;
        BitmapFont labelFont;
        Runner runner;
        public Meter(final Runner runner){
            this.runner = runner;
            labelFont = getArcadeSkin().getFont("screen");
            labelFont.getData().markupEnabled = true;
            style = new Label.LabelStyle(labelFont, null);
            currentIndicator = new Label("[#2a2a2a]1[] [#2a2a2a]2[] [#2a2a2a]MAX![]", style);
        }

        public Label getSpeedMeter() {
            return currentIndicator;
        }

        public void update(float speed){
            if(speed <= runner.MAX_SPEED - 100) {
                currentIndicator.setText("[#ffffff]1[] [#2a2a2a]2[] [#2a2a2a]MAX![]");
            }else if(speed <= runner.MAX_SPEED - 50) {
                currentIndicator.setText("[#2a2a2a]1[] [#ffffff]2[] [#2a2a2a]MAX![]");
            }else if(speed == runner.MAX_SPEED) {
                currentIndicator.setText("[#2a2a2a]1[] [#2a2a2a]2[] [#ffffff]MAX![]");
            }
        }
    }

    public class Health{
        ProgressBar healthBar;
        Runner runr;

        public Health(final Runner runner){
            healthBar = new ProgressBar(1, 50, 1, false, getCleanCrispySkin(), "default-horizontal");
            healthBar.setAnimateDuration(.5f);
            this.runr = runner;
        }

        public ProgressBar getHealthBar() {
            return healthBar;
        }

        public void update(){
            healthBar.setValue(runr.getHealth());
        }
    }

    public class Distance{
        Label indicator;
        Runner runr;

        public Distance(final Runner runner){
            this.runr = runner;
            indicator = new Label(getText(), getArcadeSkin(), "default");
        }

        public Label getIndicator(){
            return indicator;
        }

        public String getText(){
            return Integer.toString(runr.indicatePosition()).concat(" m");
        }

        public void update(){
            indicator.setText(getText());
        }
    }

    public class SlowDownButton{
        ImageButton sbutton;
        ImageButton.ImageButtonStyle sbuttonStyle;

        public SlowDownButton(Runner runner){
            sbutton = new ImageButton(getCleanCrispySkin(), "default");
            sbuttonStyle = new ImageButton.ImageButtonStyle();
            Skin buttonSkin = new Skin(GameAssetLoader.atlas);
            sbuttonStyle.up = getCleanCrispySkin().getDrawable("button-c");
            sbuttonStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            sbuttonStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
            sbuttonStyle.imageUp = buttonSkin.getDrawable("backward");;
            sbuttonStyle.imageDown = buttonSkin.getDrawable("backward");
            sbutton.setStyle(sbuttonStyle);
            button1Listener(sbutton, runner);
        }

        public void button1Listener(final ImageButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause) {
                    if(prefs.getBoolean("SoundOn")) {
                        speedAdjustSound.play();
                    }
                    runner.slowDown();
                }
                return true;
                }
            });
        }

        public ImageButton getSlowDownButton(){
            return sbutton;
        }

    }

    public class RunButton{
        ImageButton rbutton;
        ImageButton.ImageButtonStyle rbuttonStyle;

        public RunButton(Runner runner){
            rbutton = new ImageButton(getCleanCrispySkin(), "default");
            rbuttonStyle = new ImageButton.ImageButtonStyle();
            Skin buttonSkin = new Skin(GameAssetLoader.atlas);
            rbuttonStyle.up = getCleanCrispySkin().getDrawable("button-c");
            rbuttonStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            rbuttonStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
            rbuttonStyle.imageUp = buttonSkin.getDrawable("forward");
            rbuttonStyle.imageDown = buttonSkin.getDrawable("forward");

            rbutton.setStyle(rbuttonStyle);

            button1Listener(rbutton, runner);
        }

        public void button1Listener(final ImageButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(!gameScreen.isPause) {
                    if(prefs.getBoolean("SoundOn")) {
                        speedAdjustSound.play();
                    }
                    runner.run();
                }
                return true;
                }
            });
        }

        public ImageButton getRunButton(){
            return rbutton;
        }

    }

    public class PauseButton{
        ImageButton pauseButton;
        ImageButton.ImageButtonStyle buttonStyle, unpauseStyle;
        TapRunner game;
        Group pauseGroup;
        Table rtable;

        public PauseButton(TapRunner game, GameScreen gameScreen){
            this.game = game;
            pauseButton = new ImageButton(getCleanCrispySkin(), "default");
            buttonStyle = new ImageButton.ImageButtonStyle();
            unpauseStyle = new ImageButton.ImageButtonStyle();

            Skin buttonSkin = new Skin(GameAssetLoader.atlas);

            buttonStyle.up = getCleanCrispySkin().getDrawable("button-c");
            buttonStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            buttonStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
            buttonStyle.imageUp = buttonSkin.getDrawable("pause");
            buttonStyle.imageDown = buttonSkin.getDrawable("pause");

            unpauseStyle.up = getCleanCrispySkin().getDrawable("button-c");
            unpauseStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            unpauseStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
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
                        pauseGame();
                    }else{
                        if(prefs.getBoolean("SoundOn")) {
                            blipSelectSound.play(1.0f);
                        }
                        pauseButton.setStyle(buttonStyle);
                        gameScreen.isPause = false;
                        rtable.remove();
                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                }
            });
        }

        public void pauseGame(){
            Table pauseTable;
            TextButton continueButton, newGameButton, mainMenuButton, exitButton;
            rtable = new Table();
            rtable.setFillParent(true);
            pauseTable = new Table();
            pauseGroup = new Group();

            continueButton = new TextButton("Continue", cleanCrispySkin, "default");
            continueButtonListener(continueButton);

            newGameButton = new TextButton("New Game", cleanCrispySkin, "default");
            newGameButtonListener(newGameButton);

            mainMenuButton = new TextButton("Main Menu", cleanCrispySkin, "default");
            mainMenuButtonListener(mainMenuButton);

            exitButton = new TextButton("Exit", cleanCrispySkin, "default");
            exitGameButtonListener(exitButton);

            pauseTable.add(continueButton).center().uniform().width(150).height(50).expandX().padTop(30);
            pauseTable.row();
            pauseTable.add(newGameButton).center().uniform().width(150).height(50).expandX().padTop(30);
            pauseTable.row();
            pauseTable.add(mainMenuButton).center().uniform().width(150).height(50).expandX().padTop(30);
            pauseTable.row();
            pauseTable.add(exitButton).center().uniform().width(150).height(50).expandX().padTop(30).padBottom(30);
            pauseTable.row();
            pauseTable.setBackground(patchDrawable);

            rtable.add(pauseTable).width(TapRunner.WIDTH / 2).center().center().expandX();
            rtable.row();
            rtable.row();
            pauseGroup.addActor(rtable);
            stage.addActor(rtable);
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
                    pauseButton.setStyle(buttonStyle);
                    gameScreen.isPause = false;
                    rtable.remove();
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
                    game.setScreen(new GameScreen(game));
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

    public class JumpButton{
        TextButton button;

        public JumpButton(Runner runner){
            button = new TextButton("JUMP", getCleanCrispySkin(), "arcade");
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

        public TextButton getJumpButton(){
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
        GameAssetLoader.dispose();
    }

}
