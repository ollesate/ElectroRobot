package olof.sjoholm.game.server.objects;

import olof.sjoholm.utils.ui.objects.DrawableActor;
import olof.sjoholm.utils.ui.TextureDrawable;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.server_logic.Player;

public class SpawnPoint extends DrawableActor {
    private final int boardX;
    private final int boardY;
    private Player player;

    public SpawnPoint(int tileSize, int x, int y) {
        super(new TextureDrawable(Textures.BACKGROUND));
        boardX = x;
        boardY = y;
        setWidth(tileSize / 2);
        setHeight(tileSize / 2);
    }

    public void setOwner(Player player) {
        this.player = player;
    }

    public Player getOwner() {
        return player;
    }

    public int getBoardX() {
        return boardX;
    }

    public int getBoardY() {
        return boardY;
    }
}
