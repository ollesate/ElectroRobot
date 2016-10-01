package olof.sjoholm.Interfaces;

import olof.sjoholm.GameWorld.Actors.IGameboardActor;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.GameWorld.Utils.Rotation;

/**
 * Created by sjoholm on 25/09/16.
 */

public interface MovableToken extends IGameboardActor {

    void move(int steps, Direction direction, Callback finishedCallback);

    void rotate(Rotation rotation, Callback finishedCallback);

}
