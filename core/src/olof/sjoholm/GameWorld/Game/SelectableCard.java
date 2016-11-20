package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.graphics.Color;

import olof.sjoholm.GameWorld.Utils.Logger;

class SelectableCard {
    private static Color SELECTED_COLOR = Color.GREEN;
    private static Color UNSELECTED_COLOR = Color.WHITE;
    private CardView cardView;

    public SelectableCard() {
        cardView = new CardView();
        cardView.setBackgroundColor(UNSELECTED_COLOR);
    }

    public void select() {
        Logger.d("Select");
        cardView.setBackgroundColor(SELECTED_COLOR);
    }

    public void unselect() {
        cardView.setBackgroundColor(UNSELECTED_COLOR);
    }

    public CardView getView() {
        return cardView;
    }
}
