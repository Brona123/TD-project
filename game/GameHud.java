package fi.joutsijoki.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.TowerStatistics;
import fi.joutsijoki.Utils;
import fi.joutsijoki.screens.GameScreen;

/**
 * Created by Sami on 4.2.2016.
 */
public class GameHud {
    private GameScreen parent;
    private Stage stage;

    private int GAME_FIELD_WIDTH;
    private int GAME_FIELD_HEIGHT;
    private int MENU_LEFT_X;
    private int MENU_WIDTH;
    private int MENU_HEIGHT;

    private float statusTableX;
    private float statusTableY;
    private float statusTableWidth;
    private float statusTableHeight;

    private float towerDescriptionX;
    private float towerDescriptionY;
    private float towerDescriptionWidth;
    private float towerDescriptionHeight;

    private BitmapFont statusFont;
    private Matrix4 fontProjectionMatrix;

    private AssetLoader.TOWER_TEXTURE selectedTowerTexture;
    private TowerStatistics.Statistic selectedTowerStatistic;

    public GameHud(GameScreen parent) {
        this.parent = parent;

        stage = new Stage();
        statusFont = new BitmapFont();
        statusFont.getData().setScale(1.5f);
        fontProjectionMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        MENU_WIDTH = Gdx.graphics.getWidth() / 6;
        MENU_LEFT_X = Gdx.graphics.getWidth() - MENU_WIDTH;
        GAME_FIELD_WIDTH = Gdx.graphics.getWidth() - MENU_WIDTH;
        GAME_FIELD_HEIGHT = Gdx.graphics.getHeight();
        MENU_HEIGHT = Gdx.graphics.getHeight() / 8;


        int statusTableHeight = 100;
        Table statusTable = createStatusTable(statusTableHeight);

        Table menuContainer = new Table();
        menuContainer.setBounds(MENU_LEFT_X, statusTable.getY() - MENU_HEIGHT, MENU_WIDTH, MENU_HEIGHT);
        menuContainer.setBackground(Utils.newSpriteDrawable(AssetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BORDER)));
        menuContainer.add(createTowerSelectionScrollPane());

        Table towerDescriptionTable = createTowerDescriptionTable(Gdx.graphics.getHeight() - (statusTableHeight + menuContainer.getHeight()));

