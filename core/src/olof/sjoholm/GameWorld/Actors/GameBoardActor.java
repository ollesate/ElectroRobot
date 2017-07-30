package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Interfaces.Drawable;
import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Views.GameStage;

public class GameBoardActor extends Actor {
    private Drawable drawable;

    {
        // TODO: Usch
        setSize(Constants.STEP_SIZE, Constants.STEP_SIZE);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (drawable != null) {
            drawable.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getOriginX(), getOriginY(), getRotation(), getColor());
        }
    }

    @Override
    public GameStage getStage() {
        return (GameStage) super.getStage();
    }

    public static class OnEndActionEvent extends Event {
        public final PlayerAction playerAction;

        public OnEndActionEvent(PlayerAction playerAction) {
            this.playerAction = playerAction;
        }
    }

    public static class OnStartActionEvent extends Event {
        public final PlayerAction playerAction;

        public OnStartActionEvent(PlayerAction playerAction) {
            this.playerAction = playerAction;;
        }
    }
}
