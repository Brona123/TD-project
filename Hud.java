package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Sami on 30.12.2015.
 */
public class Hud {
    private Main parent;
    private Stage holder;
    private TextButton exportButton;
    private TextButton loadButton;
    private ImageButton selectedObjectButton;
    private ImageButton selectedTowerButton;
    public static AssetLoader.OBJECT_TEXTURE selectedObject;
    public static AssetLoader.TOWER_TEXTURE selectedTower;

    private HashMap<String, AssetLoader.OBJECT_TEXTURE> objectNameMap;
    private HashMap<String, AssetLoader.TOWER_TEXTURE> towerNameMap;

    public Hud(Main parent, Stage holder) {
        this.parent = parent;
        this.holder = holder;

        createImagePane(holder);
        createControlPane(holder);
        createTowerPane(holder);
    }

    public void drawHolder() {
        this.holder.act();
        this.holder.draw();
    }

    public void addButtons() {
        final TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = new BitmapFont();
        btnStyle.fontColor = Color.WHITE;
        btnStyle.overFontColor = Color.RED;
        btnStyle.checkedOffsetX = 5;
        btnStyle.checkedOffsetY = 5;
        btnStyle.checkedFontColor = Color.BLUE;

        exportButton = new TextButton("Export", btnStyle);
        exportButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        parent.exportMap(text);
                    }

