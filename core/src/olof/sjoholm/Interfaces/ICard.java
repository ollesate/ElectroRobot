package olof.sjoholm.Interfaces;

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
