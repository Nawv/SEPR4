package org.teamfarce.mirch.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;

import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.OrthogonalTiledMapRendererWithPeople;
import org.teamfarce.mirch.entities.AbstractPerson;
import org.teamfarce.mirch.entities.PlayerController;
import org.teamfarce.mirch.entities.Suspect;
import org.teamfarce.mirch.screens.elements.RoomArrow;
import org.teamfarce.mirch.screens.elements.StatusBar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by brookehatton on 31/01/2017.
 */
public class MapScreen extends AbstractScreen {
	
	public final float PLAY_TIME = 5.0f;

    /**
     * This stores the most recent frame as an image
     */
    public static Image recentFrame;
    public static boolean grabScreenshot = false;
    /**
     * This is the list of NPCs who are in the current room
     */
    List<Suspect> currentNPCs = new ArrayList<Suspect>();
    private OrthogonalTiledMapRendererWithPeople tileRender;
    private OrthographicCamera camera;
    private PlayerController playerController;

    /**
     * This stores the room arrow that is drawn when the player stands on a room changing mat
     */
    private RoomArrow arrow = new RoomArrow(game);
    /**
     * This is the sprite batch that is relative to the screens origin
     */
    private SpriteBatch spriteBatch;
    /**
     * This stores whether the room is currently in transition or not
     */
    private boolean roomTransition = false;
    /**
     * The amount of ticks it takes for the black to fade in and out
     */
    private float ANIM_TIME = 0.7f;

    /**
     * The black sprite that is used to fade in/out
     */
    private Sprite BLACK_BACKGROUND = new Sprite();
    /**
     * The current animation frame of the fading in/out
     */
    private float animTimer = 0.0f;
    /**
     * This boolean determines whether the black is fading in or out
     */
    private boolean fadeToBlack = true;
    private StatusBar statusBar;
    
    private float playTime = 0.0f;
    private boolean gameTransition = false;
    private boolean gameTransitionPause = false;

    public MapScreen(MIRCH game, Skin uiSkin) {
        super(game);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, w, h);
        this.camera.update();
        this.tileRender = new OrthogonalTiledMapRendererWithPeople(game.player.getRoom().getTiledMap());
        this.tileRender.addPerson(game.player);
        currentNPCs = game.gameSnapshot.map.getNPCs(game.player.getRoom());
        tileRender.addPerson((List<AbstractPerson>) ((List<? extends AbstractPerson>) currentNPCs));
        this.playerController = new PlayerController(game, camera);
        this.spriteBatch = new SpriteBatch();

