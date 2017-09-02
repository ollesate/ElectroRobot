package olof.sjoholm.net;

import com.badlogic.gdx.graphics.Color;

public class Player {
    private final int id;
    private Color color;
    private String name;
    private boolean ready;

    public Player(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public int getId() {
        return id;
    }
}
