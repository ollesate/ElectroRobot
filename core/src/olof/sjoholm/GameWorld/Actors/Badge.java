package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import olof.sjoholm.Skins;

public class Badge extends Label {
    private final Actor target;

    public Badge(Actor target, CharSequence text) {
        super(text, Skins.DEFAULT);
        this.target = target;
        Pixmap labelColor = new Pixmap(1, 1, Pixmap.Format.RGB888);
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        getStyle().background = new Image(new Texture(labelColor)).getDrawable();
    }

    public Actor getTarget() {
        return target;
    }

    public void update() {
        setX(target.getX() + (target.getWidth() - getWidth()) / 2);
        setY(target.getY() + (target.getHeight() - getHeight()) / 2);
    }
}
