package olof.sjoholm.Client;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import olof.sjoholm.Client.stages.HandStage;
import olof.sjoholm.Client.stages.LobbyStage;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.Interfaces.ActionCard;

/**
 * Created by sjoholm on 23/12/16.
 */

public class PlayerGame extends ScreenAdapter {
    private Stage currentStage;

    public PlayerGame() {
        currentStage = new LobbyStage();
    }

    @Override
    public void render(float delta) {
        if (currentStage != null) {
            currentStage.act(delta);
            currentStage.draw();
        }
    }

    public void startGame() {
        currentStage = new HandStage();
    }

    public void dealCards(List<ActionCard> list) {
        ((HandStage) currentStage).dealCards(list);
    }

    public void getCards(Player.OnCardsReceivedListener onCardsReceivedListener) {
        ((HandStage) currentStage).getCards(onCardsReceivedListener);
    }
}
