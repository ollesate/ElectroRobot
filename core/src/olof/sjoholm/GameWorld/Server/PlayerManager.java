package olof.sjoholm.GameWorld.Server;

import java.util.List;

public interface PlayerManager {

    List<Player> getPlayers();

    interface OnPlayerDisconnected {

    }
}

