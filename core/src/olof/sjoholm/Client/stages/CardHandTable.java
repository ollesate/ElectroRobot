package olof.sjoholm.Client.stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.Client.CardViewParser;
import olof.sjoholm.Client.SelectableCard;
import olof.sjoholm.GameWorld.Game.CardView;
import olof.sjoholm.common.CardModel;

class CardHandTable extends Table implements CardHandModel.OnHandChangedListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    private static final int GAP = 60;
    private final Array<SelectableCard> cards = new Array<SelectableCard>();
    private final CardHandModel cardHandModel;
    private SelectableCard selectedCard;

    public CardHandTable(CardHandModel cardHandModel) {
        this.cardHandModel = cardHandModel;
        cardHandModel.setOnHandChangedListener(this);
        setFillParent(true);
        addListener(getInputListener());
        addCards();
    }

    private InputListener getInputListener() {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 point = new Vector2();
                for (SelectableCard card : cards) {
                    card.getView().parentToLocalCoordinates(point.set(x, y));
                    if (card.getView().hit(point.x, point.y, false) != null) {
                        onCardClicked(card);
                        break;
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        };
    }

    private void onCardClicked(SelectableCard hitCard) {
        if (hitCard.isSelected()) {
            hitCard.unselect();
            selectedCard = null;
        } else if (selectedCard != null) {
            swap(hitCard, selectedCard);
            selectedCard.unselect();
            selectedCard = null;
        } else {
            hitCard.select();
            selectedCard = hitCard;
        }
    }

    private void swap(SelectableCard cardA, SelectableCard cardB) {
        int indexA = cards.indexOf(cardA, false);
        int indexB = cards.indexOf(cardB, false);
        cardHandModel.swap(indexA, indexB);
    }

    @Override
    public void onChanged() {
        clearChildren();
        cards.clear();
        addCards();
    }

    public void addCards() {
        for (CardModel cardModel : cardHandModel.getCardModels()) {
            CardView cardView = CardViewParser.getInstance().modelToView(cardModel);
            SelectableCard selectableCard = new SelectableCard(cardView);
            cards.add(selectableCard);
            add(cardView).width(300f).height(600f).padLeft(20).top();
        }
    }
}
