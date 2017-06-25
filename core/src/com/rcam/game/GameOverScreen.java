package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
 * Created by Rod on 5/2/2017.
 */

public class GameOverScreen implements Screen{
    final TapRunner game;
    OrthographicCamera cam;
    Skin cleanCrispySkin,arcadeSkin;
    Stage stage;
    Table rootTable, table;
    Texture bg;
    Ground ground;
    TextButton newGameButton, exitButton, settingsButton, mainMenuButton;
    Runner runner;
    Label current, bestDistance, currentLabel, bestDistanceLabel;
    static Preferences prefs;
    String gameMode;

    public GameOverScreen(final TapRunner gam, final Runner runner){
        this.game = gam;
        this.runner = runner;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        arcadeSkin = new Skin(Gdx.files.internal("skin/arcade-ui/arcade-ui.json"));
        stage = new Stage(new FitViewport(480, 800));
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        prefs = Gdx.app.getPreferences("TapRunner");

        gameMode = prefs.getString("GameMode");

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("Block_Type2_Yellow.png")), 4, 4, 4, 4);

        currentLabel = new Label("Current:", arcadeSkin, "default");
        current = new Label(Integer.toString(runner.indicatePosition()) + " m", arcadeSkin, "default");
        bestDistanceLabel = new Label("Best:", arcadeSkin, "default");
        if(gameMode.equals("The Ground Is Lava")){
            bestDistance = new Label(Integer.toString(runner.getHighScoreLavaMode()) + " m", arcadeSkin, "default");
        }else{
            bestDistance = new Label(Integer.toString(runner.getHighScoreNormalMode()) + " m", arcadeSkin, "default");
        }

        mainMenuButton = new TextButton("Main Menu", cleanCrispySkin, "default");
        mainMenuButtonListener(mainMenuButton, runner);
        newGameButton = new TextButton("New Game", cleanCrispySkin, "default");
        newGameButtonListener(newGameButton, runner);
        settingsButton = new TextButton("Options", cleanCrispySkin, "default");
        settingsButtonListener(settingsButton, runner);
        exitButton = new TextButton("Exit", cleanCrispySkin, "default");
        exitButtonListener(exitButton);
        bg = new Texture("background.png");
        ground = new Ground(cam.position.x - (cam.viewportWidth / 2));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        table.add(bestDistanceLabel).expandX().center().right().uniform().padLeft(30);
        table.add(bestDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(currentLabel).expandX().center().right().uniform().padLeft(30);
        table.add(current).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(newGameButton).colspan(2).width(150).height(50).expandX().padTop(20);
        table.row();
        table.add(settingsButton).colspan(2).width(150).height(50).expandX().padTop(20);
        table.row();
        table.add(mainMenuButton).colspan(2).width(150).height(50).expandX().padTop(30);
        table.row();
        table.add(exitButton).colspan(2).width(150).height(50).expandX().padTop(30).padBottom(30);
        table.row();
        table.center().center().pad(20);
        table.setBackground(new NinePatchDrawable(patch));

        rootTable.add(table);
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);

    }

    public void mainMenuButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                showInterstitialAd();
                loadInterstitialAd();
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });
    }

    public void newGameButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                showInterstitialAd();
                loadInterstitialAd();
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
    }

    public void settingsButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                showInterstitialAd();
                loadInterstitialAd();
                game.setScreen(new SettingsScreen(game));
                return true;
            }
        });
    }

    public void exitButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 40);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 469);
        game.batch.draw(ground.getTexture(), 0, 0);
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
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
        stage.dispose();
        bg.dispose();
        ground.dispose();
    }
}
