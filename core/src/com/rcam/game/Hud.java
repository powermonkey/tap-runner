package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 4/18/2017.
 */

public class Hud {
    Stage stage;
    Table rootTable, indicatorstable, distancetable, controlsTable;
    Skin cleanCrispySkin, arcadeSkin;
    Meter meter;
//    RunButton runButton;
    Joystick joystick;
    JumpButton jumpButton;
    Distance distance;
    Health health;
    Label healthLabel, speedMeterLabel;
    Drawable joystickGray, joystickLeft, joystickRight;
    NinePatch patch;

    public Hud(final Runner runner){
        rootTable = new Table();
        indicatorstable = new Table();
        distancetable = new Table();
        controlsTable = new Table();
        rootTable.setFillParent(true);
        cleanCrispySkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui/clean-crispy-ui.json"));
        arcadeSkin = new Skin(Gdx.files.internal("skin/arcade-ui/arcade-ui.json"));
        stage = new Stage(new FitViewport(480, 800));
        meter = new Meter();
        distance = new Distance(runner);
        health = new Health(runner);
//        runButton = new RunButton(runner);
        joystick = new Joystick(runner);
        jumpButton = new JumpButton(runner);
        healthLabel = new Label("HEALTH", cleanCrispySkin);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);

//        stage.setDebugAll(true);

        patch = new NinePatch(new Texture(Gdx.files.internal("Block_Type2_Yellow.png")), 4, 4, 4, 4);

//        Gdx.input.setInputProcessor(stage);

        distancetable.add(distance.getIndicator()).padBottom(120).colspan(2).expand().center().center();
        distancetable.row();

        indicatorstable.add(healthLabel).width(80).padLeft(40);
        indicatorstable.add(health.getHealthBar()).padRight(20);
        indicatorstable.row();
        indicatorstable.add(speedMeterLabel).width(80).padLeft(40);
        indicatorstable.add(meter.getSpeedMeter()).padBottom(10).padRight(20);
        indicatorstable.row();
//        indicatorstable.setBackground(new NinePatchDrawable(patch));

//        table.add(runButton.getRunButton()).padLeft(20).expandX();
        controlsTable.add(joystick.getJoystick()).left().height(120).padTop(10).padLeft(30).expandX();
        controlsTable.add(jumpButton.getJumpButton()).left().padLeft(10).expandX();
        controlsTable.row();
//        controlsTable.setBackground(new NinePatchDrawable(patch));

        rootTable.add(distancetable).width(TapRunner.WIDTH).center().expand();
        rootTable.row();
        rootTable.add(indicatorstable).width(TapRunner.WIDTH - 120).bottom().expandX();
        rootTable.row();
        rootTable.add(controlsTable).width(TapRunner.WIDTH - 120).bottom().expandX();
        rootTable.row();
//        rootTable.center().center();
        stage.addActor(rootTable);
    }

    public class Meter{
        ProgressBar speedMeter;

        public Meter(){
            speedMeter = new ProgressBar(1, 4, 1, false, getCleanCrispySkin(), "default-horizontal");
            speedMeter.setAnimateDuration(.5f);
        }

        public ProgressBar getSpeedMeter() {
            return speedMeter;
        }

        public void update(float speed){
            if(speed < 50)
                speedMeter.setValue(1);
            else if(speed < 100)
                speedMeter.setValue(2);
            else if(speed < 150)
                speedMeter.setValue(3);
            else if(speed == 150)
                speedMeter.setValue(4);
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
        }

        public Label getIndicator(){
            return indicator;
        }

        public void update(){
            indicator.setText(Integer.toString(runr.indicatePosition()) + " m");
        }
    }

    public class Joystick{
        Touchpad joystick;
        Touchpad.TouchpadStyle joystickStyle;

        public Joystick(Runner runner){
            joystickGray = getArcadeSkin().getDrawable("joystick-gray");
            joystickLeft = getArcadeSkin().getDrawable("joystick-l-gray");
            joystickRight = getArcadeSkin().getDrawable("joystick-r-gray");

            joystickStyle = new Touchpad.TouchpadStyle();

            joystick = new Touchpad(10, getArcadeSkin());
//            joystick.setBounds(15, 15, 200, 200);
            joystick.setResetOnTouchUp(true);
            joystickStyle.background = joystickGray;
            joystick.setStyle(joystickStyle);
            joystickListener(joystick, runner);
        }

        public void joystickListener(final Touchpad joystick, final Runner runner){
            joystick.addListener(new ClickListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                    if(runner.isOnGround) {
                        if(joystick.getKnobPercentX() > 0){
                            joystickStyle.background = joystickRight;
                            joystick.setStyle(joystickStyle);
                            runner.run();
                        }else if((joystick.getKnobPercentX() < 0)){
                            joystickStyle.background = joystickLeft;
                            joystick.setStyle(joystickStyle);
                            runner.slowDown();
                        }
//                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    joystickStyle.background = joystickGray;
                    joystick.setStyle(joystickStyle);
                }
            });
        }

        public Touchpad getJoystick(){
            return joystick;
        }

    }

//    public class RunButton{
//        Button button;
//
//        public RunButton(Runner runner){
//            button = new Button(getArcadeSkin(), "yellow");
//            button1Listener(button, runner);
//        }
//
//        public void button1Listener(final Button button, final Runner runner){
//            button.addListener(new InputListener(){
//                @Override
//                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                    if(runner.isOnGround)
//                        runner.run();
//                    return true;
//                }
//            });
//        }
//
//        public Button getRunButton(){
//            return button;
//        }
//
//    }

    public class JumpButton{
        Button button;

        public JumpButton(Runner runner){
            button = new Button(getArcadeSkin(), "yellow");
            buttonListener(button, runner);
        }

        public void buttonListener(final Button button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    if(runner.isOnGround) {
                        runner.jump();
                    }
                    return true;
                }

                @Override
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    runner.isJumping = false;
                }
            });
        }

        public Button getJumpButton(){
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
