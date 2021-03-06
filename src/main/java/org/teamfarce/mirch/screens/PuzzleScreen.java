package org.teamfarce.mirch.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.teamfarce.mirch.Assets;
import org.teamfarce.mirch.GameState;
import org.teamfarce.mirch.MIRCH;
import org.teamfarce.mirch.screens.elements.StatusBar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Screen used to display and interact with the puzzle
 * 
 * @author Team JAAPAN
 *
 */
public class PuzzleScreen extends AbstractScreen {
	
	private static final int PUZZLE_WIDTH = 500;
	private static final int PUZZLE_HEIGHT = 500;
	private static final int PUZZLE_X = Gdx.graphics.getWidth()/2 - PUZZLE_WIDTH/2;
	private static final int PUZZLE_Y = Gdx.graphics.getHeight()/2 - PUZZLE_HEIGHT/2;
	
	private Stage puzzleStage;
	private Skin uiSkin;
	private StatusBar statusBar;
	
	private int[][] puzzle;

	public PuzzleScreen(MIRCH game, Skin uiSkin) {
		super(game);

		this.uiSkin = uiSkin;
		statusBar = new StatusBar(game, uiSkin);
	}
	
	private void initStage() {
		puzzleStage = new Stage();

		BitmapFont font = new BitmapFont();
		Label.LabelStyle textStyle = new Label.LabelStyle(font, Color.WHITE);
		Label instructions = new Label("Complete this puzzle to gain access to the " +
				"TOP SECRET surveillance room. You'll still have to find the entrance yourself!", textStyle);
		instructions.setX(310);
		instructions.setY(650);

		puzzleStage.addActor(instructions);

		// Retrieve the puzzle for the current game instance
		puzzle = game.gameSnapshot.puzzle;
		
		// Load the puzzle texture
		Texture duck = Assets.loadTexture("puzzle.png");
		
		// Create the 15 tiles as square regions of the texture
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// If this tile is the gap, continue
				if (puzzle[i][j] == -1) continue;
				
				// Get the number of this tile (0 = top left in unscrambled image, counting in rows)
				int number = puzzle[i][j];
				// Compute the x and y coordinates of the tile in the image
				int tileX = number % 4;
				int tileY = number / 4;
				
				// Generate the region from the texture
				Image img = new Image(new TextureRegion(duck, tileX*125, tileY*125, 125, 125));
				
				// Create the style, using the region
				ImageButtonStyle style = new ImageButtonStyle();
				style.up = img.getDrawable();
				style.down = img.getDrawable();
				style.over = img.getDrawable();
				
				ImageButton imgBtn = new ImageButton(style);
				
				// Position the tile on the screen, and add it to the stage
				imgBtn.setX(PUZZLE_X + j*125);
				imgBtn.setY(PUZZLE_Y + 375-i*125);
				puzzleStage.addActor(imgBtn);
				
				// Event handler for when a tile is clicked
				imgBtn.addListener(new ClickListener() {
		            @Override
		            public void clicked(InputEvent event, float x, float y) {
		            	// Move the tile
		            	move(number, imgBtn);
		            	
		            	// Test the win condition
		            	if (hasWon()) {
		            		// Enable the secret room trigger tile and return to the map
							MIRCH.me.rooms.get(0).enableSecretRoom();
		            		game.gameSnapshot.setState(GameState.map);
		            	}
		            }
				});
			}
		}
	}

	@Override
	public void show() {
		initStage();
		
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(puzzleStage);
        multiplexer.addProcessor(statusBar.stage);
        Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        puzzleStage.act();
        puzzleStage.draw();
        statusBar.render();
	}

	@Override
	public void resize(int width, int height) {
        puzzleStage.getViewport().update(width, height, false);
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
		puzzleStage.dispose();
		statusBar.dispose();
	}
	
	/**
	 * Move a given tile, if possible
	 * 
	 * @param tile The number of the tile in the unscrambled image
	 * @param btn The tile itself
	 * 
	 * @author Team JAAPAN
	 */
	private void move(int tile, ImageButton btn) {
		int tileX = 0, tileY = 0, gapX = 0, gapY = 0;
		// Find the tile and gap in the puzzle 
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (puzzle[j][i] == tile) {
					tileX = i;
					tileY = j;
				} else if (puzzle[j][i] == -1) {
					gapX = i;
					gapY = j;
				}
			}
		}
		
		// If the tile can be moved, move it
		if (tileX == gapX - 1 && tileY == gapY) {
			btn.setX(btn.getX() + PUZZLE_WIDTH/4);
			puzzle[gapY][gapX] = puzzle[tileY][tileX];
			puzzle[tileY][tileX] = -1;
		} else if (tileX == gapX + 1 && tileY == gapY) {
			btn.setX(btn.getX() - PUZZLE_WIDTH/4);
			puzzle[gapY][gapX] = puzzle[tileY][tileX];
			puzzle[tileY][tileX] = -1;
		} else if (tileX == gapX && tileY == gapY - 1) {
			btn.setY(btn.getY() - PUZZLE_HEIGHT/4);
			puzzle[gapY][gapX] = puzzle[tileY][tileX];
			puzzle[tileY][tileX] = -1;
		} else if (tileX == gapX && tileY == gapY + 1) {
			btn.setY(btn.getY() + PUZZLE_HEIGHT/4);
			puzzle[gapY][gapX] = puzzle[tileY][tileX];
			puzzle[tileY][tileX] = -1;
		}
	}
	
	/**
	 * @return true if the puzzle is complete, false otherwise
	 * 
	 * @author Team JAAPAN
	 */
	private boolean hasWon() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (puzzle[i][j] != i*4+j && i*4+j < 15) {
					return false;
				}
			}
		}
		return true;
	}

}
