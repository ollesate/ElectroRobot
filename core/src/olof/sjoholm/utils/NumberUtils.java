package olof.sjoholm.utils;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static int toInt(String number, int fallback) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
