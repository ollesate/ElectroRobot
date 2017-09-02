package olof.sjoholm.game.objects;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.utils.graphic.RegionDrawable;

public class Tile extends olof.sjoholm.game.objects.GameBoardActor {

    public Tile() {
        setDrawable(new RegionDrawable(Textures.TILE));
    }
}
