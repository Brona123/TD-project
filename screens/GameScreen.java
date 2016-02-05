package fi.joutsijoki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Constant;
import fi.joutsijoki.FileHandler;
import fi.joutsijoki.GameField;
import fi.joutsijoki.Level;
import fi.joutsijoki.Main;
import fi.joutsijoki.PathHandler;
import fi.joutsijoki.TowerStatistics;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;
import fi.joutsijoki.enemy.Mob;
import fi.joutsijoki.game.GameHud;
import fi.joutsijoki.game.GameInputHandler;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.Node;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 4.2.2016.
 */
public class GameScreen implements Screen {
    private Main parent;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private GameField gameField;
    private GameInputHandler inputHandler;
    private GameHud gameHud;
    private FileHandler fileHandler;

    private PathHandler pathHandler;
    private Array<Enemy> enemyList;
    private Array<Tower> towerList;
    private final int SPAWN_INTERVAL_MS = 1000;
    private final int PEACE_TIME = 5000;

    private int money = 200;
    private int currentWave = 0;
    private int currentHealth = 30;

    private ShapeRenderer debugRenderer;

    public GameScreen(Main parent) {
        this.parent = parent;
    }

    private void init() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        debugRenderer = new ShapeRenderer();

        Constant.setDimensions(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight());
        fileHandler = new FileHandler();
        gameField = fileHandler.loadMap("level-one-farmland.json");

        gameHud = new GameHud(this);

        inputHandler = new GameInputHandler(this, gameField, gameHud);

        InputMultiplexer impx = new InputMultiplexer();
        impx.addProcessor(gameHud.getStage());
        impx.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(impx);

        fetchTowers();
        startLevel();
    }

    public void addToTowerList(Tower t) {
        this.towerList.add(t);
    }

    public void buyTower(AssetLoader.TOWER_TEXTURE towerTexture, TowerStatistics.Statistic statistic) {
        if (money >= statistic.cost) {
            addToTowerList(gameField.buildTower(towerTexture, statistic));
            money -= statistic.cost;
        }
    }

    public void fetchTowers() {
        this.towerList = new Array<Tower>();

        for (Node n : this.gameField.getNodes()) {
            if (n.towerRef != null) {
                this.towerList.add(n.towerRef);
            }
        }
    }

    private void startLevel() {
        pathHandler = new PathHandler(this.gameField.getGraphImpl());
        Array<GraphPathImpl> paths = new Array<GraphPathImpl>();

        for (Node n : this.gameField.getNodes()) {
            if (n.end) {
                Node endNode = n;
                int startNodeIndex = n.getPathStartNodeIndex();

                GraphPathImpl path = pathHandler.searchPath(this.gameField.getNodes().get(startNodeIndex), endNode);
                paths.add(path);
            }
        }

        Level level = new Level(1);
        startLevelThread(level, paths);
    }

    private void startLevelThread(final Level level, final Array<GraphPathImpl> paths) {
        enemyList = new Array<Enemy>();

        Thread levelThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(!level.isLevelComplete()) {
                    spawnWave(level, paths);
                    level.setCurrentWaveIndex(level.getCurrentWaveIndex() + 1);
                    System.out.println("WAVE FINISHED");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("LEVEL FINISHED");
            }
        });
        levelThread.start();
    }

    private void spawnWave(final Level level, final Array<GraphPathImpl> paths) {
        spawnInitialMobs(level.getCurrentWave(), paths);
        int mobsSpawned = 1;

        long startTime = System.currentTimeMillis();

        while(!level.spawningComplete(mobsSpawned)) {
            moveMobs();
            checkTowers();

            long timePassed = System.currentTimeMillis() - startTime;

            if (timePassed >= SPAWN_INTERVAL_MS) {
                mobsSpawned++;
                startTime = System.currentTimeMillis();

                spawnInitialMobs(level.getCurrentWave(), paths);
            }

            sleep();
        }

        while(!level.isWaveFinished(enemyList)) {
            moveMobs();
            checkTowers();
            sleep();
        }

        clearProjectiles();
    }

    private void clearProjectiles() {
        for (int i = 0; i < this.towerList.size; i++) {
            Tower t = this.towerList.get(i);

            t.clearProjectiles();
        }
    }

    public void spawnInitialMobs(Level.Wave wave, Array<GraphPathImpl> paths) {
        for (GraphPathImpl path : paths) {
            Node startNode = path.get(0);
            Enemy e = new Enemy(wave.enemy.getTexture(), wave.enemy.getCurrentHealth(), startNode.x, startNode.y, wave.enemy.getBounty(), this);
            e.setPath(path);
            enemyList.add(e);
        }
    }

    public void increaseMoney(int money) {
        this.money += money;
    }

    public void moveMobs() {
        Array<Enemy> temp = new Array<Enemy>();

        for (int i = 0; i < enemyList.size; i++) {
            Enemy e = enemyList.get(i);

            if (e.isAtEnd()) {
                reduceHealth(1);
            }

            if (e.isAtEnd() || !e.isAlive()) {
                temp.add(e);
            } else {
                e.update();
            }
        }

        this.enemyList.removeAll(temp, true);
    }

    private void reduceHealth(int amount) {
        this.currentHealth -= amount;
        // TODO game over
    }

    private void checkTowers() {
        if (this.towerList == null) {
            return;
        }

        for (Tower t : towerList) {
            t.updateTower();

            for (Enemy e : enemyList) {
                float distance = Utils.centerPos(t.getPos()).dst(Utils.centerPos(e.getPos()));

                if (distance < t.getRadius() * Constant.CELL_WIDTH) {
                    if (t.isLockedAtTarget() && !t.isTargetDead()) {
                        t.shoot();
                    } else {
                        t.setTarget(e);
                    }
                }
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        init();
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw the gamefield
        batch.begin();
        gameField.draw(batch, false);
        drawGame();
        batch.end();

        gameField.drawGrid(debugRenderer);


        // Draw the HUD (menu)
        batch.begin();
        this.gameHud.tick(delta);
        batch.end();

        // Draw the status (health left, current wave etc)
        batch.begin();
        this.gameHud.drawStatus(batch, this.currentHealth, this.money, 15, 30, 3);
        this.gameHud.drawTowerDescriptions(batch);
        batch.end();

    }

    private void drawGame() {
        if (enemyList != null) {
            for (int i = 0; i < enemyList.size; i++) {
                Enemy e = enemyList.get(i);

                e.draw(batch);
            }
        }

        if (towerList != null) {
            for (int i = 0; i < towerList.size; i++) {
                Tower t = towerList.get(i);

                t.drawTower(batch);
                t.updateProjectiles();
                t.drawProjectiles(batch);
            }
        }
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
}
