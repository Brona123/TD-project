package fi.joutsijoki.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Main;
import fi.joutsijoki.Utils;

/**
 * Created by Sami on 3.2.2016.
 */
public class MainMenu implements Screen {
    private Main parent;
    private Stage mainMenuStage;

    public MainMenu(Main parent) {
        this.parent = parent;
    }

    @Override
    public void show() {
        init();
    }

    private void init() {
        this.mainMenuStage = new Stage();

        ImageButton playButton = new ImageButton(Utils.newSpriteDrawable(AssetLoader.getMenuTexture(AssetLoader.MENU_TEXTURE.PLAY)));
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(parent.modeSelectionScreen);
            }
        });

        ImageButton editorButton = new ImageButton(Utils.newSpriteDrawable(AssetLoader.getMenuTexture(AssetLoader.MENU_TEXTURE.EDITOR)));
        editorButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("GO TO EDITOR SCREEN");
                parent.setScreen(parent.editorScreen);
            }
        });

        Table buttonTable = new Table();
        buttonTable.setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 128, 64 * 3);
        buttonTable.add(playButton).row();
        buttonTable.add(editorButton).row();

        this.mainMenuStage.addActor(buttonTable);

        Gdx.input.setInputProcessor(this.mainMenuStage);
    }

    @Override
    public void render(float delta) {
        this.mainMenuStage.act(delta);
        this.mainMenuStage.draw();
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
