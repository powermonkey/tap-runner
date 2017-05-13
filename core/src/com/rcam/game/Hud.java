package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 4/18/2017.
 */

public class Hud {
    Stage stage;
    Table table;
    Skin cleanCrispySkin, arcadeSkin;
    Meter meter;
    RunButton runButton;
    JumpButton jumpButton;
    Distance distance;
    Health health;
    Label healthLabel, speedMeterLabel, distanceLabel;

    public Hud(final Runner runner){
        table = new Table();
        table.setFillParent(true);
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        arcadeSkin = new Skin(Gdx.files.internal("skin/arcade-ui/arcade-ui.json"));
        stage = new Stage(new FitViewport(480, 800));
        meter = new Meter();
        distance = new Distance(runner);
        health = new Health(runner);
        runButton = new RunButton(runner);
        jumpButton = new JumpButton(runner);
        healthLabel = new Label("HEALTH", cleanCrispySkin);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);
//        stage.setDebugAll(true);

        Gdx.input.setInputProcessor(stage);

        table.add(distance.getIndicator()).padBottom(120).colspan(2).expand().center().center();
        table.row();
        table.add(healthLabel).width(80).padLeft(40);
        table.add(health.getHealthBar()).padRight(20);
        table.row();
        table.add(speedMeterLabel).width(80).padLeft(40);
        table.add(meter.getSpeedMeter()).padRight(20);
        table.row();
        table.add(runButton.getRunButton()).padLeft(20).width(100).height(100).expandX();
        table.add(jumpButton.getJumpButton()).padRight(20).width(100).height(100).expandX();
        table.row();
        table.center().bottom();
        table.padBottom(20);
        stage.addActor(table);
    }

    public class Meter{
        ProgressBar speedMeter;

        public Meter(){
            speedMeter = new ProgressBar(1, 15, 1, false, getCleanCrispySkin(), "default-horizontal");
            speedMeter.setAnimateDuration(.5f);
        }

        public ProgressBar getSpeedMeter() {
            return speedMeter;
        }

        public void update(float speed){
            if(speed < 25)
                speedMeter.setValue(1);
            else if(speed < 50)
                speedMeter.setValue(2);
            else if(speed < 75)
                speedMeter.setValue(3);
            else if(speed < 100)
                speedMeter.setValue(4);
            else if(speed < 125)
                speedMeter.setValue(5);
            else if(speed < 150)
                speedMeter.setValue(6);
            else if(speed < 175)
                speedMeter.setValue(7);
            else if(speed < 200)
                speedMeter.setValue(8);
            else if(speed < 257)
                speedMeter.setValue(9);
            else if(speed < 314)
                speedMeter.setValue(10);
            else if(speed < 371)
                speedMeter.setValue(11);
            else if(speed < 428)
                speedMeter.setValue(12);
            else if(speed < 485)
                speedMeter.setValue(13);
            else if(speed < 542)
                speedMeter.setValue(14);
            else if(speed < 600)
                speedMeter.setValue(15);
        }
    }

    public class Health{
        ProgressBar healthBar;
        Runner runr;

        public Health(final Runner runner){
            healthBar = new ProgressBar(1, 50, 1, false, getCleanCrispySkin(), "default-horizontal");
            healthBar.setAnimateDuration(.5f);
            this.runr = runner;
        }

        public ProgressBar getHealthBar() {
            return healthBar;
        }

        public void update(){
            healthBar.setValue(runr.getHealth());
        }
    }

    public class Distance{
        Label indicator;
        Runner runr;

        public Distance(final Runner runner){
            this.runr = runner;
            indicator = new Label(Integer.toString(runner.indicatePosition()) + " m", getArcadeSkin(), "default");
//            indicator.setFontScale(1.5f);
        }

        public Label getIndicator(){
            return indicator;
        }

        public void update(){
            indicator.setText(Integer.toString(runr.indicatePosition()) + " m");
        }
    }

    public class RunButton{
        TextButton button;

        public RunButton(Runner runner){
            button = new TextButton("RUN", getCleanCrispySkin(), "arcade");
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
            button = new TextButton("JUMP", getCleanCrispySkin(), "arcade");
            buttonListener(button, runner);
        }

        public void buttonListener(TextButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(runner.isOnGround) {
                        runner.jump();
                        runner.tempGround = runner.groundLevel;
                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    runner.isJumping = false;
                    runner.tempGround = runner.groundLevel;
                }
            });
        }

        public TextButton getJumpButton(){
            return button;
        }

    }

    public Skin getArcadeSkin(){
        return arcadeSkin;
    }

    public Skin getCleanCrispySkin(){
        return cleanCrispySkin;
    }

    public void render(){
        stage.act();
        stage.draw();
    }

    public void dispose(){
        cleanCrispySkin.dispose();
        arcadeSkin.dispose();
        stage.dispose();
    }

}
