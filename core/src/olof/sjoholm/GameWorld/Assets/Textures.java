package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Textures {
    public static SpriteSheetRegion TILE;
    public static TextureRegion MISSILE;
    public static Texture SPRITE_SHEET;
    public static Texture BACKGROUND;
    public static Texture PARTICLE;

    private Textures() {
    }

    public static void initialize() {
        SPRITE_SHEET = new Texture("textures/spritesheet.png");
        
        TILE = new SpriteSheetRegion(SPRITE_SHEET, 1, 0, 32, 32);
        MISSILE = new TextureRegion(SPRITE_SHEET, 9, 4 * 32 + 13, 14, 5);

        BACKGROUND = new Texture("textures/background.png");
        PARTICLE = new Texture("textures/particle_texture.png");
    }

    public static void dispose() {
        SPRITE_SHEET.dispose();
        BACKGROUND.dispose();
        PARTICLE.dispose();
    }
}
