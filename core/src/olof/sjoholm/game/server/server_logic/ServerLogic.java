package olof.sjoholm.game.server.server_logic;

import com.badlogic.gdx.graphics.Color;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import olof.sjoholm.game.server.ServerGameScreen;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.net.ServerConnection;

public class ServerLogic {
    private final ServerGameScreen serverGameScreen;
    private final ServerConnection serverConnection;
    private final Set<Player> players = new HashSet<Player>();

    public interface PlayerApi {

        void sendMessage(Envelope envelope);
    }

    public ServerLogic(ServerGameScreen serverGameScreen, ServerConnection serverConnection) {
        this.serverGameScreen = serverGameScreen;
        this.serverConnection = serverConnection;
    }

    public void onPlayerConnected(final int id) {
        if (getPlayer(id) == null) {
            Player player = new Player(id, new PlayerApi() {
                @Override
                public void sendMessage(Envelope envelope) {
                    serverConnection.send(id, envelope);
                }
            });
            players.add(player);
            serverGameScreen.onPlayerConnected(player);
        }
    }

    public void onPlayerDisconnected(int id) {
        Player player = getPlayer(id);
        if (player != null) {
            players.remove(player);
            serverGameScreen.onPlayerDisconnected(player);
        }
    }

    public void onPlayerChangeName(int id, String name) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setName(name);
            serverGameScreen.updateAppearance(player);
        }
    }

    public void onPlayerChangeColor(int id, Color color) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setColor(color);
            serverGameScreen.updateAppearance(player);
        }
    }

    public void onPlayerReady(int id, boolean ready) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setReady(true);
            serverGameScreen.onPlayerReady(player, ready);

            boolean allReady = true;
            for (Player checkPlayer : players) {
                if (!checkPlayer.isReady()) {
                    allReady = false;
                }
            }
            if (allReady) {
                serverGameScreen.allPlayersReady();
            }
        }
    }

    public void onCardsReady(int id, boolean ready) {
        Player player = getPlayer(id);
        if (player != null) {
            serverGameScreen.onPlayerCardsReady(player, ready);
        }
    }

    public void onCardsReceived(int id, List<BoardAction> cards) {
        Player player = getPlayer(id);
        if (player != null) {
            player.setCards(cards);
            serverGameScreen.onPlayerCardsReceived(player, cards);
        }
    }

    private Player getPlayer(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }
}