        ImageButton buyTowerButton = new ImageButton(Utils.newSpriteDrawable(AssetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BUY_TOWER)));
        buyTowerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("BOUGHT TOWER");
                buyTower();
            }
        });
        buyTowerButton.setBounds(MENU_LEFT_X, 50, MENU_WIDTH, 60);

        stage.addActor(statusTable);
        stage.addActor(menuContainer);
        stage.addActor(towerDescriptionTable);
        stage.addActor(buyTowerButton);

        this.statusTableX = statusTable.getX();
        this.statusTableY = statusTable.getY();
        this.statusTableWidth = statusTable.getWidth();
        this.statusTableHeight = statusTable.getHeight();

        this.towerDescriptionX = towerDescriptionTable.getX();
        this.towerDescriptionY = towerDescriptionTable.getY();
        this.towerDescriptionWidth = towerDescriptionTable.getWidth();
        this.towerDescriptionHeight = towerDescriptionTable.getHeight();
    }

    private void buyTower() {
        parent.buyTower(this.selectedTowerTexture, this.selectedTowerStatistic);
    }

    private Table createTowerDescriptionTable(float height) {
        Table t = new Table();
        t.setBounds(MENU_LEFT_X, 0, MENU_WIDTH, height);
        t.setBackground(Utils.newSpriteDrawable(AssetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.TOWER_DESCRIPTION)));

        return t;
    }

    private Table createStatusTable(int statusTableHeight) {
        Table t = new Table();
        t.setBackground(Utils.newSpriteDrawable(AssetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.STATUS_BAR)));
        t.setBounds(MENU_LEFT_X, Gdx.graphics.getHeight() - statusTableHeight, MENU_WIDTH, statusTableHeight);

        return t;
    }

    private ScrollPane createTowerSelectionScrollPane() {
        final HashMap<String, AssetLoader.TOWER_TEXTURE> towerNameMap = new HashMap<String, AssetLoader.TOWER_TEXTURE>();
        towerNameMap.put("ice", AssetLoader.TOWER_TEXTURE.ICE);
        towerNameMap.put("shadow", AssetLoader.TOWER_TEXTURE.SHADOW);
        towerNameMap.put("poison", AssetLoader.TOWER_TEXTURE.POISON);
        towerNameMap.put("lightning", AssetLoader.TOWER_TEXTURE.LIGHTNING);
        towerNameMap.put("shadow-mage", AssetLoader.TOWER_TEXTURE.SHADOW_MAGE_IDLE);
        towerNameMap.put("dwarf", AssetLoader.TOWER_TEXTURE.DWARF_IDLE);

        final Table t = new Table();
        final Array<ImageButton> towerButtonList = new Array<ImageButton>();

        for (String key : towerNameMap.keySet()) {
            ImageButton ib = Utils.buildImageButton(AssetLoader.getTowerTexture(towerNameMap.get(key)), key);
            towerButtonList.add(ib);

            t.add(ib);
        }

        ScrollPane sp = new ScrollPane(t);
        sp.setScrollingDisabled(false, true);
        sp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (ImageButton ib : towerButtonList) {
                    if (ib.getName().equals(actor.getName())) {
                        setTowerSelected(towerNameMap.get(ib.getName()));
                    }
                }
            }
        });

        return sp;
    }

    public void setTowerSelected(AssetLoader.TOWER_TEXTURE towerTexture) {
        this.selectedTowerTexture = towerTexture;
        selectedTowerStatistic = TowerStatistics.getStatisticsObject(towerTexture);
    }

    public AssetLoader.TOWER_TEXTURE getSelectedTowerTexture() {
        return this.selectedTowerTexture;
    }

    public void tick(float delta) {
        // TODO STATUS TEXT
        this.stage.act(delta);
        this.stage.draw();
    }

    public void drawStatus(SpriteBatch batch, int healthLeft, int money, int currentWave, int totalWaves, int secondsUntilNextWave) {
        batch.setProjectionMatrix(fontProjectionMatrix);
        statusFont.setColor(1.0f, .0f, .0f, 1.0f);

        statusFont.draw(batch
                , "" + healthLeft
                , this.statusTableX + this.statusTableWidth / 3
                , this.statusTableY + this.statusTableHeight - (this.statusTableHeight / 20));

        statusFont.draw(batch
                , "" + money
                , this.statusTableX + this.statusTableWidth / 3
                , this.statusTableY + this.statusTableHeight - (this.statusTableHeight / 2));

        statusFont.draw(batch
                , "" + currentWave + "/" + totalWaves
                , this.statusTableX + this.statusTableWidth - (this.statusTableWidth / 4)
                , this.statusTableY + this.statusTableHeight - (this.statusTableHeight / 20));

        statusFont.draw(batch
                , "" + secondsUntilNextWave + "s"
                , this.statusTableX + this.statusTableWidth - (this.statusTableWidth / 4)
                , this.statusTableY + this.statusTableHeight - (this.statusTableHeight / 2));
    }

    public void drawTowerDescriptions(SpriteBatch batch) {
        if (this.selectedTowerStatistic != null) {
            batch.setProjectionMatrix(fontProjectionMatrix);
            statusFont.setColor(1.0f, .0f, .0f, 1.0f);

            statusFont.draw(batch
                    , "Damage: " + this.selectedTowerStatistic.damage
                    , this.towerDescriptionX + this.towerDescriptionWidth / 4
                    , this.towerDescriptionY + this.towerDescriptionHeight - 100);

            statusFont.draw(batch
                    , "Radius: " + this.selectedTowerStatistic.radius
                    , this.towerDescriptionX + this.towerDescriptionWidth / 4
                    , this.towerDescriptionY + this.towerDescriptionHeight - 150);

            statusFont.draw(batch
                    , "Cooldown: " + this.selectedTowerStatistic.cooldown
                    , this.towerDescriptionX + this.towerDescriptionWidth / 4
                    , this.towerDescriptionY + this.towerDescriptionHeight - 200);

            statusFont.draw(batch
                    , "Cost: " + this.selectedTowerStatistic.cost
                    , this.towerDescriptionX + this.towerDescriptionWidth / 4
                    , this.towerDescriptionY + this.towerDescriptionHeight - 250);
        }
    }

    public Stage getStage() {
        return this.stage;
    }
}
