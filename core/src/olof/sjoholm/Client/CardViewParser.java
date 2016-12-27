package olof.sjoholm.Client;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Game.CardView;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.common.CardModel;
import olof.sjoholm.common.MoveModel;
import olof.sjoholm.common.RotateModel;

public class CardViewParser {
    private final Map<String, CardParser> cardParsers = new HashMap<String, CardParser>();
    private static CardViewParser instance;

    private CardViewParser() {
        addParser(new MoveCardParser(), MoveModel.class.getName());
        addParser(new RotateCardParser(), RotateModel.class.getName());
    }

    public static CardViewParser getInstance() {
        if (instance == null) {
            instance = new CardViewParser();
        }
        return instance;
    }

    public void addParser(CardParser parser, String name) {
        cardParsers.put(name, parser);
    }

    public CardView modelToView(CardModel handModel) {
        String name = handModel.getClass().getName();
        CardParser parser = cardParsers.get(name);
        if (parser != null) {
            return parser.toCard(handModel);
        }
        throw new IllegalArgumentException("No parser for " + name);
    }

    private static class MoveCardParser implements CardParser<MoveModel> {

        @Override
        public CardView toCard(MoveModel model) {
            CardView cardView = new CardView();
            cardView.setTitle("Move");
            cardView.setActionTexture(Textures.up);
            cardView.setPowerText(String.valueOf(model.steps));
            return cardView;
        }
    }

    private static class RotateCardParser implements CardParser<RotateModel> {

        @Override
        public CardView toCard(RotateModel model) {
            CardView cardView = new CardView();
            cardView.setTitle("Rotate");
            cardView.setActionTexture(getTexture(model.rotation));
            cardView.setPowerText("100");
            return cardView;
        }

        private Texture getTexture(Rotation rotation) {
            switch (rotation) {
                case LEFT:
                    return Textures.rotate_left;
                case RIGHT:
                    return Textures.rotate_right;
                case UTURN:
                    return Textures.rotate_uturn;
            }
            throw new IllegalStateException("No texture for " + rotation);
        }
    }

    private interface CardParser<T extends CardModel> {

        CardView toCard(T model);
    }
}
