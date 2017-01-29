package olof.sjoholm.Utils;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.GameLogic.Cards.MoveAction;
import olof.sjoholm.GameLogic.Cards.RotateAction;
import olof.sjoholm.Interfaces.Action;
import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Models.MoveModel;
import olof.sjoholm.Models.RotateModel;

public class Actions {
    private final Map<String, ActionParser> parsers = new HashMap<String, ActionParser>();
    private static Actions instance;

    private Actions() {
        addParser(MoveModel.class, new MoveParser());
        addParser(RotateModel.class, new RotateParser());
    }

    private static Actions getInstance() {
        if (instance == null) {
            instance = new Actions();
        }
        return instance;
    }

    private void addParser(Class<? extends CardModel> clazz, ActionParser parser) {
        parsers.put(clazz.getName(), parser);
    }

    public static Action fromModel(CardModel model) {
        String name = model.getClass().getName();
        ActionParser parser = getInstance().parsers.get(name);
        if (parser != null) {
            return parser.parse(model);
        }
        throw new IllegalArgumentException("No parser exists for " + name);
    }

    private static class MoveParser implements ActionParser<MoveModel> {

        @Override
        public Action parse(MoveModel model) {
            return new MoveAction(model.direction, model.steps);
        }
    }

    private static class RotateParser implements ActionParser<RotateModel> {

        @Override
        public Action parse(RotateModel model) {
            return new RotateAction(model.rotation);
        }
    }

    private interface ActionParser<T extends CardModel> {

        Action parse(T model);
    }
}
