package olof.sjoholm.Interfaces;

import olof.sjoholm.GameWorld.Actors.IGameboardActor;
import olof.sjoholm.GameWorld.Utils.Direction;

/**
 * Created by sjoholm on 25/09/16.
 */

public interface MovableToken extends IGameboardActor {

    void move(int steps, Direction direction, Callback finishedCallback);

}
