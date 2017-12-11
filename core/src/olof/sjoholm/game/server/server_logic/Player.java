package olof.sjoholm.game.server.server_logic;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.net.Envelope;

public class Player {
    private final int id;
    private final ServerLogic.PlayerApi playerApi;

    private List<BoardAction> cards;
    private Color color;
    private String name;
    private boolean ready;

    public Player(int id, ServerLogic.PlayerApi playerApi) {
        this.id = id;
        this.playerApi = playerApi;
        color = Color.WHITE;
        name = "Player " + id;
    }

    public void updateDamage(int damage) {
        playerApi.sendMessage(new Envelope.UpdateDamage(damage));
    }

    public void sendCards(List<BoardAction> cards) {
        playerApi.sendMessage(new Envelope.SendCards(cards));
        this.cards = cards;
    }

    public List<BoardAction> getCards() {
        return cards;
    }

    public void startCountdown(float countdown) {
        playerApi.sendMessage(new Envelope.StartCountdown(countdown));
    }

    public void startGame() {
        playerApi.sendMessage(new Envelope.StartGame());
    }

    public void onCardActivated(BoardAction boardAction) {
        playerApi.sendMessage(new Envelope.OnCardActivated(boardAction));
    }

    public void onCardDeactivated(BoardAction boardAction) {
        playerApi.sendMessage(new Envelope.OnCardActivated(boardAction));
    }

    public void sendCardPhaseEnded() {
        playerApi.sendMessage(new Envelope.OnCardPhaseEnded());
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

    void setCards(List<BoardAction> cards) {
        this.cards = cards;
    }

    public boolean isReady() {
        return ready;
    }

    public int getId() {
        return id;
    }
}
