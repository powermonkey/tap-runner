package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 4/18/2017.
 */

public class Hud {
    Stage stage;
    Table table;
    Skin mySkin;
    Meter meter;
    RunButton runButton;
    JumpButton jumpButton;


    public Hud(final Runner runner){
        table = new Table();
        table.setFillParent(true);
        mySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));
        stage = new Stage();
        meter = new Meter();
        runButton = new RunButton(runner);
        jumpButton = new JumpButton(runner);

        Gdx.input.setInputProcessor(stage);

        table.add(meter.getSpeedMeter()).size(100,100);
        table.row();
        table.add(runButton.getRunButton()).padLeft(20).width(100).height(100).expandX();
        table.add(jumpButton.getJumpButton()).padRight(20).width(100).height(100).expandX();
        table.row();
        table.center().bottom();
        table.padBottom(50);
        stage.addActor(table);
    }

    public class Meter{
        ProgressBar speedMeter;

        public Meter(){
            speedMeter = new ProgressBar(1, 15, 1, false, getMySkin(), "default-horizontal");
            speedMeter.setAnimateDuration(.5f);
        }

        public ProgressBar getSpeedMeter() {
            return speedMeter;
        }

        public void update(float speed){
            if(speed < 100)
                speedMeter.setValue(1);
            else if(speed < 200)
                speedMeter.setValue(2);
            else if(speed < 300)
                speedMeter.setValue(3);
            else if(speed < 400)
                speedMeter.setValue(4);
            else if(speed < 500)
                speedMeter.setValue(5);
            else if(speed < 600)
                speedMeter.setValue(6);
            else if(speed < 700)
                speedMeter.setValue(7);
            else if(speed < 800)
                speedMeter.setValue(8);
            else if(speed < 900)
                speedMeter.setValue(9);
            else if(speed < 1000)
                speedMeter.setValue(10);
            else if(speed < 1200)
                speedMeter.setValue(11);
            else if(speed < 1400)
                speedMeter.setValue(12);
            else if(speed < 1600)
                speedMeter.setValue(13);
            else if(speed < 1800)
                speedMeter.setValue(14);
            else if(speed < 2000)
                speedMeter.setValue(15);
        }
    }

    public class RunButton{
        TextButton button;

        public RunButton(Runner runner){
            button = new TextButton("Run",mySkin,"arcade");
            button1Listener(button, runner);
        }

        public void button1Listener(TextButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(runner.isOnGround)
                        runner.run();
                    return true;
                }
            });
        }

        public TextButton getRunButton(){
            return button;
        }

    }

    public class JumpButton{
        TextButton button;

        public JumpButton(Runner runner){
            button = new TextButton("Jump",mySkin,"arcade");
            buttonListener(button, runner);
        }

        public void buttonListener(TextButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(runner.isOnGround)
                        runner.jump();
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

    public Skin getMySkin(){
        return mySkin;
    }

    public void render(){
        stage.act();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
    }

}
