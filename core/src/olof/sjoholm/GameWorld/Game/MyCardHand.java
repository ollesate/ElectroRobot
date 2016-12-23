package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Interfaces.ICard;

public class MyCardHand extends HandView {
    private final Array<SelectableCard> cards = new Array<SelectableCard>();
    private SelectableCard selectedCard;

    public MyCardHand() {
        addListener(new InputListener() {
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
        });

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
        cards.swap(
                cards.indexOf(cardA, false),
                cards.indexOf(cardB, false)
        );
        clearCards();
        for (SelectableCard card : cards) {
            addCardView(card.getView());
        }
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
    }

    public void addCard(SelectableCard card) {
        super.addCardView(card.getView());
        cards.add(card);
    }

    public List<ICard> getCards() {
        return null;
    }
}
