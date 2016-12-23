package olof.sjoholm.Client;

import com.badlogic.gdx.graphics.Color;

import olof.sjoholm.GameWorld.Game.CardView;
import olof.sjoholm.GameWorld.Utils.Logger;

public class SelectableCard {
    private static Color SELECTED_COLOR = Color.GREEN;
    private static Color UNSELECTED_COLOR = Color.WHITE;
    private CardView cardView;
    private boolean isSelected;

    public SelectableCard() {
        cardView = new CardView();
        cardView.setBackgroundColor(UNSELECTED_COLOR);
    }

    public void select() {
        isSelected = true;
        cardView.setBackgroundColor(SELECTED_COLOR);
    }

    public void unselect() {
        isSelected = false;
        cardView.setBackgroundColor(UNSELECTED_COLOR);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public CardView getView() {
        return cardView;
    }
}
