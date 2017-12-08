package olof.sjoholm.game.server.server_logic;

import com.badlogic.gdx.graphics.Color;

import olof.sjoholm.net.Envelope;

public class Player {
    private final int id;
    private final ServerLogic.PlayerApi playerApi;

    private Color color;
    private String name;
    private boolean ready;

    public Player(int id, ServerLogic.PlayerApi playerApi) {
        this.id = id;
        this.playerApi = playerApi;
        color = Color.WHITE;
        name = "Player " + id;
    }

    public void send(Envelope envelope) {
        playerApi.sendMessage(envelope);
    }

    public Color getColor() {
        return color;
    }

    void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public int getId() {
        return id;
    }
}
