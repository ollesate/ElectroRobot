package olof.sjoholm.game.server.objects;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.utils.ui.RegionDrawable;

public class Tile extends olof.sjoholm.game.server.objects.GameBoardActor {

    public Tile() {
        setDrawable(new RegionDrawable(Textures.TILE));
    }
}
