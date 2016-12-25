package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.MovableToken;
import olof.sjoholm.common.Card;
import olof.sjoholm.common.CardModel;

public class RotateCard extends Card {
    private Rotation rotation;

    public RotateCard(CardModel cardModel, Rotation rotation) {
        super(cardModel);
        this.rotation = rotation;
    }

    @Override
    public void apply(MovableToken movableToken, final Callback finishedCallback) {
        movableToken.rotate(rotation, new Callback() {
            @Override
            public void callback() {
                finishedCallback.callback();
            }
        });
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return super.toString() + ", rotation " + rotation;
    }
}
