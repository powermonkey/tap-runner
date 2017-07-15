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
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Ground;
import com.rcam.game.sprites.Runner;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;

/**
 * Created by Rod on 5/2/2017.
 */

public class GameOverScreen implements Screen{
    final TapRunner game;
    OrthographicCamera cam;
    Skin cleanCrispySkin,arcadeSkin;
    Stage stage;
    Table rootTable, table;
    Ground ground;
    TextButton newGameButton, exitButton, settingsButton, mainMenuButton;
    Runner runner;
    Label current, bestDistance, currentLabel, bestDistanceLabel;
    static Preferences prefs;
    String gameMode;
    TextureAtlas.AtlasRegion bg, blockYellow, blockYellowGreen;
    Sound blipSelectSound, newGameblipSound;
    BitmapFont buttonFonts;
    Long gameOverScreenStart, adTimer;
    boolean soundOn;
    StringBuilder currentValue, bestValue;

    public GameOverScreen(final TapRunner gam, final Runner runner){
        this.game = gam;
        this.runner = runner;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH * 0.5f, TapRunner.HEIGHT * 0.5f + 50);
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        stage = new Stage(new FitViewport(480, 800));
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        currentValue = new StringBuilder();
        bestValue = new StringBuilder();
        bg = GameAssetLoader.bg;
        blockYellow = GameAssetLoader.blockYellow;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        blipSelectSound = GameAssetLoader.blipSelect;
        newGameblipSound = GameAssetLoader.newGameblip;
        prefs = Gdx.app.getPreferences("TapRunner");
        gameMode = prefs.getString("GameMode");
        soundOn = prefs.getBoolean("SoundOn");


        NinePatch patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        NinePatch patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        NinePatchDrawable patchDrawableYellow = new NinePatchDrawable(patchYellow);

        buttonFonts = GameAssetLoader.buttonFonts;
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = patchDrawableGreen;
        buttonStyle.down = patchDrawableGreen;
        buttonStyle.font = buttonFonts;

        currentLabel = new Label("Current:", arcadeSkin, "default");
        current = new Label(currentValue.append(runner.indicatePosition()).append(" m"), arcadeSkin, "default");
        bestDistanceLabel = new Label("Best:", arcadeSkin, "default");
        if(gameMode.equals("The Ground Is Lava")){
            bestDistance = new Label(bestValue.append(runner.getHighScoreLavaMode()).append(" m"), arcadeSkin, "default");
        }else{
            bestDistance = new Label(bestValue.append(runner.getHighScoreNormalMode()).append(" m"), arcadeSkin, "default");
        }

        mainMenuButton = new TextButton("Main Menu", buttonStyle);
        mainMenuButtonListener(mainMenuButton, runner);
        newGameButton = new TextButton("New Game", buttonStyle);
        newGameButtonListener(newGameButton, runner);
        settingsButton = new TextButton("Options", buttonStyle);
        settingsButtonListener(settingsButton, runner);
        exitButton = new TextButton("Exit", buttonStyle);
        exitButtonListener(exitButton);

        ground = new Ground(cam.position.x - (cam.viewportWidth * 0.5f));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        table.add(bestDistanceLabel).expandX().center().right().uniform().padLeft(30);
        table.add(bestDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(currentLabel).expandX().center().right().uniform().padLeft(30);
        table.add(current).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(newGameButton).colspan(2).width(200).height(50).expandX().padTop(20);
        table.row();
        table.add(settingsButton).colspan(2).width(200).height(50).expandX().padTop(4);
        table.row();
        table.add(mainMenuButton).colspan(2).width(200).height(50).expandX().padTop(4);
        table.row();
        table.add(exitButton).colspan(2).width(200).height(50).expandX().padTop(4).padBottom(30);
        table.row();
        table.center().center().pad(20);
        table.setBackground(patchDrawableYellow);

        rootTable.add(table);
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);
        gameOverScreenStart = millis();
        adTimer = 1000l;
    }

    public void mainMenuButtonListener(TextButton button, final Runner runner){
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
        });
    }

    public void newGameButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    newGameblipSound.play();
                }
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }
        });
    }

    public void settingsButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    blipSelectSound.play();
                }
                showInterstitialAd();
                game.setScreen(new SettingsScreen(game));
                dispose();
                return true;
            }
        });
    }

    public void exitButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                GameAssetLoader.dispose();
                Gdx.app.exit();
                return true;
            }
        });
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.disableBlending();
        game.batch.begin();
        if(timeSinceMillis(gameOverScreenStart) > adTimer){
            gameOverScreenStart = millis();
            showInterstitialAd();
            adTimer = 8000l;
        }
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 459);
        game.batch.draw(ground.getTextureGround(), 0, 0);
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
        buttonFonts.dispose();
    }
}
