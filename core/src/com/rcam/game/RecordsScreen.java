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
 * Created by Rod on 6/18/2017.
 */

public class RecordsScreen implements Screen{
    final TapRunner game;
    OrthographicCamera cam;
    Skin cleanCrispySkin,arcadeSkin;
    Stage stage;
    Table rootTable, table, labelTable, goBackButtonTable;
    Texture bg;
    Ground ground;
    TextButton goBackButton;
    Runner runner;
    Label bestLabel, bestNormalLabel, bestLavaLabel, recordsLabel, bestNormalDistance, bestLavaDistance;
    static Preferences prefs;

    public RecordsScreen(final TapRunner gam){
        this.game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        arcadeSkin = new Skin(Gdx.files.internal("skin/arcade-ui/arcade-ui.json"));
        stage = new Stage(new FitViewport(480, 800));
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        goBackButtonTable = new Table();
        labelTable = new Table();
        prefs = Gdx.app.getPreferences("TapRunner");

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("Block_Type2_Yellow.png")), 4, 4, 4, 4);

        recordsLabel = new Label("Records", arcadeSkin, "default");
        bestLabel = new Label("Best Distance", arcadeSkin, "default");
        bestNormalLabel = new Label("Normal Mode: ", cleanCrispySkin, "default");
        bestNormalDistance = new Label(Integer.toString(prefs.getInteger("BestDistanceNormalMode")) + " m", arcadeSkin, "default");
        bestLavaLabel = new Label("The Ground is Lava: ", cleanCrispySkin, "default");
        bestLavaDistance = new Label(Integer.toString(prefs.getInteger("BestDistanceLavaMode")) + " m", arcadeSkin, "default");
        goBackButton = new TextButton("Main Menu", cleanCrispySkin, "default");
        goBackButtonListener(goBackButton);
        bg = new Texture("bg.png");
        ground = new Ground(cam.position.x - (cam.viewportWidth / 2));
        Gdx.input.setInputProcessor(stage);

//        stage.setDebugAll(true);


        labelTable.add(recordsLabel).colspan(2).expandX().center().uniform().pad(5);
        labelTable.row();
        labelTable.setBackground(new NinePatchDrawable(patch));

        table.add(bestLabel).colspan(2).expandX().center().center().uniform().padBottom(15);
        table.row();
        table.add(bestNormalLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestNormalDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.add(bestLavaLabel).expandX().center().right().uniform().padLeft(10);
        table.add(bestLavaDistance).expandX().center().left().padLeft(10).uniform();
        table.row();
        table.pad(20, 20, 30, 20);
        table.setBackground(new NinePatchDrawable(patch));

        goBackButtonTable.add(goBackButton).colspan(2).width(150).height(50).expandX().pad(15);
        goBackButtonTable.row();
        goBackButtonTable.center().center();
        goBackButtonTable.setBackground(new NinePatchDrawable(patch));


        rootTable.add(labelTable).fillX();
        rootTable.row();
        rootTable.add(table);
        rootTable.row();
        rootTable.add(goBackButtonTable).fillX();
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);

    }

    public void goBackButtonListener(TextButton button){
        button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
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
