package olof.sjoholm.game.shared.logic.cards;


import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class Rotate extends BoardAction {
    private Rotation rotation;

    public Rotate(Rotation rotation) {
        this.rotation = rotation;
    }

    @Override
    public Action getAction(PlayerToken playerToken) {
        return playerToken.rotate(rotation);
    }

    @Override
    public String getText() {
        if (Rotation.UTURN.equals(rotation)) {
            return "Perform U-TURN";
        }
        return "Rotate " + rotation;
    }
}
