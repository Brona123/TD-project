package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Sami on 30.12.2015.
 */
public class InputHandler implements InputProcessor {
    private Labyrinth targetLabyrinth;
    private int draggingButton = -1;

    public InputHandler(Labyrinth labyrinth) {
        this.targetLabyrinth = labyrinth;
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
                //this.targetLabyrinth.toObstacle(screenX, screenY, Hud.selectedObject);
                this.targetLabyrinth.toBuildableLocation(screenX, screenY);
            } else {
                this.targetLabyrinth.toRoad(screenX, screenY);
            }
            return true;
        } else if (button == 1) { // Right click
            if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                this.targetLabyrinth.toTower(screenX, screenY, Hud.selectedTower);
            } else {
                this.targetLabyrinth.toWall(screenX, screenY);
            }
            return true;
        } else if (button == 2){
            this.targetLabyrinth.clearPaths();
            return true;
        } else if (button == 3) {
            this.targetLabyrinth.beginPath(screenX, screenY);
            return true;
        } else if (button == 4) {
            this.targetLabyrinth.findPath(screenX, screenY);
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
                //this.targetLabyrinth.toObstacle(screenX, screenY, Hud.selectedObject);
                this.targetLabyrinth.toBuildableLocation(screenX, screenY);
            } else {
                this.targetLabyrinth.toRoad(screenX, screenY);
            }
            return true;
        } else if (this.draggingButton == 1) { // Right click
            this.targetLabyrinth.toWall(screenX, screenY);
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
        return false;
    }

    public void setLab(Labyrinth lab) {
        this.targetLabyrinth = lab;
    }
}
