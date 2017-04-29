package org.teamfarce.mirch.screens;

import org.teamfarce.mirch.Assets;
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

public class PuzzleScreen extends AbstractScreen {
	
	private static final int PUZZLE_WIDTH = 500;
	private static final int PUZZLE_HEIGHT = 500;
	private static final int PUZZLE_X = Gdx.graphics.getWidth()/2 - PUZZLE_WIDTH/2;
	private static final int PUZZLE_Y = Gdx.graphics.getHeight()/2 - PUZZLE_HEIGHT/2;
	
	private Stage puzzleStage;
	private Skin uiSkin;
	private StatusBar statusBar;
	
	private int[][] puzzle = {
		{ 0,  1,  2,  3},
		{ 4,  5,  6,  7},
		{ 8,  9, 10, 11},
		{12, 13, 14, -1}
	};

	public PuzzleScreen(MIRCH game, Skin uiSkin) {
		super(game);

		this.uiSkin = uiSkin;
		statusBar = new StatusBar(game, uiSkin);
	}
	
	private void initStage() {
		puzzleStage = new Stage();
		
		Texture duck = Assets.loadTexture("puzzle.png");
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == 3 && j == 3) break;
				
				int number = i*4+j;
				
				Image img = new Image(new TextureRegion(duck, j*125, i*125, 125, 125));
				
				ImageButtonStyle style = new ImageButtonStyle();
				style.up = img.getDrawable();
				style.down = img.getDrawable();
				style.over = img.getDrawable();
				
				ImageButton imgBtn = new ImageButton(style);
				
				imgBtn.setX(PUZZLE_X + j*125);
				imgBtn.setY(PUZZLE_Y + 375-i*125);
				puzzleStage.addActor(imgBtn);
				
				imgBtn.addListener(new ClickListener() {
		            @Override
		            public void clicked(InputEvent event, float x, float y) {
		            	move(number, imgBtn);
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
        Gdx.gl.glClearColor(135, 206, 235, 1);
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
	
	private void move(int tile, ImageButton btn) {
		int tileX = 0, tileY = 0, gapX = 0, gapY = 0;
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

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.println("");
		}
	}

}
