package olof.sjoholm.common;

/**
 * Created by sjoholm on 23/12/16.
 */

public class CardModel {

    public CardModel(String type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    public int priority;
    public String type;
}
