package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.utils.ui.TextureDrawable;

public class ConveyorBelt extends GameBoardActor {

    public ConveyorBelt() {
        setDrawable(new TextureDrawable(Textures.BACKGROUND));
        setColor(Color.BROWN);
    }

    @Override
    public void onAddedToStage() {

    }

    public Action getAction() {
        return Actions.delay(1f);
    }
}
