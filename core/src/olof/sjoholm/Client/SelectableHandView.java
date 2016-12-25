package olof.sjoholm.Client;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

import java.util.List;

import olof.sjoholm.Interfaces.ActionCard;

public class SelectableHandView extends olof.sjoholm.Client.HandView {
    private final Array<olof.sjoholm.Client.SelectableCard> cards = new Array<olof.sjoholm.Client.SelectableCard>();
    private olof.sjoholm.Client.SelectableCard selectedCard;

    public SelectableHandView() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vector2 point = new Vector2();
                for (olof.sjoholm.Client.SelectableCard card : cards) {
                    card.getView().parentToLocalCoordinates(point.set(x, y));
                    if (card.getView().hit(point.x, point.y, false) != null) {
                        onCardClicked(card);
                        break;
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

    }

    private void onCardClicked(olof.sjoholm.Client.SelectableCard hitCard) {
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

    private void swap(olof.sjoholm.Client.SelectableCard cardA, olof.sjoholm.Client.SelectableCard cardB) {
        cards.swap(
                cards.indexOf(cardA, false),
                cards.indexOf(cardB, false)
        );
        clearCards();
        for (olof.sjoholm.Client.SelectableCard card : cards) {
            addCardView(card.getView());
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
    }

    public void addCard(olof.sjoholm.Client.SelectableCard card) {
        super.addCardView(card.getView());
        cards.add(card);
    }

    public List<ActionCard> getCards() {
        return null;
    }
}
