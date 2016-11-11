package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import olof.sjoholm.GameWorld.Assets.Textures;

public class Backgrounds {

    public static SpriteDrawable create(Color color) {
        Sprite sprite = new Sprite(Textures.background);
        sprite.setColor(color);
        return new SpriteDrawable(sprite);
    }

}
