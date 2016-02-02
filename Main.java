package fi.joutsijoki;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Labyrinth labyrinth;
	private InputHandler inputHandler;
	private Hud hud;
	private FileHandler fileHandler;
	private Simulation simulation;
	private boolean runningSimulation;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		labyrinth = new Labyrinth();

		hud = new Hud(this, new Stage());
		hud.addButtons();

		inputHandler = new InputHandler(labyrinth);

		fileHandler = new FileHandler();

		InputMultiplexer impx = new InputMultiplexer();
		impx.addProcessor(hud.getHolder());
		impx.addProcessor(inputHandler);
		Gdx.input.setInputProcessor(impx);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		int srcFunc = batch.getBlendSrcFunc();
		int dstFunc = batch.getBlendDstFunc();
		labyrinth.draw(batch, runningSimulation);
		batch.end();

		batch.begin();
		if (runningSimulation) {
			simulation.draw(batch);
		}
		batch.setBlendFunction(srcFunc, dstFunc);
		batch.end();


		labyrinth.drawOutlines(camera.combined);
		//labyrinth.drawTowerRadius(camera.combined);
		labyrinth.drawBuildableLocations(camera.combined);

		hud.drawHolder();
	}

	public void exportMap(String fileName) {
		fileHandler.exportMap(labyrinth, fileName);
	}

	public void loadMap(String fileName) {
		Labyrinth lab = fileHandler.loadMap(fileName);

		if (lab != null) {
			this.labyrinth = lab;
			this.inputHandler.setLab(this.labyrinth);
		} else {
			System.out.println("Loading failed");
		}
	}

	public void runSimulations() {
		simulation = new Simulation();
		simulation.setLabyrinth(this.labyrinth);
		simulation.fetchTowers();
		simulation.start();
		this.runningSimulation = true;
	}
}
