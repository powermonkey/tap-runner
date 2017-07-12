package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Rod on 7/3/2017.
 */

public class CreditsScreen implements Screen {
    final TapRunner game;
    OrthographicCamera cam;
    Table rootTable, table, table2, table3, table4, table5, table6, table7, table8, table9;
    Skin arcadeSkin, cleanCrispySkin;
    Stage stage;
    TextureAtlas.AtlasRegion ground, bg, blockYellow, blockYellowGreen;
    TextButton goBackButton;
    Sound blipSelectSound;
    static Preferences prefs;
    BitmapFont buttonFonts;

    public CreditsScreen(final TapRunner gam){
        game = gam;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, TapRunner.WIDTH * 0.5f, TapRunner.HEIGHT * 0.5f + 50);
        bg = GameAssetLoader.bg;
        ground = GameAssetLoader.ground;
        arcadeSkin = GameAssetLoader.arcadeSkin;
        cleanCrispySkin = GameAssetLoader.cleanCrispySkin;
        blockYellow = GameAssetLoader.blockYellow;
        blipSelectSound = GameAssetLoader.blipSelect;

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
        table9 = new Table();

        prefs = Gdx.app.getPreferences("TapRunner");

        //runner & background texture
        Label runnerAndBgLabel = new Label("Runner & Background Textures by ", arcadeSkin, "screen");
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
        Label enemyLabel = new Label("Enemy & Fruits Textures by ", arcadeSkin, "screen");
        Label enemyCredit = new Label("Master484", arcadeSkin, "screen");
        Label enemyLicense1 = new Label("CC-BY 3.0", arcadeSkin, "screen");
        Label enemyLicense2 = new Label("OGA-BY 3.0", arcadeSkin, "screen");
        Label enemyLicense3 = new Label("CC0", arcadeSkin, "screen");
        enemyCredit.setColor(Color.BLUE);
        enemyLicense1.setColor(Color.BLUE);
        enemyLicense2.setColor(Color.BLUE);
        enemyLicense3.setColor(Color.BLUE);

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
        enemyLicense3.addListener(new ClickListener(){
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
        Label skinLabel = new Label("Clean Crispy Skin and ", arcadeSkin, "screen");
        Label skinLabel2 = new Label("Arcade Skin by ", arcadeSkin, "screen");
        Label skinCredit = new Label("Raymond Raeleus Buckley", arcadeSkin, "screen");
        Label skinLicense = new Label("CC-BY 4.0", arcadeSkin, "screen");
        skinCredit.setColor(Color.BLUE);
        skinLicense.setColor(Color.BLUE);

        skinCredit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://ray3k.wordpress.com/software/skin-composer-for-libgdx/");
            }
        });
        skinLicense.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/4.0/");
            }
        });

        //sound effects
        Label soundLabel = new Label("Sound Effects by ", arcadeSkin, "screen");
        Label soundCredit1 = new Label("KIIRA", arcadeSkin, "screen");
        Label soundCredit2 = new Label("Jesus Lastra", arcadeSkin, "screen");
        Label soundCredit3 = new Label("dklon", arcadeSkin, "screen");
        Label soundCredit4 = new Label("8-bit Platformer SFX commissioned by Mark McCorkle", arcadeSkin, "screen");
        Label soundLicense = new Label("CC-BY 3.0", arcadeSkin, "screen");
        soundCredit1.setColor(Color.BLUE);
        soundCredit2.setColor(Color.BLUE);
        soundCredit3.setColor(Color.BLUE);
        soundCredit4.setColor(Color.BLUE);
        soundLicense.setColor(Color.BLUE);

        soundCredit1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/byte-man-sfx-1");
            }
        });
        soundCredit2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/8-bit-powerup-1");
            }
        });
        soundCredit3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/platformer-jumping-sounds");
            }
        });
        soundCredit4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://opengameart.org/content/8-bit-platformer-sfx");
            }
        });
        soundLicense.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://creativecommons.org/licenses/by/3.0/");
            }
        });

        //fonts
        Label fontLabel = new Label("Fonts by ", arcadeSkin, "screen");
        Label fontCredit1 = new Label("Codeman38", arcadeSkin, "screen");
        Label fontCredit2 = new Label("Russo", arcadeSkin, "screen");
        fontCredit1.setColor(Color.BLUE);
        fontCredit2.setColor(Color.BLUE);

        fontCredit1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("http://www.zone38.net/");
            }
        });
        fontCredit2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://github.com/czyzby/gdx-skins/blob/master/arcade/RussoOne.txt");
            }
        });

        //tester
        Label testerLabel = new Label("Tester ", arcadeSkin, "screen");
        Label testerCredit = new Label("Donna Marie", arcadeSkin, "screen");
        testerCredit.setColor(Color.BLUE);


        blockYellowGreen = GameAssetLoader.blockYellowGreen;
        NinePatch patchGreen = new NinePatch(blockYellowGreen, 4, 4, 4, 4);
        NinePatchDrawable patchDrawableGreen = new NinePatchDrawable(patchGreen);
        buttonFonts = GameAssetLoader.buttonFonts;
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = patchDrawableGreen;
        buttonStyle.down = patchDrawableGreen;
        buttonStyle.font = buttonFonts;
        goBackButton = new TextButton("Main Menu", buttonStyle);

        goBackButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(prefs.getBoolean("SoundOn")) {
                    blipSelectSound.play();
                }
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return true;
            }
        });

