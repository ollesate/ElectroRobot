package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.shared.Damageable;
import olof.sjoholm.utils.ui.Drawable;
import olof.sjoholm.utils.ui.TextureDrawable;

public class HealthBarActor extends Actor {
    private final Drawable background;
    private final Drawable foreground;
    private final Color backgroundColor;
    private final Color foregroundColor;

    private Damageable target;
    private boolean smartShow;

    public HealthBarActor() {
        this(false);
    }

    public HealthBarActor(boolean smartShow) {
        this.smartShow = smartShow;

        background = new TextureDrawable(Textures.BACKGROUND);
        foreground = new TextureDrawable(Textures.BACKGROUND);
        backgroundColor = new Color(Color.RED);
        foregroundColor = new Color(Color.GREEN);
    }

    @Override
    protected void setParent(Group parent) {
        super.setParent(parent);
        target = (Damageable) parent;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (target == null) {
            return;
        }

        if (smartShow && target.getCurrentHealth() == target.getMaxHealth()) {
            // Don't show health when full
            return;
        }

        float percentage = target.getCurrentHealth() / ((float) target.getMaxHealth());
        float foregroundWidth = percentage * getWidth();

        background.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getOriginX(), getOriginY(), 0f, backgroundColor);
        foreground.draw(batch, parentAlpha, getX(), getY(), foregroundWidth, getHeight(), getScaleX(), getScaleY(), getOriginX(), getOriginY(), 0f, foregroundColor);
    }
}
