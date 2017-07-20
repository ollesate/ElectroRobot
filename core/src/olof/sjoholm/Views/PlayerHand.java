package olof.sjoholm.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Client.CardViewParser;
import olof.sjoholm.Client.SelectableCard;
import olof.sjoholm.GameWorld.Actors.CardView;
import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Utils.Logger;

/**
 * Created by olof on 2017-07-20.
 */
class PlayerHand extends Table {
    private List<SelectableCard> cards = new ArrayList<SelectableCard>();
    private static final float height = 125f;

    public PlayerHand() {
        left();
        top();
    }

    public void setCards(final List<CardModel> cardModels) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                for (CardModel cardModel : cardModels) {
                    Logger.d("Card " + cardModel.getClass().getName());
                    CardView cardView = CardViewParser.getInstance().modelToView(cardModel);
                    SelectableCard selectableCard = new SelectableCard(cardView);
                    cards.add(selectableCard);
                    cardView.setPadding(0f);
                    cardView.setMinSize(70f, height, 1f);
                    add(cardView).space(10f).top();
                }
            }
        });
    }

    public void select(int i) {

    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getPrefHeight() {
        return height;
    }

}
