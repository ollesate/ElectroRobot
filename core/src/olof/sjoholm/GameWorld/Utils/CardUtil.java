package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameLogic.ConcretePlayer;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Interfaces.ActionCard;
import olof.sjoholm.common.CardFactory;


public class CardUtil {
    
    public static Texture getDirectionTexture(Direction direction) {
        switch (direction) {
            case DOWN:
                return Textures.down;
            case LEFT:
                return Textures.left;
            case RIGHT:
                return Textures.right;
            case UP:
                return Textures.up;
        }
        return null;
    }
    
    public static Texture getNumberTexture(int number) {
        switch (number) {
            case 1:
                return Textures.one;
            case 2:
                return Textures.two;
            case 3:
                return Textures.three;
            case 4:
                return Textures.four;
            case 5:
                return Textures.five;
                
        }
        return null;
    }

    public static List<ActionCard> popTopCards(List<ConcretePlayer> players) {
        List<ActionCard> cards = new ArrayList<ActionCard>();
        for (ConcretePlayer player : players) {
            if (player.hasCard()) {
                cards.add(player.popTopCard());
            }
        }
        return cards;
    }

    public static List<ActionCard> createRandomCards(int count) {
        List<ActionCard> cards = new ArrayList<ActionCard>();
        for (int i = 0; i < count; i++) {
            cards.add(createRandomCard());
        }
        return cards;
    }

    private static ActionCard createRandomCard() {
        return CardFactory.getInstance().createRandom();
    }

}
