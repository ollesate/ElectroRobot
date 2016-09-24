package olof.sjoholm.GameLogic;

import olof.sjoholm.GameWorld.Actors.ICardView;

/**
 * Created by sjoholm on 27/09/16.
 */
public interface ICard extends ICardView {

    void playCard(Callback finishedCallback);

    int getCardPriority();

    void delete();

    @Override
    void select();

    @Override
    void unselect();
}
