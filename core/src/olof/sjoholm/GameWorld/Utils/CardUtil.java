package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameLogic.ConcretePlayer;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Interfaces.ICard;

/**
 * Created by sjoholm on 26/09/16.
 */

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

    public static List<ICard> popTopCards(List<ConcretePlayer> players) {
        List<ICard> cards = new ArrayList<ICard>();
        for (ConcretePlayer player : players) {
            if (player.hasCard()) {
                cards.add(player.popTopCard());
            }
        }
        return cards;
    }

    public static List<ICard> createRandomCards(int count) {
        List<ICard> cards = new ArrayList<ICard>();
        for (int i = 0; i < count; i++) {
            cards.add(createRandomCard());
        }
        return cards;
    }

    private static ICard createRandomCard() {
        ICard card;
        if (Math.random() > 0.25f) {
            card = new MoveCard();
        } else {
            card = new RotateCard();
        }
        return card;
    }

}
