package olof.sjoholm.Client.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import olof.sjoholm.Client.MyCardHand;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.Interfaces.ActionCard;

/**
 * Created by sjoholm on 23/12/16.
 */

public class HandStage extends Stage {

    public HandStage() {
        MyCardHand cardHand = new MyCardHand();
        addActor(cardHand);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {

    }

    public void dealCards(List<ActionCard> list) {

    }

    public void getCards(Player.OnCardsReceivedListener onCardsReceivedListener) {

    }
}
