package fi.joutsijoki.game;

import com.badlogic.gdx.InputProcessor;

import fi.joutsijoki.GameField;
import fi.joutsijoki.screens.GameScreen;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 5.2.2016.
 */
public class GameInputHandler implements InputProcessor {
    private GameScreen parentScreen;
    private GameField target;
    private GameHud hud;

    public GameInputHandler(GameScreen parentScreen, GameField target, GameHud hud) {
        this.parentScreen = parentScreen;
        this.target = target;
        this.hud = hud;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        target.toSelectedLocation(screenX, screenY);

        return true;
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
