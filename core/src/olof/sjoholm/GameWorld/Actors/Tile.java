package olof.sjoholm.GameWorld.Actors;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Assets.RegionDrawable;
import olof.sjoholm.Utils.Constants;

public class Tile extends GameBoardActor {

    public Tile() {
        setDrawable(new RegionDrawable(Textures.TILE));
    }
}
