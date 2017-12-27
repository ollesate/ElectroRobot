package olof.sjoholm.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import olof.sjoholm.utils.ui.SpriteSheetRegion;

public class Textures {
    public static SpriteSheetRegion TILE;
    public static TextureRegion MISSILE;
    public static Texture SPRITE_SHEET;
    public static Texture BACKGROUND;
    public static Texture PARTICLE;
    public static Texture CONVEYOR_BELT_UP;
    public static Texture CONVEYOR_BELT_CURVE_RIGHT;
    public static Texture CONVEYOR_BELT_CURVE_LEFT;
    public static Texture LASER_UP;
    public static Texture LASER_RIGHT;
    public static Texture LASER_DOWN;
    public static Texture LASER_LEFT;

    private Textures() {
    }

    public static void initialize() {
        SPRITE_SHEET = new Texture("textures/spritesheet.png");
        
        TILE = new SpriteSheetRegion(SPRITE_SHEET, 1, 0, 32, 32);
        MISSILE = new TextureRegion(SPRITE_SHEET, 9, 4 * 32 + 13, 14, 5);

        BACKGROUND = new Texture("textures/background.png");
        PARTICLE = new Texture("textures/particle_texture.png");

        CONVEYOR_BELT_UP = new Texture("textures/up.png");
        CONVEYOR_BELT_CURVE_LEFT = new Texture("textures/left.png");
        CONVEYOR_BELT_CURVE_RIGHT = new Texture("textures/right.png");

        LASER_UP = new Texture("textures/laser_up.png");
        LASER_RIGHT = new Texture("textures/laser_right.png");
        LASER_DOWN = new Texture("textures/laser_down.png");
        LASER_LEFT = new Texture("textures/laser_left.png");
    }

    public static void dispose() {
        SPRITE_SHEET.dispose();
        BACKGROUND.dispose();
        PARTICLE.dispose();
        CONVEYOR_BELT_UP.dispose();
        CONVEYOR_BELT_CURVE_LEFT.dispose();
        CONVEYOR_BELT_CURVE_RIGHT.dispose();
        LASER_UP.dispose();
        LASER_RIGHT.dispose();
        LASER_DOWN.dispose();
        LASER_LEFT.dispose();
    }
}
