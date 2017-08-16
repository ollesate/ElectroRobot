package olof.sjoholm.Api;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Utils.Rotation;

public class CardGenerator {

    private CardGenerator() {
    }

    public static List<BoardAction> generateList(int nrOfCards) {
        RandomGenerator<BoardActionFactory> factories = new RandomGenerator<BoardActionFactory>();
        factories.add(2, new RotateFactory());
        factories.add(3, new MovementFactory());

        List<BoardAction> list = new ArrayList<BoardAction>(nrOfCards);
        for (int i = 0; i < nrOfCards; i++) {
            BoardAction boardAction = factories.get().getRandom();
            boardAction.setId(i);
            list.add(boardAction);
        }
        return list;
    }

    private static class RotateFactory implements BoardActionFactory {
        private final RandomGenerator<Rotation> rotationGenerator;

        public RotateFactory() {
            rotationGenerator = new CardGenerator.RandomGenerator<Rotation>();
            rotationGenerator.add(1, Rotation.LEFT);
            rotationGenerator.add(1, Rotation.RIGHT);
            rotationGenerator.add(1, Rotation.UTURN);
        }

        @Override
        public BoardAction getRandom() {
            Rotation rotation = rotationGenerator.get();
            return new BoardAction.Rotate(rotation);
        }
    }

    private static class MovementFactory implements BoardActionFactory {
        private final RandomGenerator<Integer> stepGenerator;

        public MovementFactory() {
            stepGenerator = new RandomGenerator<Integer>();
            stepGenerator.add(1, 1);
            stepGenerator.add(2, 2);
            stepGenerator.add(2, 3);
            stepGenerator.add(1, 4);
        }

        @Override
        public BoardAction getRandom() {
            int randSteps = stepGenerator.get();
            return new BoardAction.MoveForward(randSteps);
        }
    }

    private interface BoardActionFactory {

        BoardAction getRandom();
    }

    private static class RandomGenerator<T> {
        List<Pair<Integer, T>> objects = new ArrayList<Pair<Integer, T>>();
        int size;

        public void add(int weight, T object) {
            if (weight <= 0) {
                throw new IllegalArgumentException("This object need to have a change that is a positive number " + object);
            }
            objects.add(new Pair<Integer, T>(size, object));
            size += weight;
        }

        public T get() {
            int rand = MathUtils.random(1, size);
            for (int i = objects.size() - 1; i >= 0; i--) {
                Pair<Integer, T> pair = objects.get(i);
                if (rand > pair.key) {
                    return pair.value;
                }
            }
            throw new IllegalStateException("Should never happen");
        }
    }
}
