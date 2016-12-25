package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.graphics.Texture;

import olof.sjoholm.GameWorld.Actors.Cards.BaseCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.ActionCard;

public class RotateCardActor extends BaseCard {
    private Texture texture;

    public RotateCardActor(ActionCard card) {
        super(card);
        Rotation rotation = ((RotateCard) card).getRotation();

        switch (rotation) {
            case LEFT:
                texture = Textures.rotate_left;
                break;
            case RIGHT:
                texture = Textures.rotate_right;
                break;
            case UTURN:
                texture = Textures.rotate_uturn;
                break;
        }
        build();
    }

    @Override
    protected Texture getActionTexture() {
        return texture;
    }
}
