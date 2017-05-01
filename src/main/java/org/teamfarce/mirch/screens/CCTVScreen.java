package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.map.Room;
import org.teamfarce.mirch.screens.elements.StatusBar;

import java.util.ArrayList;
import java.util.List;

public class CCTVScreen extends AbstractScreen {

	private static final int CCTV_WIDTH = 500;
	private static final int CCTV_HEIGHT = 500;
	private static final int PUZZLE_X = Gdx.graphics.getWidth()/2 - CCTV_WIDTH/2;
	private static final int PUZZLE_Y = Gdx.graphics.getHeight()/2 - CCTV_HEIGHT/2;

	private Stage CCTVStage;
	private Skin uiSkin;
	private StatusBar statusBar;


	public CCTVScreen(MIRCH game, Skin uiSkin) {
		super(game);

		this.uiSkin = uiSkin;
		statusBar = new StatusBar(game, uiSkin);
	}

	private void initStage() {
		CCTVStage = new Stage();

		BitmapFont font = new BitmapFont();
		Label.LabelStyle textStyle = new Label.LabelStyle(font, Color.GREEN);
		Label intro = new Label("Sentient CCTV Pro 7.8 System accessed\nUsername: n00bKiller\nPassword: ********\n" +
				"Using the AI knowledge of ARIN and vision knowledge of VIGR, I have analysed your work, as\n" +
				"well as the available CCTV footage and have deduced that these are the three most likely\n" +
				"perpetrators. I would suggest focusing your efforts on them.\nGood luck.", textStyle);


		List<String> cctvStrings = MIRCH.me.gameSnapshot.getCCTVSuspects();
		List<Label> cctvClues = new ArrayList<>();
		cctvClues.add(new Label(cctvStrings.get(0), textStyle));
		cctvClues.add(new Label(cctvStrings.get(1), textStyle));
		cctvClues.add(new Label(cctvStrings.get(2), textStyle));

		intro.setPosition(100.0f, 490.0f);

		CCTVStage.addActor(intro);

		int i = 0;
		for (Label label : cctvClues) {
			label.setPosition(100.0f, 400 - (30 * i));
			CCTVStage.addActor(label);
			i++;
		}

	}

	@Override
	public void show() {
		initStage();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(CCTVStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		CCTVStage.act();
		CCTVStage.draw();
        statusBar.render();
	}

	@Override
	public void resize(int width, int height) {
		CCTVStage.getViewport().update(width, height, false);
        statusBar.resize(width, height);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		CCTVStage.dispose();
		statusBar.dispose();
	}


}
