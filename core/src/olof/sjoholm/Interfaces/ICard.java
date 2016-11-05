package olof.sjoholm.Interfaces;

public interface ICard {

    // Used for making an action on server side
    void apply(MovableToken movableToken, Callback finishedCallback);

    // Used for drawing a number on client side and for sorting on server side
    int getPriority();

    // What kind of card this is, use this for drawing on client side
    String getType();

}
