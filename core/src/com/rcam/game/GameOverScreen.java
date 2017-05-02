package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    Table table;
    Texture bg;
    Ground ground;
    TextButton newGameButton, exitButton;
    Runner runner;
    Label distance, bestDistance, distanceLabel, bestDistanceLabel;

    public GameOverScreen(final TapRunner gam, final Runner runner){
        this.game = gam;
        this.runner = runner;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        arcadeSkin = new Skin(Gdx.files.internal("skin/arcade-ui/arcade-ui.json"));
        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        distanceLabel = new Label("Distance:", arcadeSkin, "default");
        distance = new Label(Integer.toString(runner.indicatePosition()) + " m", arcadeSkin, "default");
        bestDistanceLabel = new Label("Best:", arcadeSkin, "default");
        bestDistance = new Label(Integer.toString(runner.getHighScore()) + " m", arcadeSkin, "default");
        newGameButton = new TextButton("New Game", cleanCrispySkin, "default");
        newGameButtonListener(newGameButton, runner);
        exitButton = new TextButton("Exit", cleanCrispySkin, "default");
        exitButtonListener(exitButton);
        bg = new Texture("bg.png");
        ground = new Ground(cam.position.x - (cam.viewportWidth / 2));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);

        table.add(distanceLabel).expandX().center().right().uniform().padLeft(30);
        table.add(distance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestDistanceLabel).expandX().center().right().uniform().padLeft(30);
        table.add(bestDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(newGameButton).colspan(2).width(150).height(50).expandX().padTop(40);
        table.row();
        table.add(exitButton).colspan(2).width(150).height(50).expandX();
        table.row();
        table.center().center();
        stage.addActor(table);

    }

    public void newGameButtonListener(TextButton button, final Runner runner){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
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

        //distance reached
        //best
        //new game
        //exit
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
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
    }
}
