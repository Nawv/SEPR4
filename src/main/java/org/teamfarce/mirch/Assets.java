package org.teamfarce.mirch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * This class manages all the assets.
 */
public class Assets {
    /**
     * These variables store the textures for the corresponding arrow directions
     */
    public static TextureRegion UP_ARROW;
    public static TextureRegion DOWN_ARROW;
    public static TextureRegion LEFT_ARROW;
    public static TextureRegion RIGHT_ARROW;

    /**
     * This it the animation for the clue glint to be drawn where a clue is hidden
     */
    public static Animation CLUE_GLINT;
    
    public static BitmapFont FONT30;
    public static GlyphLayout LAYOUT;

    /**
     * @param file - The file that contains the textures.
     * @return Returns the new texture.
     */
    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    /**
     * This loads all of the necessary textures
     */
    public static void load() {
        Texture arrows = loadTexture("arrows.png");
        LEFT_ARROW = new TextureRegion(arrows, 0, 0, 32, 32);
        RIGHT_ARROW = new TextureRegion(arrows, 32, 0, 32, 32);
        DOWN_ARROW = new TextureRegion(arrows, 0, 32, 32, 32);
        UP_ARROW = new TextureRegion(arrows, 32, 32, 32, 32);

        Texture glintFile = loadTexture("clues/glintSheet.png");
        TextureRegion[][] splitFrames = TextureRegion.split(glintFile, 32, 32);
        TextureRegion[] frames = splitFrames[0];

        CLUE_GLINT = new Animation(0.1f, frames);
        
        FONT30 = createFont("arial", 30);
        LAYOUT = new GlyphLayout();
    }

    /**
     * This method takes a direction and returns the corresponding arrow asset for that direction
     *
     * @param direction - The direction to fetch
     * @return (TextureRegion) the corresponding TextureRegion
     */
    public static TextureRegion getArrowDirection(String direction) {
        if (direction.equals("NORTH")) {
            return UP_ARROW;
        } else if (direction.equals("SOUTH")) {
            return DOWN_ARROW;
        } else if (direction.equals("WEST")) {
            return LEFT_ARROW;
        } else if (direction.equals("EAST")) {
            return RIGHT_ARROW;
        }

        return null;
    }
    
	/**
	* Creates a BitmapFont with the specified font and size.
	*
	* @param font The name of the font - must be stored as a .ttf file
	* under this name in the fonts directory
	* @param size The size of the font
	* @return The generated font
	* 
	* @author JAAPAN
	*/
	public static BitmapFont createFont(String font, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + font + ".ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		BitmapFont f = generator.generateFont(parameter);
		generator.dispose();
	
		return f;
	}
   

}
