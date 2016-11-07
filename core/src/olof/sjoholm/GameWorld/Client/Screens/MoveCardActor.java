package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.graphics.Texture;

import olof.sjoholm.GameWorld.Actors.Cards.BaseCard;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.Interfaces.ICard;

public class MoveCardActor extends BaseCard {
    private Texture texture;

    public MoveCardActor(ICard card) {
        super(card);
        Direction direction = ((MoveCard) card).getDirection();
        texture = CardUtil.getDirectionTexture(direction);
        build();
    }

    @Override
    public Texture getActionTexture() {
        return texture;
    }
}
