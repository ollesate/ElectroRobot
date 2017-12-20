package olof.sjoholm.game.shared.logic;

public enum Movement {

    FORWARD(0f),

    CRAB_LEFT(-90f),

    CRAB_RIGHT(90f),

    BACKWARDS(180f);

    private final float rotation;

    Movement(float rotation) {
        this.rotation = rotation;
    }

    public static Movement random() {
        int rand = (int)(Math.random() * Movement.values().length);
        return Movement.values()[rand];
    }

    public static Movement fromString(String string) {
        for (Movement movement : Movement.values()) {
            if (movement.name().equalsIgnoreCase(string)) {
                return movement;
            }
        }
        return null;
    }

    public float getRotation() {
        return rotation;
    }
}
