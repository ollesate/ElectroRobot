package olof.sjoholm.GameWorld.Actors.Cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.MovableToken;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

/**
 * Created by sjoholm on 01/10/16.
 */

public class RotateCard implements ICard {
    private int priority;

    private Rotation rotation;

    public RotateCard() {
        priority = (int) (Math.random() * 3 + 1);
        rotation = getRandomRotation();
    }

    private Rotation getRandomRotation() {
        return Rotation.values()[(int) (Math.random() * Rotation.values().length)];
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
    public int getPriority() {
        return priority;
    }

    @Override
    public String getType() {
        return RotateCard.class.getSimpleName();
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public String toString() {
        return super.toString() + ", rotation " + rotation;
    }
}
