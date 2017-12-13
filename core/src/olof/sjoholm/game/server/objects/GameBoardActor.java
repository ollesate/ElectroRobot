package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.utils.ui.Drawable;

public abstract class GameBoardActor extends Group {
    private static int idCounter;

    private final int id;
    private Drawable drawable;

    {
        id = idCounter++;
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            onAddedToStage();
        }
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (drawable != null) {
            drawable.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getOriginX(), getOriginY(), getRotation(), getColor());
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public GameStage getStage() {
        return (GameStage) super.getStage();
    }

    public int getId() {
        return id;
    }

    public void onAddedToStage() {

    }
}
