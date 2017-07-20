package olof.sjoholm.Client;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Actors.CardView;
import olof.sjoholm.Utils.Direction;
import olof.sjoholm.Utils.Rotation;
import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Models.MoveModel;
import olof.sjoholm.Models.RotateModel;

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
            CardView.Resource resource = new CardView.Resource();
            resource.texture = getTexture(model.direction);
            resource.powerText = String.valueOf(model.steps);
            resource.title = "Move";
            return new CardView(resource);
        }

        private Texture getTexture(Direction direction) {
            switch (direction) {
                case FORWARD:
                    return Textures.up;
                case CRAB_LEFT:
                    return Textures.left;
                case CRAB_RIGHT:
                    return Textures.right;
                case BACKWARDS:
                    return Textures.down;
            }
            throw new IllegalArgumentException("No texture for " + direction);
        }
    }

    private static class RotateCardParser implements CardParser<RotateModel> {

        @Override
        public CardView toCard(RotateModel model) {
            CardView.Resource resource = new CardView.Resource();
            resource.texture = getTexture(model.rotation);
            resource.powerText = "100";
            resource.title = "Rotate";
            return new CardView(resource);
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
