package olof.sjoholm.game.shared.logic.cards;


import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.game.shared.objects.PlayerToken;

public class Shoot extends BoardAction {

    @Override
    public Action getAction(PlayerToken playerToken) {
        return playerToken.getShootAction();
    }

    @Override
    public String getText() {
        return "Shoot";
    }
}
