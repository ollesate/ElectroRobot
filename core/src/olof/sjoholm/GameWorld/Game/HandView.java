package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Constants;

public class HandView extends Table {
    private static final float HORIZONTAL_SPACING = 45f;
    private static final float VERTICAL_PADDING = HORIZONTAL_SPACING * 1.5f;

    private Texture texture;

    public HandView() {
        texture = Textures.background;
        initialize();
    }

    private void initialize() {
        setDebug(true);
        padRight(HORIZONTAL_SPACING);
        padBottom(VERTICAL_PADDING);
        padTop(VERTICAL_PADDING);
        setWidth(Constants.WORLD_WIDTH);
        setHeight(Constants.WORLD_HEIGHT);
        left();
        top();
    }

    public void clearCards() {
        super.reset();
        initialize();
    }

    public void addCardView(CardView card) {
        addColumn(card);
    }

    private void addColumn(CardView card) {
        add(card).width(300f).height(600f).padLeft(20).top();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }
}
