package com.rcam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    RunButton runButton;
    SlowDownButton slowDownButton;
//    Joystick joystick;
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
        runButton = new RunButton(runner);
        slowDownButton = new SlowDownButton(runner);
//        joystick = new Joystick(runner);
        jumpButton = new JumpButton(runner);
        healthLabel = new Label("HEALTH", cleanCrispySkin);
        speedMeterLabel = new Label("SPEED", cleanCrispySkin);

//        stage.setDebugAll(true);

        patch = new NinePatch(new Texture(Gdx.files.internal("Block_Type2_Yellow.png")), 4, 4, 4, 4);

        Gdx.input.setInputProcessor(stage);

        distancetable.add(distance.getIndicator()).padBottom(120).colspan(2).expand().center().center();
        distancetable.row();

        indicatorstable.add(healthLabel).width(80).padLeft(40);
        indicatorstable.add(health.getHealthBar()).padRight(20);
        indicatorstable.row();
        indicatorstable.add(speedMeterLabel).width(80).padLeft(40);
        indicatorstable.add(meter.getSpeedMeter()).padBottom(10).padRight(20);
        indicatorstable.row();
//        indicatorstable.setBackground(new NinePatchDrawable(patch));

//        controlsTable.add(arrowLeft).padRight(-30).padTop(10);
//        controlsTable.add(arrowRight).padLeft(90).padTop(10);
//        controlsTable.add(joystick.getJoystick()).padLeft(-141).left().height(120).padTop(10);

        controlsTable.add(slowDownButton.getSlowDownButton()).padLeft(20).padRight(20).left();
        controlsTable.add(runButton.getRunButton()).left();
        controlsTable.add(jumpButton.getJumpButton()).right().padRight(20).expandX();
        controlsTable.row();
        controlsTable.setBackground(new NinePatchDrawable(patch));

        rootTable.add(distancetable).width(TapRunner.WIDTH).center().expand();
        rootTable.row();
        rootTable.add(indicatorstable).width(TapRunner.WIDTH).bottom().expandX();
        rootTable.row();
        rootTable.add(controlsTable).width(TapRunner.WIDTH).bottom().height(130).expandX();
        rootTable.row();
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

//    public class Joystick{
//        Touchpad joystick;
//        Touchpad.TouchpadStyle joystickStyle;
//
//        public Joystick(Runner runner){
//            joystickGray = getArcadeSkin().getDrawable("joystick-gray");
//            joystickGray.setMinWidth(132);
//            joystickGray.setMinHeight(138);
//            joystickLeft = getArcadeSkin().getDrawable("joystick-l-gray");
//            joystickLeft.setMinWidth(132);
//            joystickLeft.setMinHeight(138);
//            joystickRight = getArcadeSkin().getDrawable("joystick-r-gray");
//            joystickRight.setMinWidth(132);
//            joystickRight.setMinHeight(138);
//            joystickStyle = new Touchpad.TouchpadStyle();
//
//            joystick = new Touchpad(50, getArcadeSkin());
//            joystick.setBounds(0, 0, 132, 138);
//            System.out.println(joystick.getPrefWidth());
//            System.out.println(joystick.getPrefHeight());
//            joystick.setResetOnTouchUp(true);
//            joystickStyle.background = joystickGray;
//            joystick.setStyle(joystickStyle);
//            joystickListener(joystick, runner);
//        }
//
//        public void joystickListener(final Touchpad joystick, final Runner runner){
//            joystick.addListener(new ClickListener(){
//                @Override
//                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
////                    if(runner.isOnGround) {
//                        if(joystick.getKnobPercentX() > 0){
//                            joystickStyle.background = joystickRight;
//                            joystick.setStyle(joystickStyle);
//                            runner.run();
//                        }else if((joystick.getKnobPercentX() < 0)){
//                            joystickStyle.background = joystickLeft;
//                            joystick.setStyle(joystickStyle);
//                            runner.slowDown();
//                        }
////                    }
//                    return true;
//                }
//
//                @Override
//                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//                    joystickStyle.background = joystickGray;
//                    joystick.setStyle(joystickStyle);
//                }
//            });
//        }
//
//        public Touchpad getJoystick(){
//            return joystick;
//        }
//
//    }

    public class SlowDownButton{
        ImageButton sbutton;
        ImageButton.ImageButtonStyle sbuttonStyle;

        public SlowDownButton(Runner runner){
            sbutton = new ImageButton(getCleanCrispySkin(), "default");
            sbuttonStyle = new ImageButton.ImageButtonStyle();
            sbuttonStyle.up = getCleanCrispySkin().getDrawable("button-c");
            sbuttonStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            sbuttonStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
            sbuttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("arrowLeft.png")));
            sbuttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("arrowLeft.png")));
            sbutton.setStyle(sbuttonStyle);
            button1Listener(sbutton, runner);
        }

        public void button1Listener(final ImageButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    runner.slowDown();
                    return true;
                }
            });
        }

        public ImageButton getSlowDownButton(){
            return sbutton;
        }

    }

    public class RunButton{
        ImageButton rbutton;
        ImageButton.ImageButtonStyle rbuttonStyle;

        public RunButton(Runner runner){
            rbutton = new ImageButton(getCleanCrispySkin(), "default");
            rbuttonStyle = new ImageButton.ImageButtonStyle();
            rbuttonStyle.up = getCleanCrispySkin().getDrawable("button-c");
            rbuttonStyle.down = getCleanCrispySkin().getDrawable("button-pressed-over-c");
            rbuttonStyle.over = getCleanCrispySkin().getDrawable("button-over-c");
            rbuttonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("arrowRight.png")));
            rbuttonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(new Texture("arrowRight.png")));
            rbutton.setStyle(rbuttonStyle);

            button1Listener(rbutton, runner);
        }

        public void button1Listener(final ImageButton button, final Runner runner){
            button.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    runner.run();
                    return true;
                }
            });
        }

        public ImageButton getRunButton(){
            return rbutton;
        }

    }

    public class JumpButton{
        TextButton button;

        public JumpButton(Runner runner){
            button = new TextButton("JUMP", getCleanCrispySkin(), "arcade");
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
