package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Sami on 30.12.2015.
 */
public class InputHandler implements InputProcessor {
    private GameField targetGameField;
    private int draggingButton = -1;

    public InputHandler(GameField gameField) {
        this.targetGameField = gameField;
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
        this.draggingButton = button;

        if (button == 0) { // Left click
            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                this.targetGameField.toObstacle(screenX, screenY, EditorHud.selectedObject);
            } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                this.targetGameField.toBuildableLocation(screenX, screenY);
            } else {
                this.targetGameField.toRoad(screenX, screenY);
            }
            return true;
        } else if (button == 1) { // Right click
            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                this.targetGameField.toTower(screenX, screenY, EditorHud.selectedTower);
            } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                this.targetGameField.toNotBuildableLocation(screenX, screenY);
            } else {
                this.targetGameField.toWall(screenX, screenY);
            }
            return true;
        } else if (button == 2){
            this.targetGameField.clearPaths();
            return true;
        } else if (button == 3) {
            this.targetGameField.beginPath(screenX, screenY);
            return true;
        } else if (button == 4) {
            this.targetGameField.findPath(screenX, screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.draggingButton = -1;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (this.draggingButton == 0) { // Left click
            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                this.targetGameField.toObstacle(screenX, screenY, EditorHud.selectedObject);
            } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                this.targetGameField.toBuildableLocation(screenX, screenY);
            } else {
                this.targetGameField.toRoad(screenX, screenY);
            }
            return true;
        } else if (this.draggingButton == 1) { // Right click
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                this.targetGameField.toNotBuildableLocation(screenX, screenY);
            } else {
                this.targetGameField.toWall(screenX, screenY);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        System.out.println("SCROLLED AMOUNT: " + amount);
        return false;
    }

    public void setLab(GameField lab) {
        this.targetGameField = lab;
    }
}
