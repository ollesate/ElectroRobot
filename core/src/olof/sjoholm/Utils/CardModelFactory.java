package olof.sjoholm.Utils;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Models.CardModel;
import olof.sjoholm.Models.MoveModel;
import olof.sjoholm.Models.RotateModel;

public class CardModelFactory {
    private List<FactoryWeightPair> factories = new ArrayList<FactoryWeightPair>();
    private int weightSum;
    private static CardModelFactory cardFactory;

    private CardModelFactory() {
        addFactory(new MoveCardFactory(), 2);
        addFactory(new RotateCardFactory(), 1);
    }

    public static CardModelFactory getInstance() {
        if (cardFactory == null) {
            cardFactory = new CardModelFactory();
        }
        return cardFactory;
    }

    private void addFactory(AbstractFactory abstractActionFactory, int weight) {
        weightSum += weight;
        factories.add(new FactoryWeightPair(abstractActionFactory, weight));
    }

    public CardModel createRandom() {
        int rand = MathUtils.random(weightSum - 1);
        int sum = 0;
        for (FactoryWeightPair factoryWeightPair : factories) {
            AbstractFactory factory = factoryWeightPair.factory;
            sum += factoryWeightPair.weight;
            if (rand < sum) {
                return factory.create();
            }
        }
        throw new IllegalStateException("This should not happen");
    }

    private static class MoveCardFactory implements AbstractFactory {

        @Override
        public CardModel create() {
            MoveModel model = new MoveModel();
            model.type = MoveModel.class.getName();
            model.steps = MathUtils.random(1, 4);
            model.direction = Direction.FORWARD;
            return model;
        }
    }

    private static class RotateCardFactory implements AbstractFactory {

        @Override
        public CardModel create() {
            RotateModel model = new RotateModel();
            model.priority = MathUtils.random(1, 4);
            model.type = RotateModel.class.getName();
            model.rotation = Rotation.random();
            return model;
        }
    }

    interface AbstractFactory {

        CardModel create();
    }

    private static class FactoryWeightPair {
        int weight;
        AbstractFactory factory;

        FactoryWeightPair(AbstractFactory actionFactory, int weight) {
            factory = actionFactory;
            this.weight = weight;
        }
    }
}
