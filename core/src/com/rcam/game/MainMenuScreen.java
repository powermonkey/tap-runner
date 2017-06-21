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

/**
 * Created by Rod on 4/14/2017.
 */

public class MainMenuScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Texture bg, ground;
    Table rootTable, table;
    Skin cleanCrispySkin;
    TextButton exit, newGame, options, records;
    Stage stage;
    static Preferences prefs;

    public MainMenuScreen(final TapRunner gam){
        game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        stage = new Stage(new FitViewport(480, 800));
        Gdx.input.setInputProcessor(stage);
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();

        newGame = new TextButton("New Game", cleanCrispySkin, "default");
        options = new TextButton("Options", cleanCrispySkin, "default");
        records = new TextButton("Records", cleanCrispySkin, "default");
        exit = new TextButton("Exit", cleanCrispySkin, "default");

        newGameButtonListener(newGame);
        settingsButtonListener(options);
        recordsButtonListener(records);
        exitButtonListener(exit);

//        stage.setDebugAll(true);

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("Block_Type2_Yellow.png")), 4, 4, 4, 4);

        table.add(newGame).center().uniform().width(150).height(50).expandX().padTop(30);
        table.row();
        table.add(options).center().uniform().width(150).height(50).expandX().padTop(30);
        table.row();
        table.add(records).center().uniform().width(150).height(50).expandX().padTop(30);
        table.row();
        table.add(exit).center().uniform().width(150).height(50).expandX().padTop(30).padBottom(30);
        table.row();
        table.center().center().pad(20);
        table.setBackground(new NinePatchDrawable(patch));

        rootTable.add(table).width(TapRunner.WIDTH / 2);
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);

        prefs = Gdx.app.getPreferences("TapRunner");
        //set default game mode to normal
        if (!prefs.contains("GameMode")) {
            prefs.putString("GameMode", "Normal");
            prefs.flush();
        }

    }

    public void newGameButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });
    }

    public void settingsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
                return true;
            }
        });
    }

    public void recordsButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new RecordsScreen(game));
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);

        game.batch.begin();
        game.batch.draw(bg, 0, 0);
//        game.batch.draw(playBtn, (cam.viewportWidth / 2 ) - (playBtn.getWidth() / 2 ), cam.viewportHeight / 2);
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

    }
}
