package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.GameLogic.Callback;

/**
 * Created by sjoholm on 25/09/16.
 */

public interface MovableToken {

    void move(int steps, Direction direction, Callback finishedCallback);

}
