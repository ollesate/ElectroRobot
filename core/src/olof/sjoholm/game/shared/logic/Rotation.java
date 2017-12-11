package olof.sjoholm.game.shared.logic;

public enum Rotation {

    LEFT(90f, 1),

    RIGHT(-90f, 1),

    UTURN(180f, 2);

    Rotation(float degrees, int duration) {
        this.degrees = degrees;
        this.duration = duration;
    }

    public float degrees;

    public int duration;

    public static Rotation random() {
        int rand = (int)(Math.random() * Rotation.values().length);
        return Rotation.values()[rand];
    }

    public static Rotation fromString(String string) {
        for (Rotation rotation : values()) {
            if (rotation.name().equalsIgnoreCase(string)) {
                return rotation;
            }
        }
        return null;
    }
}
