package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Interfaces.Drawable;
import olof.sjoholm.Utils.Logger;

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
        if (drawable != null) {
            drawable.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getOriginX(), getOriginY(), getRotation(), getColor());
        }
    }

    public void startAction() {
        fire(new OnStartActionEvent(this));
    }

    public void endAction() {
        fire(new OnEndActionEvent(this));
    }

    public class OnEndActionEvent extends Event {
        public GameBoardActor gameBoardActor;

        public OnEndActionEvent(GameBoardActor gameBoardActor) {
            this.gameBoardActor = gameBoardActor;
        }
    }

    public class OnStartActionEvent extends Event {
        public GameBoardActor gameBoardActor;

        public OnStartActionEvent(GameBoardActor gameBoardActor) {
            this.gameBoardActor = gameBoardActor;
        }
    }
}
