package fi.joutsijoki;

import com.badlogic.gdx.Game;

import fi.joutsijoki.screens.Editor;
import fi.joutsijoki.screens.GameScreen;
import fi.joutsijoki.screens.MainMenu;
import fi.joutsijoki.screens.ModeSelection;

public class Main extends Game {
	public static MainMenu mainMenuScreen;
	public static Editor editorScreen;
	public static ModeSelection modeSelectionScreen;
	public static GameScreen gameScreen;
	public static AssetLoader assetLoader;

	@Override
	public void create () {
		assetLoader = new AssetLoader();

		mainMenuScreen = new MainMenu(this);
		editorScreen = new Editor(this);
		modeSelectionScreen = new ModeSelection(this);
		gameScreen = new GameScreen(this);

		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
}
