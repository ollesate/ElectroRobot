package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;

import java.util.List;

import olof.sjoholm.Client.stages.HandStage;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;

public class PlayerGameScreen extends PlayerScreen {
    private final HandStage handStage;

    public PlayerGameScreen() {
        super();
        handStage = new HandStage();
        final List<BoardAction> list = CardGenerator.generateList(Constants.CARDS_TO_DEAL);
        for (BoardAction boardAction : list) {
            handStage.addCard(boardAction);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                BoardAction prev = null;
                for (BoardAction boardAction : list) {

                    if (prev != null) {
                        handStage.deselect(prev);
                    }
                    prev = boardAction;
                    handStage.select(boardAction);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {

                    }
                }
                handStage.deselect(prev);

            }
        }).start();
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
        handStage.lockCards();
        handStage.blockCards(3);
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
            for (BoardAction card : ((Envelope.SendCards) envelope).cards) {
                handStage.addCard(card);
            }
            handStage.update();
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
            send(new Envelope.SendCards(handStage.getSortedCards()));
            handStage.lockCards();
        }
    }
}
