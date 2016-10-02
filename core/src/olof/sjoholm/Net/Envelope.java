package olof.sjoholm.Net;

import java.io.Serializable;

/**
 * Created by sjoholm on 02/10/16.
 */

public class Envelope implements Serializable {
    private final Object contents;
    private final String type;

    public Envelope(final Object contents, String type) {
        this.contents = contents;
        this.type = type;
    }

    public <T> T getContents(Class<T> clazz) {
        return (T) contents;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Envelope-> Type: " + type + ", Content: " + contents.toString();
    }

    public static class Message extends Envelope {

        public Message(String contents) {
            super(contents, "String");
        }
    }
}
