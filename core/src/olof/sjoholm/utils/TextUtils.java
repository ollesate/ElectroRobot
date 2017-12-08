package olof.sjoholm.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class TextUtils {

    private TextUtils() {
    }

    public static String join(Object[] objects, String separator) {
        return join(Arrays.asList(objects), separator);
    }

    public static String join(Collection<Object> objects, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }
}
