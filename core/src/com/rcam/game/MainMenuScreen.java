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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Lava;

/**
 * Created by Rod on 4/14/2017.
 */

public class MainMenuScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Table rootTable, table, titleTable, titleTableInner, tableAudioState;
    TextButton exit, newGame, options, records, credits, moreGames, privacy;
    ImageTextButton rate;
    Stage stage;
    static Preferences prefs;
    TextureAtlas.AtlasRegion ground, bg, blockYellow, blockYellowGreen;
    TextureRegion runnerJump;
    Sound blipSelectSound, newGameblipSound;
    Boolean soundOn;
    Label title;
    Image runner;
    BitmapFont myFont, buttonFonts;
    String gameMode;
    Lava lava;
    TextureAtlas.AtlasRegion star;

    public MainMenuScreen(final TapRunner gam){
        game = gam;
        bg = GameAssetLoader.bg;
        ground = GameAssetLoader.ground;
        blockYellow = GameAssetLoader.blockYellow;
        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        blipSelectSound = GameAssetLoader.blipSelect;
        newGameblipSound = GameAssetLoader.newGameblip;
        runnerJump = GameAssetLoader.regionJump;
        myFont = GameAssetLoader.fonts;
        buttonFonts = GameAssetLoader.buttonFonts;
        star = GameAssetLoader.star;

        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH * 0.5f, TapRunner.HEIGHT * 0.5f  + 50);
        stage = new Stage(new FitViewport(480, 800), game.batch);
        Gdx.input.setInputProcessor(stage);
        rootTable = new Table();
        table = new Table();
        titleTable = new Table();
        titleTableInner = new Table();
        tableAudioState = new Table();
        rootTable.setFillParent(true);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = myFont;

        runner = new Image(runnerJump);

        title = new Label("Happy Runnings", labelStyle);
        title.setAlignment(Align.center);
        NinePatch patchYellow = new NinePatch(blockYellow, 4, 4, 4, 4);
        NinePatch patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        NinePatchDrawable patchDrawableYellow = new NinePatchDrawable(patchYellow);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

        buttonStyle.up = patchDrawableYellow;
        buttonStyle.down = patchDrawableYellow;
        buttonStyle.font = buttonFonts;

        newGame = new TextButton("New Game", buttonStyle);
        options = new TextButton("Game Modes", buttonStyle);
        records = new TextButton("Records", buttonStyle);
        credits = new TextButton("Credits", buttonStyle);
        privacy = new TextButton("Privacy Policy", buttonStyle);
        exit = new TextButton("Exit", buttonStyle);
        moreGames = new TextButton("More Games", buttonStyle);

        ImageTextButton.ImageTextButtonStyle rateButtonStyle = new ImageTextButton.ImageTextButtonStyle();
        rateButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(star));
        rateButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(star));
        rateButtonStyle.up = patchDrawableYellow;
        rateButtonStyle.down = patchDrawableYellow;
        rateButtonStyle.font = buttonFonts;
        rate = new ImageTextButton("Rate", rateButtonStyle);
        rate.clearChildren();
        rate.add(rate.getLabel());
        rate.add(rate.getImage());

        newGameButtonListener(newGame);
        settingsButtonListener(options);
        recordsButtonListener(records);
        creditsButtonListener(credits);
        privacyButtonListener(privacy);
        exitButtonListener(exit);
        rateButtonListener(rate);
        moreGamesButtonListener(moreGames);

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

        table.add(newGame).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(options).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(records).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(credits).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(moreGames).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(rate).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(privacy).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.add(exit).center().uniform().width(300).height(50).expandX().padTop(10);
        table.row();
        table.center().center();
        table.pad(20);
        table.setBackground(patchDrawableGreen);

        rootTable.add(titleTable);
        rootTable.row();
        rootTable.add(table).width(350).pad(20);
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

        gameMode = prefs.getString("GameMode");
        lava = new Lava(cam.position.x - (cam.viewportWidth * 0.5f));
    }

    public void newGameButtonListener(TextButton button){
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

    public void settingsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(soundOn) {
                    blipSelectSound.play();
                }
                game.setScreen(new SettingsScreen(game));
                dispose();
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
                dispose();
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
                dispose();
                return true;
            }
        });
    }

    public void rateButtonListener(ImageTextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.rcam.game");

                return true;
            }
        });
    }

    public void moreGamesButtonListener(TextButton button) {
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://play.google.com/store/apps/developer?id=Rodolfo+C.+Cam+II");

                return true;
            }
        });
    }

    public void privacyButtonListener(TextButton button) {
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://sites.google.com/view/happy-runnings-game/privacy-policy");

                return true;
            }
        });
    }

    public void exitButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                GameAssetLoader.dispose();
                Gdx.app.exit();
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
        if(gameMode.equals("The Ground Is Lava") || gameMode.equals("First Degree Burn") || gameMode.equals("Burn Baby Burn")){
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
        myFont.dispose();
        buttonFonts.dispose();
//        GameAssetLoader.dispose();
    }
}
