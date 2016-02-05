package fi.joutsijoki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fi.joutsijoki.Constant;
import fi.joutsijoki.FileHandler;
import fi.joutsijoki.EditorHud;
import fi.joutsijoki.InputHandler;
import fi.joutsijoki.GameField;
import fi.joutsijoki.Main;
import fi.joutsijoki.Simulation;

/**
 * Created by Sami on 3.2.2016.
 */
public class Editor implements Screen {
    private Main parent;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameField gameField;
    private InputHandler inputHandler;
    private EditorHud hud;
    private FileHandler fileHandler;
    private Simulation simulation;
    private boolean runningSimulation;

    // TODO mobile port
    public Editor(Main parent) {
        this.parent = parent;
    }

    private void init() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameField = new GameField();

        hud = new EditorHud(this, new Stage());
        hud.addButtons();

        inputHandler = new InputHandler(gameField);

        fileHandler = new FileHandler();

        InputMultiplexer impx = new InputMultiplexer();
        impx.addProcessor(hud.getHolder());
        impx.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(impx);
    }

    @Override
    public void show() {
        Constant.setDimensions(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight());
        init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        gameField.draw(batch, runningSimulation);

        if (runningSimulation) {
            simulation.draw(batch);
        }

        batch.end();

        gameField.drawOutlines(camera.combined);
        //gameField.drawTowerRadius(camera.combined);
        gameField.drawBuildableLocations(camera.combined);

        hud.drawHolder();
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

    }

    public void exportMap(String fileName) {
        fileHandler.exportMap(gameField, fileName);
    }

    public void loadMap(String fileName) {
        GameField lab = fileHandler.loadMap(fileName);

        if (lab != null) {
            this.gameField = lab;
            this.inputHandler.setLab(this.gameField);
        } else {
            System.out.println("Loading failed");
        }
    }

    public void runSimulations() {
        simulation = new Simulation();
        simulation.setLabyrinth(this.gameField);
        simulation.fetchTowers();
        simulation.start();
        this.runningSimulation = true;
    }
}