//        stage.setDebugAll(true);

        NinePatch patch = new NinePatch(blockYellow, 4, 4, 4, 4);

        table.add(runnerAndBgLabel);
        table.row();
        table.add(runnerAndBgCredit);
        table.row();
        table.add(runnerAndBgLicense);
        table.row();
        table.center().center().pad(10);
        table.setBackground(new NinePatchDrawable(patch));

        table2.add(enemyLabel);
        table2.row();
        table2.add(enemyCredit);
        table2.row();
        table2.add(enemyLicense1);
        table2.row();
        table2.add(enemyLicense2);
        table2.row();
        table2.add(enemyLicense3);
        table2.row();
        table2.center().center().pad(10);
        table2.setBackground(new NinePatchDrawable(patch));

        table3.add(iconsLabel);
        table3.row();
        table3.add(iconsCredit);
        table3.row();
        table3.add(iconsLicense);
        table3.row();
        table3.center().center().pad(10);
        table3.setBackground(new NinePatchDrawable(patch));

        table5.add(skinLabel);
        table5.row();
        table5.add(skinLabel2);
        table5.row();
        table5.add(skinCredit);
        table5.row();
        table5.add(skinLicense);
        table5.row();
        table5.center().center().pad(10);
        table5.setBackground(new NinePatchDrawable(patch));

        table6.add(soundLabel);
        table6.row();
        table6.add(soundCredit1);
        table6.row();
        table6.add(soundCredit2);
        table6.row();
        table6.add(soundCredit3);
        table6.row();
        table6.add(soundCredit4);
        table6.row();
        table6.add(soundLicense);
        table6.row();
        table6.center().center().pad(10);
        table6.setBackground(new NinePatchDrawable(patch));

        table7.add(fontLabel);
        table7.row();
        table7.add(fontCredit1);
        table7.row();
        table7.add(fontCredit2);
        table7.row();
        table7.center().center().pad(10);
        table7.setBackground(new NinePatchDrawable(patch));

        table8.add(testerLabel);
        table8.row();
        table8.add(testerCredit);
        table8.row();
        table8.center().center().pad(10);
        table8.setBackground(new NinePatchDrawable(patch));

        table9.add(goBackButton).pad(5).width(200).height(50).expandX().pad(15);
        table9.row();
        table9.setBackground(new NinePatchDrawable(patch));

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
        rootTable.add(table9).fillX();
        rootTable.row();
        rootTable.center().center();

        stage.addActor(rootTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.disableBlending();
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
        stage.dispose();
        buttonFonts.dispose();
//        GameAssetLoader.dispose();
    }
}
