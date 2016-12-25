package olof.sjoholm.common;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Actors.Cards.MoveCard;
import olof.sjoholm.GameWorld.Actors.Cards.RotateCard;
import olof.sjoholm.GameWorld.Utils.Direction;
import olof.sjoholm.GameWorld.Utils.Rotation;
import olof.sjoholm.Interfaces.ActionCard;

public class CardFactory {
    private List<FactoryWeightPair> factories = new ArrayList<FactoryWeightPair>();
    private int weightSum;
    private static CardFactory cardFactory;

    private CardFactory() {
        addFactory(new MoveCardFactory(), 3);
        addFactory(new RotateCardFactory(), 1);
    }

    public static CardFactory getInstance() {
        if (cardFactory == null) {
            cardFactory = new CardFactory();
        }
        return cardFactory;
    }

    private void addFactory(AbstractFactory abstractFactory, int weight) {
        weightSum += weight;
        factories.add(new FactoryWeightPair(abstractFactory, weight));
    }

    public ActionCard createRandom() {
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
        public ActionCard create() {
            return new MoveCard(
                    new CardModel(
                            RotateCard.class.getSimpleName(),
                            getRandomPriority()
                    ),
                    getRandomDirection(),
                    getRandomSteps()
            );
        }

        private int getRandomSteps() {
            return MathUtils.random(1, 3);
        }

        private Direction getRandomDirection() {
            return Direction.values()[(int) (Math.random() * Direction.values().length)];
        }

        private int getRandomPriority() {
            return MathUtils.random(4);
        }
    }

    private static class RotateCardFactory implements AbstractFactory {

        @Override
        public ActionCard create() {
            return new RotateCard(
                    new CardModel(
                            RotateCard.class.getSimpleName(),
                            getRandomPriority()
                    ),
                    getRandomRotation()
            );
        }

        private int getRandomPriority() {
            return MathUtils.random(4);
        }

        private Rotation getRandomRotation() {
            return Rotation.values()[(int) (Math.random() * Rotation.values().length)];
        }
    }

    interface AbstractFactory {

        ActionCard create();
    }

    private static class FactoryWeightPair {
        int weight;
        AbstractFactory factory;

        FactoryWeightPair(AbstractFactory abstractFactory, int weight) {
            factory = abstractFactory;
            this.weight = weight;
        }
    }
}
