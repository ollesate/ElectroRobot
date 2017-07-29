package olof.sjoholm.Api;

public class Pair<Key, Value> {
    public final Key key;
    public final Value value;

    public Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }
}
