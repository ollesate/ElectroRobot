package olof.sjoholm.game.player.game;

import com.badlogic.gdx.Gdx;

import olof.sjoholm.game.player.PlayerScreen;
import olof.sjoholm.game.player.game.HandStage;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.utils.Logger;

public class PlayerGameScreen extends PlayerScreen {
    private final HandStage handStage;
    private int damage;

    public PlayerGameScreen() {
        super();
        handStage = new HandStage();
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(handStage);
    }

    @Override
    public void resize(int width, int height) {
        Logger.d("Resize " + width + " " + height);
        handStage.resize(width, height);
    }

    @Override
    public void render(float delta) {
        handStage.act(delta);
        handStage.draw();
    }

    @Override
    public void onConnected() {
        Logger.d("Connected");
        // No behaviour right now
    }

    @Override
    public void onConnectionFailed(String reason) {
        Logger.d("On connection failed");
        // Maybe keep it silent and just try reconnect once in a while.
    }

    @Override
    public void onDisconnected() {
        Logger.d("Disconnected");
        // No behaviour right now
    }

    @Override
    public void onMessage(Envelope envelope) {
        Logger.d("Player receive message " + envelope);
        if (envelope instanceof Envelope.SendCards) {
            // Received cards.
            handStage.update(((Envelope.SendCards) envelope).cards);
            handStage.blockCards(damage);
        } else if (envelope instanceof Envelope.StartCountdown) {
            // Start a countdown.
            float coolDown = ((Envelope.StartCountdown) envelope).time;
            handStage.startCountdown(coolDown);
        } else if (envelope instanceof Envelope.OnCardActivated) {
            // Card activated.
            handStage.select(((Envelope.OnCardActivated) envelope).boardAction);
        } else if (envelope instanceof Envelope.OnCardDeactivated) {
            // Card deactivated.
            handStage.deselect(((Envelope.OnCardDeactivated) envelope).boardAction);
        } else if (envelope instanceof Envelope.OnCardPhaseEnded) {
            // Card phase ended
            send(new Envelope.SendCards(handStage.getSortedCards()));
            handStage.lockCards();
        } else if (envelope instanceof Envelope.UpdateDamage) {
            // Update damage
            damage = ((Envelope.UpdateDamage) envelope).damage;
            handStage.blockCards(damage);
        }
    }
}
