package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.net.Player;
import olof.sjoholm.utils.ui.Drawable;

public class GameBoardActor extends Group {
    private Drawable drawable;

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

    public static class OnEndActionEvent extends Event {
        public final BoardAction boardAction;
        public final Player player;

        public OnEndActionEvent(Player player, BoardAction boardAction) {
            this.player = player;
            this.boardAction = boardAction;
        }
    }

    public static class OnStartActionEvent extends Event {
        public final BoardAction boardAction;
        public final Player player;

        public OnStartActionEvent(Player player, BoardAction boardAction) {
            this.player = player;
            this.boardAction = boardAction;
        }
    }
}
