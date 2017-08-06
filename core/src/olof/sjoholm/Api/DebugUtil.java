package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.PlayerAction;
import olof.sjoholm.Net.Server.Player;
import olof.sjoholm.Utils.Constants;

/**
 * Created by olof on 2017-08-06.
 */

public class DebugUtil {

    public static Turns generateTurns(List<Player> players) {
        Turns turns = new Turns(Constants.CARDS_TO_PLAY);

        for (Player player : players) {
            List<BoardAction> cards = CardGenerator.generateList(Constants.CARDS_TO_PLAY);
            for (int i = 0; i < cards.size(); i++) {
                turns.addToTurn(i, new PlayerAction(player, cards.get(i)));
            }
        }
        return turns;
    }

    public static Player getPlayer(int i) {
        List<String> names = new ArrayList<String>();
        names.add("Olof");
        names.add("Anna");
        names.add("Stina");
        names.add("Eva");

        List<Color> colors = new ArrayList<Color>();
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLUE);

        Player player = new Player(i);
        player.setName(names.get(i));
        player.setColor(colors.get(i));
        return player;
    }
}
