package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Rod on 7/3/2017.
 */

public class CreditsScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Table rootTable, table, table2, table3, table4, table5, table6, table7, table8;
    Skin arcadeSkin, cleanCrispySkin;
    Stage stage;
    TextureAtlas.AtlasRegion ground, bg, blockYellow;
    TextButton goBackButton;

    public CreditsScreen(final TapRunner gam){
        game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);
        bg = GameAssetLoader.atlas.findRegion("background");
        ground = GameAssetLoader.atlas.findRegion("ground");
        arcadeSkin = GameAssetLoader.arcadeSkin;
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        blockYellow = GameAssetLoader.atlas.findRegion("Block_Type2_Yellow");

        stage = new Stage(new FitViewport(480, 800));
        Gdx.input.setInputProcessor(stage);
        rootTable = new Table();
        rootTable.setFillParent(true);
        table = new Table();
        table2 = new Table();
        table3 = new Table();
        table4 = new Table();
        table5 = new Table();
        table6 = new Table();
        table7 = new Table();
        table8 = new Table();

        //runner & background texture
        Label runnerAndBgLabel = new Label("Runner & Background Texture by ", arcadeSkin, "screen");
        Label runnerAndBgCredit = new Label("GrafxKid", arcadeSkin, "screen");
        Label runnerAndBgLicense = new Label("CC-BY 3.0", arcadeSkin, "screen");
        runnerAndBgCredit.setColor(Color.BLUE);
        runnerAndBgLicense.setColor(Color.BLUE);

        runnerAndBgCredit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://grafxkid.tumblr.com/");
            }
        });
        runnerAndBgLicense.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });

        //enemy texture
        Label enemyLabel = new Label("Enemy Texture by ", arcadeSkin, "screen");
        Label enemyCredit = new Label("Master484", arcadeSkin, "screen");
        Label enemyLicense1 = new Label("CC-BY 3.0", arcadeSkin, "screen");
        Label enemyLicense2 = new Label("OGA-BY 3.0", arcadeSkin, "screen");
        enemyCredit.setColor(Color.BLUE);
        enemyLicense1.setColor(Color.BLUE);
        enemyLicense2.setColor(Color.BLUE);

        enemyCredit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://m484games.ucoz.com/");
            }
        });
        enemyLicense1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });
        enemyLicense2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://static.opengameart.org/OGA-BY-3.0.txt");
            }
        });

        //fruit power up texture
        Label powerUpLabel = new Label("Fruits Texture by ", arcadeSkin, "screen");
        Label powerUpCredit = new Label("Master484", arcadeSkin, "screen");
        Label powerUpLicense = new Label("CC0", arcadeSkin, "screen");
        powerUpCredit.setColor(Color.BLUE);
        powerUpLicense.setColor(Color.BLUE);

        powerUpCredit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://m484games.ucoz.com/");
            }
        });
        powerUpLicense.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/publicdomain/zero/1.0/");
            }
        });

        //icons
        Label iconsLabel = new Label("UI Icons by ", arcadeSkin, "screen");
        Label iconsCredit = new Label("Kenney", arcadeSkin, "screen");
        Label iconsLicense = new Label("CC0", arcadeSkin, "screen");
        iconsCredit.setColor(Color.BLUE);
        iconsLicense.setColor(Color.BLUE);

        iconsCredit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://kenney.nl/");
            }
        });
        iconsLicense.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/publicdomain/zero/1.0/");
            }
        });

        //skins
        Label skinLabel1 = new Label("Clean Crispy Skin by ", arcadeSkin, "screen");
        Label skinCredit1 = new Label("Raymond Raeleus Buckley", arcadeSkin, "screen");
        Label skinLicense1 = new Label("CC-BY 4.0", arcadeSkin, "screen");
        skinCredit1.setColor(Color.BLUE);
        skinLicense1.setColor(Color.BLUE);

        skinCredit1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://ray3k.wordpress.com/software/skin-composer-for-libgdx/");
            }
        });
        skinLicense1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/4.0/");
            }
        });

        Label skinLabel2 = new Label("Arcade Skin by ", arcadeSkin, "screen");
        Label skinCredit2 = new Label("Raymond Raeleus Buckley", arcadeSkin, "screen");
        Label skinLicense2 = new Label("CC-BY 4.0", arcadeSkin, "screen");
        Label skinFontLicense2 = new Label("See font license", arcadeSkin, "screen");
        skinCredit2.setColor(Color.BLUE);
        skinLicense2.setColor(Color.BLUE);
        skinFontLicense2.setColor(Color.BLUE);

        skinCredit2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://ray3k.wordpress.com/software/skin-composer-for-libgdx/");
            }
        });
        skinLicense2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/4.0/");
            }
        });
        skinFontLicense2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/czyzby/gdx-skins/blob/master/arcade/RussoOne.txt");
            }
        });

        //tester
        Label testerLabel = new Label("Tester ", arcadeSkin, "screen");
        Label testerCredit = new Label("Donna Marie", arcadeSkin, "screen");
        testerCredit.setColor(Color.BLUE);

        goBackButton = new TextButton("Main Menu", cleanCrispySkin, "default");
        goBackButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

