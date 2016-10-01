package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sjoholm on 24/09/16.
 */

public class Textures {
    public static Texture origTexture;
    public static Texture one;
    public static Texture two;
    public static Texture three;
    public static Texture four;
    public static Texture five;

    public static Texture up;
    public static Texture down;
    public static Texture left;
    public static Texture right;

    public static SpriteSheetRegion TILE;
    public static Texture spritesheet;

    Textures() {

    }

    public static void initialize() {
        origTexture = new Texture("badlogic.jpg");
        one = new Texture("one.png");
        two = new Texture("two.png");
        three = new Texture("three.png");
        four = new Texture("four.png");
        five = new Texture("five.png");

        up = new Texture("up.png");
        down = new Texture("down.png");
        left = new Texture("left.png");
        right = new Texture("right.png");

        spritesheet = new Texture("spritesheet.png");
        
        TILE = new SpriteSheetRegion(spritesheet, 1, 0);;
    }

    public static void dispose() {
        origTexture.dispose();
        one.dispose();
        two.dispose();
        three.dispose();
        four.dispose();
        five.dispose();
        up.dispose();
        down.dispose();
        left.dispose();
        right.dispose();
        spritesheet.dispose();
    }
}
