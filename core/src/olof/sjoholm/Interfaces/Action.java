package olof.sjoholm.Interfaces;

public interface Action {

    void apply(MovableToken movableToken, Callback finishedCallback);
}
