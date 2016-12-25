package olof.sjoholm.GameWorld.Actors.Cards;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Game.CardView;
import olof.sjoholm.Interfaces.ActionCard;

public class CardRepresentations {

    public static void fromICard(ActionCard card) {
        CardView cardView = new CardView();
        if (card instanceof MoveCard) {
            MoveCard moveCard = ((MoveCard) card);
            cardView.setPowerText(String.valueOf(moveCard.getSteps()));
            switch (moveCard.getDirection()) {
                case UP:
                    cardView.setActionTexture(Textures.up);
                    cardView.setTitle("Move forward");
                    break;
                case LEFT:
                    cardView.setActionTexture(Textures.left);
                    break;
                case RIGHT:
                    cardView.setActionTexture(Textures.right);
                    break;
                case DOWN:
                    cardView.setActionTexture(Textures.down);
                    cardView.setTitle("Reverse");
                    break;
            }
        } else if (card instanceof RotateCard) {

            switch (((RotateCard) card).getRotation()) {
                case LEFT:
                    cardView.setActionTexture(Textures.rotate_left);
                    break;
                case RIGHT:
                    cardView.setActionTexture(Textures.rotate_right);
                    break;
                case UTURN:
                    cardView.setActionTexture(Textures.rotate_uturn);
                    break;
            }
        }

        // return cardView;
    }

}
