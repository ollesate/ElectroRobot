package olof.sjoholm.Interfaces;

/**
 * Created by sjoholm on 27/09/16.
 */
public interface ICard {

    void playCard(Callback finishedCallback);

    int getCardPriority();

    void delete();

}
