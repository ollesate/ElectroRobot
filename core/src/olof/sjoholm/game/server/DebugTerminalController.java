package olof.sjoholm.game.server;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.PlaySet;
import olof.sjoholm.game.server.objects.ConveyorBelt;
import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.server.objects.Terminal;
import olof.sjoholm.game.shared.DebugUtil;
import olof.sjoholm.game.shared.logic.CardGenerator;
import olof.sjoholm.game.shared.logic.Movement;
import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.NumberUtils;

public class DebugTerminalController {
    private final GameBoard gameBoard;
    private final Terminal terminal;

    public DebugTerminalController(GameBoard gameBoard, Terminal terminal) {
        this.gameBoard = gameBoard;
        this.terminal = terminal;
    }

    public void handleEvent(TerminalEvent event) {
        String command = event.getCommand();
        if ("spawn".equals(command)) {
            handleSpawn(event);
        } else if ("token".equals(command)) {
            handleToken(event);
        } else if ("belts".equals(event.getCommand())) {
            handleBelts(event);
        } else if ("play".equals(event.getCommand())) {
            handlePlay(event);
        }
    }

    private void handlePlay(TerminalEvent event) {
        if ("turn".equals(event.get(1))) {
            int cards;
            if (event.get(2) == null) {
                cards = Constants.CARDS_TO_PLAY;
            } else {
                cards = event.getInt(2, -1);
            }

            if (cards == -1) {
                terminal.writeError("Error getting nr of cards");
                return;
            }

            terminal.writeLine("Playing turn");
            List<PlayerToken> actors = gameBoard.getActors(PlayerToken.class);
            List<PlaySet> playSets = new ArrayList<PlaySet>();
            for (PlayerToken token : actors) {
                playSets.add(new PlaySet(token, CardGenerator.generateList(cards)));
            }
            gameBoard.playTurn(playSets);
        }
    }

    private void handleToken(TerminalEvent event) {
        PlayerToken playerToken = gameBoard.getActorById(event.getInt(1, -1), PlayerToken.class);
        if (playerToken == null) {
            terminal.writeError("No token for " + event.get(1));
            return;
        }

        if ("perform".equals(event.get(2))) {
            int length = event.getLength(3);
            if (length == 0) {
                terminal.writeError("No actions");
            }
            SequenceAction sequenceAction = new SequenceAction();
            for (int i = 3; i < length + 3; i++) {
                String[] actions = event.get(i).split(":");
                String action = actions.length > 0 ? actions[0] : null;
                Action gdxAction = null;
                if ("move".equals(action) && actions.length == 3) {
                    Movement movement = Movement.fromString(actions[1]);
                    int steps = NumberUtils.toInt(actions[2], -1);
                    if (movement != null && steps != -1) {
                        terminal.writeLine("Perform movement " + movement + " " + steps);
                        gdxAction = playerToken.getPushMoveAction(movement, steps);
                    } else {
                        terminal.writeError("Failure with move");
                    }
                } else if ("rotate".equals(action) && actions.length == 2) {
                    Rotation rotation = Rotation.fromString(actions[1]);
                    if (rotation != null) {
                        terminal.writeLine("Perform rotation " + rotation);
                        gdxAction = playerToken.rotate(rotation);
                    }
                }
                if (gdxAction != null) {
                    sequenceAction.addAction(gdxAction);
                }
            }
            if (sequenceAction.getActions().size > 0) {
                System.out.print("add sequence");
                gameBoard.addAction(sequenceAction);
            }
        }
    }

    private void handleSpawn(TerminalEvent event) {
        String what = event.get(1);
        if ("token".equals(what)) {
            try {
                int x = event.getInt(2);
                int y = event.getInt(3);
                int id = gameBoard.spawnToken(x, y).getId();
                terminal.writeLine("Spawned token with id " +id);
            } catch (TerminalException e) {
                terminal.writeError(e.getMessage());
            }
        }
    }

    private void handleBelts(TerminalEvent terminalEvent) {
        List<ConveyorBelt> belts = gameBoard.getActors(ConveyorBelt.class);
        if (belts.size() == 0) {
            print("No conveyor belts could be found!");
            return;
        }
        print("Start conveyor belts");
        ParallelAction beltActions = new ParallelAction();
        for (ConveyorBelt belt : belts) {
            beltActions.addAction(belt.getAction());
        }
        gameBoard.addAction(beltActions);
    }

    private void print(String message) {
        terminal.writeLine(message);
    }
}
