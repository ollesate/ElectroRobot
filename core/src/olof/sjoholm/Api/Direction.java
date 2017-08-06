package olof.sjoholm.Api;

public enum  Direction {
    LEFT(-1, 0, 180f),
    UP(0, 1, 90f),
    RIGHT(1, 0, 0f),
    DOWN(0, -1, 270f);

    public final float dirX;
    public final float dirY;
    public final float rotation;

    Direction(float dirX, float dirY, float rotation) {
        this.dirX = dirX;
        this.dirY = dirY;
        this.rotation = rotation;
    }
}
