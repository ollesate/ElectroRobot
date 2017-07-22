package olof.sjoholm.Net.Server;

import com.badlogic.gdx.graphics.Color;

public class Player {
    public final int id;
    public Color color;
    public String name;

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
}
