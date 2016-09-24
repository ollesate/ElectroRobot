package olof.sjoholm.GameLogic;

/**
 * Created by sjoholm on 24/09/16.
 */
public interface GameStageListener {

    void onStartActingStage();

    void onEndActingStage();

    void onStartSimulationStage();

    void onEndSimulationStage();

}
