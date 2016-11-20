package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import olof.sjoholm.GameWorld.Utils.Constants;

public class HandView extends Table {
    private static final float HORIZONTAL_SPACING = 45f;
    private static final float VERTICAL_PADDING = HORIZONTAL_SPACING * 1.5f;

    public HandView() {
        setDebug(true);
        padRight(HORIZONTAL_SPACING);
        padBottom(VERTICAL_PADDING);
        padTop(VERTICAL_PADDING);
        setWidth(Constants.WORLD_WIDTH);
        setHeight(Constants.WORLD_HEIGHT);
        left();
        top();
    }

    public void addCard(CardView card) {
        addColumn(card);
    }

    private void addColumn(CardView card) {
        add(card).width(300f).height(600f).growY().padLeft(20).top();
    }
}
