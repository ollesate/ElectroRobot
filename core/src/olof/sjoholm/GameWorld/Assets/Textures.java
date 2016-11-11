package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;


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

    public static Texture rotate_left;
    public static Texture rotate_right;
    public static Texture rotate_uturn;

    public static Texture background;

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

        rotate_left = new Texture("rotate_left.png");
        rotate_right = new Texture("rotate_right.png");
        rotate_uturn = new Texture("uturn.png");

        spritesheet = new Texture("spritesheet.png");
        
        TILE = new SpriteSheetRegion(spritesheet, 1, 0);;

        background = new Texture("background.png");
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
        background.dispose();
    }
}
