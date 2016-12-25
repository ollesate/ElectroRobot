package olof.sjoholm.Client;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import olof.sjoholm.Client.stages.HandStage;
import olof.sjoholm.Client.stages.LobbyStage;
import olof.sjoholm.common.CardModel;

public class PlayerGame extends ScreenAdapter {
    private final HandStage handStage;
    private Stage currentStage;

    public PlayerGame() {
        handStage = new HandStage();
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
        currentStage = handStage;
    }

    public void dealCards(List<CardModel> list) {
        handStage.dealCards(list);
    }

    public List<CardModel> getCards() {
        return handStage.getCards();
    }
}