//        stage.setDebugAll(true);

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);

        table.add(runnerAndBgLabel).pad(5);
        table.row();
        table.add(runnerAndBgCredit).pad(5);
        table.row();
        table.add(runnerAndBgLicense).pad(5);
        table.row();
        table.center().center().pad(5);
        table.setBackground(new NinePatchDrawable(patch));

        table2.add(enemyLabel).pad(5);
        table2.row();
        table2.add(enemyCredit).pad(5);
        table2.row();
        table2.add(enemyLicense1).pad(5);
        table2.row();
        table2.add(enemyLicense2).pad(5);
        table2.row();
        table2.center().center().pad(5);
        table2.setBackground(new NinePatchDrawable(patch));

        table3.add(iconsLabel).pad(5);
        table3.row();
        table3.add(iconsCredit).pad(5);
        table3.row();
        table3.add(iconsLicense).pad(5);
        table3.row();
        table3.center().center().pad(5);
        table3.setBackground(new NinePatchDrawable(patch));

        table4.add(powerUpLabel).pad(5);
        table4.row();
        table4.add(powerUpCredit).pad(5);
        table4.row();
        table4.add(powerUpLicense).pad(5);
        table4.row();
        table4.center().center().pad(5);
        table4.setBackground(new NinePatchDrawable(patch));

        table5.add(skinLabel1).pad(5);
        table5.row();
        table5.add(skinCredit1).pad(5);
        table5.row();
        table5.add(skinLicense1).pad(5);
        table5.row();
        table5.center().center().pad(5);
        table5.setBackground(new NinePatchDrawable(patch));

        table6.add(skinLabel2).pad(5);
        table6.row();
        table6.add(skinCredit2).pad(5);
        table6.row();
        table6.add(skinLicense2).pad(5);
        table6.row();
        table6.add(skinFontLicense2).pad(5);
        table6.row();
        table6.center().center().pad(5);
        table6.setBackground(new NinePatchDrawable(patch));

        table7.add(testerLabel).pad(5);
        table7.row();
        table7.add(testerCredit).pad(5);
        table7.row();
        table7.center().center().pad(5);
        table7.setBackground(new NinePatchDrawable(patch));

        table8.add(goBackButton).pad(5).width(150).height(50).expandX().pad(15);
        table8.row();
        table8.setBackground(new NinePatchDrawable(patch));

        rootTable.add(table).fillX();
        rootTable.row();
        rootTable.add(table2).fillX();
        rootTable.row();
        rootTable.add(table3).fillX();
        rootTable.row();
        rootTable.add(table4).fillX();
        rootTable.row();
        rootTable.add(table5).fillX();
        rootTable.row();
        rootTable.add(table6).fillX();
        rootTable.row();
        rootTable.add(table7).fillX();
        rootTable.row();
        rootTable.add(table8).fillX();
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2 + 50);
        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 112, TapRunner.WIDTH - 200, TapRunner.HEIGHT - 459);
        game.batch.draw(ground, 0, 0);
        game.batch.end();
        stage.act();
        stage.draw();
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
