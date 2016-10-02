package olof.sjoholm.GameWorld.Actors.Cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Interfaces.ICard;
import olof.sjoholm.Interfaces.MovableToken;

/**
 * Created by sjoholm on 01/10/16.
 */

public class RotateCard extends olof.sjoholm.GameWorld.Actors.Cards.BaseCard implements ICard {
    private int priority;
    private MovableToken movableToken;
    private Rotation rotation;

    public RotateCard(MovableToken movableToken) {
        this.movableToken = movableToken;
        priority = (int) (Math.random() * 3 + 1);
        rotation = getRandomRotation();
        addActors();
    }

    private Rotation getRandomRotation() {
        return Rotation.values()[(int) (Math.random() * Rotation.values().length)];
    }

    @Override
    public void playCard(final Callback finishedCallback) {
        select();
        movableToken.rotate(rotation, new Callback() {
            @Override
            public void callback() {
                unselect();
                finishedCallback.callback();
            }
        });
    }

    @Override
    public int getCardPriority() {
        return priority;
    }

    @Override
    public void delete() {
        addAction(Actions.removeActor());
    }

    @Override
    Texture getActionTexture() {
        switch (rotation) {
            case LEFT:
                return Textures.rotate_left;
            case RIGHT:
                return Textures.rotate_right;
            case UTURN:
                return Textures.rotate_uturn;
        }
        return null;
    }
}
