package olof.sjoholm.Client;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.common.CardModel;

public class CardHandModel {
    private final List<CardModel> models = new ArrayList<CardModel>();
    private OnHandChangedListener onHandChangedListener;

    public List<CardModel> getCardModels() {
        return models;
    }

    public void setOnHandChangedListener(OnHandChangedListener onHandChangedListener) {
        this.onHandChangedListener = onHandChangedListener;
    }

    public void swap(int indexA, int indexB) {
        CardModel modelA = models.get(indexA);
        models.set(indexA, models.get(indexB));
        models.set(indexB, modelA);

        if (onHandChangedListener != null) {
            onHandChangedListener.onChanged();
        }
    }

    public void addCardModels(List<CardModel> list) {
        for (CardModel cardModel : list) {
            models.add(cardModel);
        }
        if (onHandChangedListener != null) {
            onHandChangedListener.onChanged();
        }
    }

    public void clear() {
        models.clear();
        if (onHandChangedListener != null) {
            onHandChangedListener.onChanged();
        }
    }

    public CardModel popTop() {
        CardModel cardModel = models.get(0);
        models.remove(0);
        return cardModel;
    }

    public interface OnHandChangedListener {

        void onChanged();
    }
}
