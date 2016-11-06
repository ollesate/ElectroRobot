package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.Interfaces.Drawable;

/**
 * Created by sjoholm on 01/10/16.
 */

public class GameBoardActor extends Actor implements IGameboardActor {
    private int boardX;
    private int boardY;
    private Drawable drawable;

    @Override
    public int getBoardX() {
        return boardX;
    }

    @Override
    public int getBoardY() {
        return boardY;
    }

    @Override
    public void setBoardX(int x) {
        boardX = x;
    }

    protected void updateToBoardPosition() {
        setX(boardX * Constants.STEP_SIZE);
        setY(boardY * Constants.STEP_SIZE);
    }

    @Override
    public void setBoardY(int y) {
        boardY = y;
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
}
