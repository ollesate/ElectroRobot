package olof.sjoholm.game.shared.logic.cards;

import com.badlogic.gdx.scenes.scene2d.Action;

import java.io.Serializable;

import olof.sjoholm.game.shared.objects.PlayerToken;

public abstract class BoardAction implements Serializable {
    private int id;

    public abstract Action getAction(PlayerToken playerToken);

    public abstract String getText();

    public void setId(int id) {
        // TODO: make package protected.
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
