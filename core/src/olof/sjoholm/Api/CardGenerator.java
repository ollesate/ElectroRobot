package olof.sjoholm.Api;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Utils.Rotation;

public class CardGenerator {

    public static List<BoardAction> generateList(int nrOfCards) {
        List<BoardAction> list = new ArrayList<BoardAction>(nrOfCards);
        for (int i = 0; i < nrOfCards; i++) {
            BoardAction boardAction = randomAction();
            boardAction.setId(i);
            list.add(boardAction);
        }
        return list;
    }

    private static BoardAction randomAction() {
        if (Math.random() > 0.65) {
            int steps = MathUtils.random(1, 4);
            return new BoardAction.MoveForward(steps);
        } else {
            int rand = MathUtils.random(2);
            switch (rand) {
                case 0:
                    return new BoardAction.Rotate(Rotation.LEFT);
                case 1:
                    return new BoardAction.Rotate(Rotation.RIGHT);
                case 2:
                    return new BoardAction.Rotate(Rotation.UTURN);
                default:
                    throw new IllegalStateException("Woops");
            }
        }
    }
}
