package olof.sjoholm.Interfaces;

import java.util.List;

import olof.sjoholm.Net.Envelope;

public interface IServer {

    void broadcast(Envelope envelope);

    List<IPlayerApi> getConnectedPlayers();
}