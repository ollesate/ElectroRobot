package olof.sjoholm.GameWorld.Utils;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameLogic.ICard;
import olof.sjoholm.GameLogic.Player;
import olof.sjoholm.GameWorld.Actors.Direction;
import olof.sjoholm.GameWorld.Assets.Textures;

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

    public static List<ICard> popTopCards(List<Player> players) {
        List<ICard> cards = new ArrayList<ICard>();
        for (Player player : players) {
            if (player.hasCard()) {
                cards.add(player.popTopCard());
            }
        }
        return cards;
    }

    public static void select(List<ICard> cards) {
        for (ICard card : cards) {
            card.select();
        }
    }

    public static void unselect(List<ICard> cards) {
        for (ICard card : cards) {
            card.unselect();
        }
    }
}
