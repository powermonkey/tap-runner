package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Rod on 4/14/2017.
 */

public class GameScreen implements Screen{
    final TapRunner game;
    Texture bg;
    OrthographicCamera cam;
    Runner runner;
    Array<Ground> grounds;
    Ground grnd;

    Stage stage;
    TextButton button1, button2;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;


    public GameScreen(final TapRunner gam){
        this.game = gam;
        bg = new Texture("bg.png");
        runner = new Runner(50, 112);
        cam = new OrthographicCamera();
        grnd = new Ground(cam.position.x - (cam.viewportWidth / 2));
        grounds = new Array<Ground>();
        cam.setToOrtho(false, TapRunner.WIDTH / 2, TapRunner.HEIGHT / 2);

        grounds.add(new Ground(0));
        grounds.add(new Ground(grnd.getTexture().getWidth()));

        Skin mySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        Table table = new Table();
        table.setFillParent(true);
//        table.setDebug(true);


        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        TextButton button1 = new TextButton("Run",mySkin,"arcade");
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Press a Button");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                runner.run();
                return true;
            }
        });

        TextButton button2 = new TextButton("Jump",mySkin,"arcade");
        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Press a Button");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(runner.isOnGround)
                    runner.jump();
                return true;
            }
        });

        table.add(button1).padLeft(20).width(100).height(100).expandX();
        table.add(button2).padRight(20).width(100).height(100).expandX();
        table.row();
        table.center().bottom();
        table.padBottom(50);
        stage.addActor(table);
    }

    public void handleInput() {

    }

    @Override
    public void render(float delta) {
        //logic first
        cam.position.x = runner.getPosition().x + 80;
        for(Ground ground: grounds){
            if(cam.position.x - (cam.viewportWidth / 2) > ground.getPosGround().x + ground.getTexture().getWidth() )
                ground.reposition(ground.getPosGround().x + (ground.getTexture().getWidth() * 2));
        }

        cam.update();
        handleInput();
        runner.update(delta);

        //then render
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        for(Ground ground: grounds){
            game.batch.draw(ground.getTexture(), ground.getPosGround().x, ground.getPosGround().y);
        }

        game.batch.draw(runner.getTexture(), runner.getPosition().x, runner.getPosition().y);
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
        runner.dispose();
        grnd.dispose();
    }
}
