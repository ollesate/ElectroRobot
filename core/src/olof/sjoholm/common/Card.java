package olof.sjoholm.common;

import olof.sjoholm.Interfaces.ActionCard;

public abstract class Card implements ActionCard {
    protected CardModel cardModel;

    public Card(CardModel cardModel) {
        this.cardModel = cardModel;
    }

    @Override
    public int getPriority() {
        return cardModel.priority;
    }

    @Override
    public String getType() {
        return cardModel.type;
    }
}
