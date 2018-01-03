package olof.sjoholm.game.shared;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.game.server.server_logic.Player;

public class DebugUtil {

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

        Player player = new Player(i, null);
//        player.setName(names.get(i));
//        player.setColor(colors.get(i));
        return player;
    }
}
