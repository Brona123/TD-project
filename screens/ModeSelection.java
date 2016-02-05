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
public class ModeSelection implements Screen {
    private Main parent;
    private Stage modeSelectionStage;

    public ModeSelection(Main parent) {
        this.parent = parent;
    }

    private void init() {
        this.modeSelectionStage = new Stage();

        ImageButton campaignButton = new ImageButton(Utils.newSpriteDrawable(AssetLoader.getMenuTexture(AssetLoader.MENU_TEXTURE.CAMPAIGN)));
        campaignButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.setScreen(parent.gameScreen);
            }
        });

        ImageButton endlessButton = new ImageButton(Utils.newSpriteDrawable(AssetLoader.getMenuTexture(AssetLoader.MENU_TEXTURE.ENDLESS)));
        endlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        Table buttonTable = new Table();
        buttonTable.setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 128, 64 * 3);
        buttonTable.add(campaignButton).row();
        buttonTable.add(endlessButton).row();

        this.modeSelectionStage.addActor(buttonTable);

        Gdx.input.setInputProcessor(this.modeSelectionStage);
    }

    @Override
    public void show() {
        init();
    }

    @Override
    public void render(float delta) {
        this.modeSelectionStage.act(delta);
        this.modeSelectionStage.draw();
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
