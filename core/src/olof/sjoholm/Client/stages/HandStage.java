package olof.sjoholm.Client.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.common.CardModel;


public class HandStage extends Stage {
    private final CardHandTable cardHandTable;
    private final CardHandModel cardHandModel;

    public HandStage() {
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        setViewport(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Gdx.input.setInputProcessor(this);

        cardHandModel = new CardHandModel();
        cardHandTable = new CardHandTable(cardHandModel);
        addActor(cardHandTable);
    }

    public void dealCards(List<CardModel> list) {
        cardHandModel.clear();
        cardHandModel.addCardModels(list);
    }

    public List<CardModel> getCards() {
        return cardHandModel.getCardModels();
    }

    private static class CardHandController {

        public CardHandController() {

        }

        public void addCard(CardModel cardModel) {

        }

    }

}
