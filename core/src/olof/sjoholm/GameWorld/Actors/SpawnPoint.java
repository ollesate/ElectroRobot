package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.Client.stages.DrawableActor;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Net.Server.Player;

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
