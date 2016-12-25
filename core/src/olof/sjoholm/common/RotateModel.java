package olof.sjoholm.common;

import olof.sjoholm.GameWorld.Utils.Rotation;

public class RotateModel extends CardModel {
    public final Rotation rotation;

    public RotateModel(String type, int priority, Rotation rotation) {
        super(type, priority);
        this.rotation = rotation;
    }
}