                    @Override
                    public void canceled() {
                        System.out.println("canceled dialog");
                    }
                };

                Gdx.input.getTextInput(listener, "Enter file name", "", "example.json");
            }
        });

        loadButton = new TextButton("Load", btnStyle);
        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileHandle[] files = Gdx.files.local(Constant.ROOT_ASSET_FOLDER + Constant.SERIALIZED_LEVELS_FOLDER).list();

                BitmapFont font = new BitmapFont();
                font.setColor(1f, 0.5f, 0f, 1f);

                Window.WindowStyle windowStyle = new Window.WindowStyle(font, font.getColor(), newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.DIALOG_BG)));
                final Dialog dialog = new Dialog("Load map", windowStyle);

                for (FileHandle fh : files) {
                    String fileName = fh.name();
                    System.out.println("FILENAME: " + fileName);

                    TextButton b = new TextButton(fileName, btnStyle);
                    b.setName(fileName);
                    b.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            System.out.println(actor.getName());
                            dialog.hide();
                            parent.loadMap(actor.getName());
                        }
                    });

                    dialog.getContentTable().add(b).width(200).height(100).expand().left();
                    dialog.getContentTable().row();
                }

                dialog.show(holder);
            }
        });

        TextButton simulationsButton = new TextButton("Run simulations", btnStyle);
        simulationsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.runSimulations();
            }
        });

        Table buttonTable = new Table();
        buttonTable.setBackground(newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BORDER)));
        buttonTable.setBounds(Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 64 * 10, 128, 64 * 3);
        buttonTable.add(exportButton);
        buttonTable.row();
        buttonTable.add(loadButton);
        buttonTable.row();
        buttonTable.add(simulationsButton);
        buttonTable.row();

        holder.addActor(buttonTable);
    }

    public SpriteDrawable newSpriteDrawable(Texture t) {
        return new SpriteDrawable(new Sprite(t));
    }

    public void createImagePane(Stage stage) {
        objectNameMap = new HashMap<String, AssetLoader.OBJECT_TEXTURE>();
        objectNameMap.put("bush", AssetLoader.OBJECT_TEXTURE.BUSH);
        objectNameMap.put("rock", AssetLoader.OBJECT_TEXTURE.ROCK);
        objectNameMap.put("tree", AssetLoader.OBJECT_TEXTURE.TREE);
        objectNameMap.put("tree2", AssetLoader.OBJECT_TEXTURE.TREE_2);
        objectNameMap.put("pond", AssetLoader.OBJECT_TEXTURE.POND);

        final Table t = new Table();
        final Array<ImageButton> imageButtonList = new Array<ImageButton>();

        for (String key : objectNameMap.keySet()) {
            ImageButton ib = buildImageButton(Labyrinth.assetLoader.getObstacleTexture(objectNameMap.get(key)), key);
            imageButtonList.add(ib);

            t.add(ib);
            t.row();
        }

        ScrollPane sp = new ScrollPane(t);
        sp.setScrollingDisabled(true, false);
        sp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (ImageButton ib : imageButtonList) {
                    if (ib.getName().equals(actor.getName())) {
                        setObjectSelected(ib);
                    }
                }
            }
        });

        Table container = new Table();
        container.setBounds(Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 64 * 4, 128, 64 * 4);
        container.add(sp).fill();
        container.setBackground(newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BORDER)));

        stage.addActor(container);
    }

    private void createTowerPane(Stage stage) {
        towerNameMap = new HashMap<String, AssetLoader.TOWER_TEXTURE>();
        towerNameMap.put("ice", AssetLoader.TOWER_TEXTURE.ICE);
        towerNameMap.put("shadow", AssetLoader.TOWER_TEXTURE.SHADOW);
        towerNameMap.put("poison", AssetLoader.TOWER_TEXTURE.POISON);
        towerNameMap.put("lightning", AssetLoader.TOWER_TEXTURE.LIGHTNING);
        towerNameMap.put("shadow-mage", AssetLoader.TOWER_TEXTURE.SHADOW_MAGE_IDLE);
        towerNameMap.put("dwarf", AssetLoader.TOWER_TEXTURE.DWARF_IDLE);

        final Table t = new Table();
        final Array<ImageButton> towerButtonList = new Array<ImageButton>();

        for (String key : towerNameMap.keySet()) {
            ImageButton ib = buildImageButton(Labyrinth.assetLoader.getTowerTexture(towerNameMap.get(key)), key);
            towerButtonList.add(ib);

            t.add(ib);
            t.row();
        }

        ScrollPane sp = new ScrollPane(t);
        sp.setScrollingDisabled(true, false);
        sp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (ImageButton ib : towerButtonList) {
                    if (ib.getName().equals(actor.getName())) {
                        setTowerSelected(ib);
                    }
                }
            }
        });

        Table container = new Table();
        container.setBounds(Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 64 * 14, 128, 64 * 4);
        container.add(sp).fill();
        container.setBackground(newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BORDER)));

        stage.addActor(container);
    }

    private void createControlPane(Stage stage) {
        SpriteDrawable checkboxOn = newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.CHECKBOX_ON));
        SpriteDrawable checkboxOff = newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.CHECKBOX_OFF));
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle(checkboxOff, checkboxOn, new BitmapFont(), Color.YELLOW);

        CheckBox gridCheckBox = new CheckBox("Display Grid", style);
        gridCheckBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("EVENT: " + event);
                Labyrinth.drawGrid = !Labyrinth.drawGrid;
            }
        });

        CheckBox connectionsCheckBox = new CheckBox("Display Connections", style);
        connectionsCheckBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Labyrinth.drawConnections = !Labyrinth.drawConnections;
            }
        });

        CheckBox pathCheckBox = new CheckBox("Display Paths", style);
        pathCheckBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Labyrinth.drawPaths = !Labyrinth.drawPaths;
            }
        });

        CheckBox objectCheckBox = new CheckBox("Display Objects", style);
        objectCheckBox.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Labyrinth.drawObjects = !Labyrinth.drawObjects;
            }
        });

        Table controlPane = new Table();
        controlPane.setBounds(Gdx.graphics.getWidth() - 64 * 3, Gdx.graphics.getHeight() - 64 * 7, 64 * 3, 64 * 3);
        controlPane.setBackground(newSpriteDrawable(Labyrinth.assetLoader.getHudTexture(AssetLoader.HUD_TEXTURE.BORDER)));
        controlPane.row();
        controlPane.add(gridCheckBox).left();
        controlPane.row();
        controlPane.add(connectionsCheckBox).left();
        controlPane.row();
        controlPane.add(pathCheckBox).left();
        controlPane.row();
        controlPane.add(objectCheckBox).left();
        controlPane.row();

        stage.addActor(controlPane);
    }

    private void setObjectSelected(ImageButton ib) {
        if (this.selectedObjectButton == null) {
            this.selectedObjectButton = new ImageButton(((ib.getImage().getDrawable())));
            this.selectedObjectButton.setPosition(Gdx.graphics.getWidth() - 128 - 64, Gdx.graphics.getHeight() - 64);
            this.holder.addActor(this.selectedObjectButton);
        } else {
            this.selectedObjectButton.getStyle().imageUp = ib.getStyle().imageUp;
        }

        selectedObject = objectNameMap.get(ib.getName());
    }

    private void setTowerSelected(ImageButton ib) {
        if (this.selectedTowerButton == null) {
            this.selectedTowerButton = new ImageButton(((ib.getImage().getDrawable())));
            this.selectedTowerButton.setPosition(Gdx.graphics.getWidth() - 128 - 64, Gdx.graphics.getHeight() - 64 * 11);
            this.holder.addActor(this.selectedTowerButton);
        } else {
            this.selectedTowerButton.getStyle().imageUp = ib.getStyle().imageUp;
        }

        selectedTower = towerNameMap.get(ib.getName());
    }

    public ImageButton buildImageButton(Texture t, String name) {
        ImageButton.ImageButtonStyle ibs = new ImageButton.ImageButtonStyle();
        ibs.imageUp = new SpriteDrawable(new Sprite(t));
        ImageButton ib = new ImageButton(ibs);
        ib.setName(name);
        return ib;
    }

    public Stage getHolder() {
        return this.holder;
    }
}
