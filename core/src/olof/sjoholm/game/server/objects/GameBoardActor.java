package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.Point;

import olof.sjoholm.game.server.logic.Direction;
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

    public Point getGameboardPosition() {
        return getStage().getGameBoard().getBoardPosition(this);
    }

    public int getId() {
        return id;
    }

    public void onAddedToStage() {

    }

    public <T extends Actor> T findChild(Class<T> clazz) {
        for (Actor actor : getChildren()) {
            if (clazz.isInstance(actor)) {
                return clazz.cast(actor);
            }
        }
        return null;
    }

    public boolean isPassable(Direction direction) {
        return true;
    }

    public boolean isExitable(Direction direction) {
        return true;
    }

    public boolean canMove(Direction direction) {
        Point nextPos = new Point(getGameboardPosition());
        nextPos.translate(direction.dirX, direction.dirY);

        if (getStage().getGameBoard().isOutOfBounds(nextPos)) {
            System.out.println("Out of bounds");
            return false;
        }

        for (GameBoardActor actor : getStage().getGameBoard().getActorsAt(getGameboardPosition())) {
            if (!actor.isExitable(direction)) {
                System.out.println("Not exitable " + actor.getClass().getSimpleName());
                return false;
            }
        }
        for (GameBoardActor actor : getStage().getGameBoard().getActorsAt(nextPos)) {
            if (!actor.isPassable(direction)) {
                System.out.println("Not passable " + actor.getClass().getSimpleName());
                return false;
            }
        }
        return true;
    }
}
