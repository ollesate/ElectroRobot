package olof.sjoholm.common;

import java.io.Serializable;

public class CardModel implements Serializable {

    public CardModel(String type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    public int priority;
    public String type;
}
