package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;

import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Interfaces.Drawable;

public class GameBoardActor extends Actor {
    // TODO: Why not keep this a constant and zoom out instead? :(
    protected int stepSize;
    private int boardX;
    private int boardY;
    private Drawable drawable;

    {
        // TODO: Usch
        setSize(Constants.STEP_SIZE, Constants.STEP_SIZE);
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }

    public void setBoardX(int x) {
        boardX = x;
    }

    public void setBoardY(int y) {
        boardY = y;
    }

    // TODO: Why?
    public void updateStepSize(int stepSize) {
        this.stepSize = stepSize;
        setSize(stepSize, stepSize);
        updateToBoardPosition();
    }

    protected void updateToBoardPosition() {
        setX(boardX * stepSize);
        setY(boardY * stepSize);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (drawable != null) {
            drawable.draw(batch, parentAlpha);
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
