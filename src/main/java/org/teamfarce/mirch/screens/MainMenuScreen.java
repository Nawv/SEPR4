package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameSnapshot;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.Settings;

/**
 * The Main Menu screen draws the Main Menu GUI to the screen and handles any inputs
 * whilst the Menu is displayed.
 */
public class MainMenuScreen extends AbstractScreen {

    /**
     * The width of the menu
     */
    private static final int BUTTON_WIDTH = Gdx.graphics.getWidth() / 3;
    private static final int BUTTON_HEIGHT = 80;
    private static final int CENTER_MARGIN = 30;

    //Initialising necessary objects and variables
    /**
     * the stage to render the menu to
     */
    public Stage stage;
    /**
     * This is the referencing to the game snapshot
     */
    private GameSnapshot gameSnapshot;

    /**
     * Constructor for the menu
     *
     * @param game - The game object the menu is being loaded for
     */
    public MainMenuScreen(final MIRCH game, Skin uiSkin) {

        super(game);
        this.gameSnapshot = game.gameSnapshot;

        //Initialising new stage
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //Loading the menu
        initMenu(uiSkin);
    }

    private void initMenu(Skin uiSkin) {

        //Setting the background
        Image background = new Image(new TextureRegion(Assets.loadTexture("menuBackground.jpg")));

        //Creating the labelstyle used for the text in the label.
        LabelStyle textStyle = new LabelStyle(Assets.FONT30, Color.WHITE);

        //Creating the label containing text and determining  its size and location on screen
        Label title = new Label("Murder in the Ron Cooke Hub", textStyle);
        title.setBounds(Gdx.graphics.getWidth() / 2 - title.getWidth()/2, 43*Gdx.graphics.getHeight() / 48, title.getWidth(), title.getHeight());

        Label subtitle = new Label("by Team JAAPAN", textStyle);
        subtitle.setBounds(Gdx.graphics.getWidth() / 2 - subtitle.getWidth()/2, 5*Gdx.graphics.getHeight() / 6, subtitle.getWidth(), subtitle.getHeight());

        // Creating the buttons and setting their positions
        // Added by Team JAAPAN
        TextButton singlePlayerButton = new TextButton("Single Player", uiSkin);

        singlePlayerButton.setPosition((Gdx.graphics.getWidth() / 2) - (BUTTON_WIDTH / 2),
        		(Gdx.graphics.getHeight() / 2) + CENTER_MARGIN + BUTTON_HEIGHT);
        singlePlayerButton.getLabel().setFontScale(3 / 2, 3 / 2);
        singlePlayerButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        TextButton twoPlayerButton = new TextButton("Two Player", uiSkin);
        twoPlayerButton.setPosition((Gdx.graphics.getWidth() / 2) - (BUTTON_WIDTH / 2),
        		(Gdx.graphics.getHeight() / 2));
        twoPlayerButton.getLabel().setFontScale(3 / 2, 3 / 2);
        twoPlayerButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        TextButton quit = new TextButton("Quit", uiSkin);
        quit.getLabel().setFontScale(3 / 2, 3 / 2);
        quit.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        quit.setPosition((Gdx.graphics.getWidth() / 2) - (BUTTON_WIDTH / 2),
        		(Gdx.graphics.getHeight() / 2)- BUTTON_HEIGHT - CENTER_MARGIN);

        // Making the "Single Player" button clickable and causing it to start the game
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSnapshot.setState(GameState.narrator);
            }
        });
        
        // Making the "Two Player" button clickable and causing it to start the game
        // Added by Team JAAPAN
        twoPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameSnapshot.setState(GameState.narrator);
                Settings.TWO_PLAYER = true;
            }
        });

        // Making the "Quit" button clickable and causing it to close the game
        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Loading the buttons onto the stage
        stage.addActor(background);
        stage.addActor(title);
        stage.addActor(subtitle);
        stage.addActor(singlePlayerButton);
        stage.addActor(twoPlayerButton);
        stage.addActor(quit);
    }

    /**
     * This method is called to render the main menu to the stage
     */
    public void render(float delta) {
        //Determining the background colour of the menu
        Gdx.gl.glClearColor(135, 206, 235, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Rendering the buttons
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
    }

    /**
     * This method disposes of all elements
     */
    public void dispose() {
        //Called when disposing the main menu
        stage.dispose();
    }

    /**
     * This method is called when the window is resized.
     *
     * @param width  - The new width
     * @param height - The new height
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
};
