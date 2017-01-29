package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Assets.RegionDrawable;

public class Tile extends GameBoardActor {

    public Tile(int x, int y) {
        setBoardX(x);
        setBoardY(y);
        updateToBoardPosition();
        setDrawable(new RegionDrawable(this, Textures.TILE));
    }
}
