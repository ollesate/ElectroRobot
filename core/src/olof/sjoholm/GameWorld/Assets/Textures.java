package olof.sjoholm.GameWorld.Assets;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by sjoholm on 24/09/16.
 */

public class Textures {
    public static final Texture origTexture = new Texture("badlogic.jpg");
    public static final Texture one = new Texture("one.png");
    public static final Texture two = new Texture("two.png");
    public static final Texture three = new Texture("three.png");
    public static final Texture four = new Texture("four.png");
    public static final Texture five = new Texture("five.png");

    public static final Texture up = new Texture("up.png");
    public static final Texture down = new Texture("down.png");
    public static final Texture left = new Texture("left.png");
    public static final Texture right = new Texture("right.png");

    public Textures() {

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
    }
}
