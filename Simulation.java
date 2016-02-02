package fi.joutsijoki;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import fi.joutsijoki.enemy.Enemy;
import fi.joutsijoki.enemy.Mob;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.Node;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 12.1.2016.
 */
public class Simulation {
    private Labyrinth lab;
    private PathHandler pathHandler;
    private Array<Enemy> mobList;
    private Array<Tower> towerList;
    private final int SPAWN_INTERVAL_MS = 1000;
    private Level currentLevel;

    public void setLabyrinth(Labyrinth lab) {
        this.lab = lab;
    }

    public void fetchTowers() {
        this.towerList = new Array<Tower>();

        for (Node n : lab.getNodes()) {
            if (n.towerRef != null) {
                this.towerList.add(n.towerRef);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (mobList != null) {
            for (int i = 0; i < mobList.size; i++) {
                Enemy e = mobList.get(i);

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

    public void start() {
        pathHandler = new PathHandler(this.lab.getGraphImpl());
        Array<GraphPathImpl> paths = new Array<GraphPathImpl>();

        for (Node n : this.lab.getNodes()) {
            if (n.end) {
                Node endNode = n;
                int startNodeIndex = n.getPathStartNodeIndex();

                GraphPathImpl path = pathHandler.searchPath(this.lab.getNodes().get(startNodeIndex), endNode);
                paths.add(path);
            }
        }

        for (GraphPathImpl path : paths) {

            Iterator<Node> it = path.iterator();
            while (it.hasNext()) {
                it.next().path = true;
            }
        }

        this.currentLevel = new Level(1);
        beginMobSimulations(this.currentLevel, paths);
    }

    private void beginMobSimulations(final Level level, final Array<GraphPathImpl> paths) {
        mobList = new Array<Enemy>();

        Thread levelInProgress = new Thread(new Runnable() {
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
        levelInProgress.start();
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

        while(!level.isWaveFinished(mobList)) {
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
            Mob m = new Mob(wave.enemy.getTexture(), wave.enemy.getCurrentHealth(), startNode.x, startNode.y, wave.enemy.getBounty());
            m.setPath(path);
            mobList.add(m);
        }
    }

    public void moveMobs() {
        Array<Enemy> temp = new Array<Enemy>();

        for (int i = 0; i < mobList.size; i++) {
            Enemy e = mobList.get(i);

            if (e.isAtEnd() || !e.isAlive()) {
                temp.add(e);
            } else {
                e.update();
            }
        }

        this.mobList.removeAll(temp, true);
    }

    private void checkTowers() {
        if (this.towerList == null) {
            return;
        }

        for (Tower t : towerList) {
            t.updateTower();

            for (Enemy e : mobList) {
                float distance = Utils.centerPos(t.getPos()).dst(Utils.centerPos(e.getPos()));

                if (distance < t.getRadius() * Labyrinth.CELL_WIDTH) {
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
}
