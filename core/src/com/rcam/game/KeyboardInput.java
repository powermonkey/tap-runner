package com.rcam.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.rcam.game.sprites.Runner;

/**
 * Created by Rod on 6/4/2017.
 */

public class KeyboardInput implements InputProcessor{
    Runner runner;

    public KeyboardInput(Runner runner){
        this.runner = runner;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            if(runner.isOnGround) {
                runner.jump();
            }
        }
        if (keycode == Input.Keys.RIGHT) {
            if (runner.isOnGround) {
                runner.run();
            }
        }
        if (keycode == Input.Keys.LEFT) {
            if (runner.isOnGround) {
                runner.slowDown();
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        runner.isJumping = false;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
