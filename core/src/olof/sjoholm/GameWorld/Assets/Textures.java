package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;


public class Textures {
    public static SpriteSheetRegion TILE;
    public static Texture spritesheet;
    public static Texture background;

    private Textures() {
    }

    public static void initialize() {
        spritesheet = new Texture("textures/spritesheet.png");
        
        TILE = new SpriteSheetRegion(spritesheet, 1, 0, 32, 32);

        background = new Texture("textures/background.png");
    }

    public static void dispose() {
        spritesheet.dispose();
        background.dispose();
    }
}
