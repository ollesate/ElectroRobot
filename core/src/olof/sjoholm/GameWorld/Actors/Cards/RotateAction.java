package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.MovableToken;

public class RotateAction implements Action {
    private Rotation rotation;

    public RotateAction(Rotation rotation) {
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

    @Override
    public String toString() {
        return super.toString() + ", rotation " + rotation;
    }
}
