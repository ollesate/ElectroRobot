package olof.sjoholm.Interfaces;

public interface ActionCard {

    void apply(MovableToken movableToken, Callback finishedCallback);

    int getPriority();

    String getType();
}
