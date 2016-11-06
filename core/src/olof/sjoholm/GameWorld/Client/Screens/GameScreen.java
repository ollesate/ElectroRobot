package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

import olof.sjoholm.GameWorld.Actors.Cards.BaseCard;
import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Client.ClientGameHandler;
import olof.sjoholm.GameWorld.Server.Player;
import olof.sjoholm.GameWorld.Utils.CardUtil;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;
import olof.sjoholm.Interfaces.ICard;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private CardHand cardHand;

    public GameScreen(ClientGameHandler.PlayerHandler playerHandler) {
        cardHand = new CardHand();
        cardHand.setY(150);
        cardHand.setX(50);
        PlayerAdapter playerAdapter = new PlayerAdapter(cardHand);
        playerHandler.setPlayer(playerAdapter);
    }

    @Override
    public void show() {
        setBackgroundColor(Color.RED);

        stage = new Stage();
        stage.addActor(cardHand);

        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 coord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                Actor hitActor = stage.hit(coord.x, coord.y, false);

                if (hitActor != null && hitActor.getParent() instanceof BaseCard) {
                    BaseCard pressedCard = ((BaseCard) hitActor.getParent());
                    Logger.d("Select card " + pressedCard.getClass().getSimpleName());
                    cardHand.press(pressedCard);
                } else {
                    cardHand.deselect();
                }

                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private static class PlayerAdapter implements Player {
        private CardHand cardHand;

        public PlayerAdapter(CardHand cardHand) {
            this.cardHand = cardHand;
        }

        @Override
        public void dealCards(final List<ICard> cards) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    cardHand.showCards(cards);
                }
            });
        }

        @Override
        public void getCards(OnCardsReceivedListener onCardsReceivedListener) {
            List<ICard> cards = cardHand.getCards();
            for (ICard card : cards) {
                Logger.d("Card->" + card.toString());
            }
            onCardsReceivedListener.onCardsReceived(cards);
        }

        @Override
        public boolean hasCards() {
            return false;
        }

        @Override
        public ICard popTopCard() {
            return cardHand.selectNextCard();
        }
    }


}
