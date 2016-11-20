package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Client.Screens.RotateCardActor;
import olof.sjoholm.GameWorld.Utils.Rotation;

public class Cards {

    public static RotateCardActor ROTATE_LEFT() {
        return new RotateCardActor(new RotateCard(Rotation.LEFT));
    }



}
