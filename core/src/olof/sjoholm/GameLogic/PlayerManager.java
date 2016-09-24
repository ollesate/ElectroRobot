package olof.sjoholm.GameLogic;

/**
 * Created by sjoholm on 25/09/16.
 */
public abstract class PlayerManager implements GameStageListener {
    private TurnEnderCallback callback;
    private CardSet cards;
    private ICardManager cardManager;

    public PlayerManager(ICardManager cardManager, TurnEnderCallback callback) {
        this.cardManager = cardManager;
        this.callback = callback;
    }

    protected void endTurn() {
        callback.onEndActingStage(this);
    }

    public CardSet getCards() {
        return cards;
    }

    @Override
    public void onStartActingStage() {

    }
}
