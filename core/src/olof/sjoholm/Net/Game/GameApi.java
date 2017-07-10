package olof.sjoholm.Net.Game;

import olof.sjoholm.Net.Envelope;

public class GameApi {
    private GameServer gameServer;

    public GameApi(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void startGame() {
        gameServer.broadcast(new Envelope.StartGame());
    }

}