        Pixmap pixMap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);

        pixMap.setColor(Color.BLACK);
        pixMap.fill();

        BLACK_BACKGROUND = new Sprite(new Texture(pixMap));

        this.statusBar = new StatusBar(game, uiSkin);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(statusBar.stage);
        multiplexer.addProcessor(playerController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
    	if (!gameTransition)
    		game.gameSnapshot.updateScore(delta);
        
        playTime += delta;
        if (playTime > PLAY_TIME) {
            gameTransition = true;
            playTime = PLAY_TIME;
        }
        
        if (playerController.anyKeyPressed()) {
        	gameTransitionPause = false;
        }
        
        playerController.update(delta);
        game.player.update(delta);

        //loop through each suspect character, moving them randomly
        for (Suspect character : currentNPCs) {
            character.update(delta);
        }

        camera.position.x = game.player.getX();
        camera.position.y = game.player.getY();
        camera.update();
        tileRender.setView(camera);

        tileRender.render();
        tileRender.getBatch().begin();
        arrow.update();
        arrow.draw(tileRender.getBatch());
        game.player.getRoom().drawClues(delta, getTileRenderer().getBatch());

        tileRender.getBatch().end();

        updateTransition(delta);

        //Everything to be drawn relative to bottom left of the screen
        spriteBatch.begin();

        if (roomTransition || gameTransition) {
            BLACK_BACKGROUND.draw(spriteBatch);
        }
        
        if (gameTransitionPause) {
        	if (game.game1) {
        		Assets.LAYOUT.setText(Assets.FONT30, "Player 1");
        	} else {
        		Assets.LAYOUT.setText(Assets.FONT30, "Player 2");
        	}
        	Assets.FONT30.draw(spriteBatch, Assets.LAYOUT, (Gdx.graphics.getWidth() - Assets.LAYOUT.width)/2,
        			500);
        	Assets.LAYOUT.setText(Assets.FONT30, "Press Any Key to Continue");
        	Assets.FONT30.draw(spriteBatch, Assets.LAYOUT, (Gdx.graphics.getWidth() - Assets.LAYOUT.width)/2,
        			400);
        }

        spriteBatch.end();

        if (!grabScreenshot) {
            statusBar.render();
        }

        if (grabScreenshot) {
            recentFrame = new Image(ScreenUtils.getFrameBufferTexture());
        }
    }

    /**
     * This is called when the player decides to move to another room
     */
    public void initialiseRoomTransition() {
        game.gameSnapshot.setAllUnlocked();
        roomTransition = true;
    }

    /**
     * This is called when the room transition animation has completed so the necessary variables
     * can be returned to their normal values
     */
    public void finishTransition() {
        animTimer = 0;
        roomTransition = false;
        gameTransition = false;
        fadeToBlack = true;
        playTime = 0.0f;
    }

    /**
     * This method returns true if the game is currently transitioning between rooms
     */
    public boolean isTransitioning() {
        return roomTransition || gameTransition;
    }

    /**
     * This method is called once a render loop to update the room transition animation
     */
    private void updateTransition(float delta) {
        if (roomTransition || gameTransition && !gameTransitionPause) {
            BLACK_BACKGROUND.setAlpha(Interpolation.pow4.apply(0, 1, animTimer / ANIM_TIME));

            if (fadeToBlack) {
                animTimer += delta;

                if (animTimer >= ANIM_TIME) {
                	if (roomTransition) {
	                    game.player.moveRoom();
	                    currentNPCs = game.gameSnapshot.map.getNPCs(game.player.getRoom());
	                    getTileRenderer().setMap(game.player.getRoom().getTiledMap());
	                    getTileRenderer().clearPeople();
	                    getTileRenderer().addPerson((List<AbstractPerson>) ((List<? extends AbstractPerson>) currentNPCs));
	                    getTileRenderer().addPerson(game.player);
                	} else {
                    	switchGame();
                        gameTransitionPause = true;
                	}
                    fadeToBlack = false;
                }
            } else {
                animTimer -= delta;

                if (animTimer <= 0f) {
                    finishTransition();
                }
            }
        }

        if (game.player.roomChange) {
            initialiseRoomTransition();
            game.player.roomChange = false;
        }
    }
    
    public void switchGame() {
        game.game1 = !game.game1;
        if (game.game1) {
        	game.gameSnapshot = game.game1Snapshot;
        	game.rooms = game.game1Rooms;
        	game.characters = game.game1Characters;
        	game.player = game.player1;
        } else {
        	game.gameSnapshot = game.game2Snapshot;
        	game.rooms = game.game2Rooms;
        	game.characters = game.game2Characters;
        	game.player = game.player2;
        }
        
        currentNPCs = game.gameSnapshot.map.getNPCs(game.player.getRoom());
        getTileRenderer().setMap(game.player.getRoom().getTiledMap());
        getTileRenderer().clearPeople();
        getTileRenderer().addPerson((List<AbstractPerson>) ((List<? extends AbstractPerson>) currentNPCs));
        getTileRenderer().addPerson(game.player);
    }

    /**
     * This method returns the NPCs on the current map
     *
     * @return List<Suspect> - The Suspects on the current map
     */
    public List<Suspect> getNPCs() {
        return currentNPCs;
    }
    
    public float getPlayTime() {
    	return playTime;
    }

    @Override
    public void resize(int width, int height) {
        statusBar.resize(width, height);
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
        statusBar.dispose();
    }

    public OrthogonalTiledMapRendererWithPeople getTileRenderer() {
        return tileRender;
    }
}
