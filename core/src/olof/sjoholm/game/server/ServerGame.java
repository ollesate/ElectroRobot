package olof.sjoholm.game.server;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.assets.Skins;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.objects.GameBoard;
import olof.sjoholm.game.server.server_logic.Player;
import olof.sjoholm.game.server.server_logic.ServerLogic;
import olof.sjoholm.game.shared.logic.Rotation;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.logic.cards.MoveForward;
import olof.sjoholm.game.shared.logic.cards.Rotate;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.net.ServerConnection;
import olof.sjoholm.utils.Logger;
import olof.sjoholm.utils.TextUtils;

public class ServerGame extends Game implements ServerConnection.OnMessageListener,
        ServerConnection.OnPlayerConnectedListener, ServerConnection.OnPlayerDisconnectedListener, EventListener {

    private ServerConnection serverConnection;
    private ServerLogic serverLogic;
    private ServerGameScreen serverGameScreen;

    public ServerGame() {
        serverConnection = new ServerConnection();
        serverConnection.setOnMessageListener(this);
        serverConnection.setOnPlayerConnectedListener(this);
        serverConnection.setOnPlayerDisconnectedListener(this);
    }

    @Override
    public void create () {
        Logger.d("Game create");
        Textures.initialize();
        Skins.initialize();
        Fonts.initialize();
        serverGameScreen = new ServerGameScreen();
        serverGameScreen.setMessageListener(this);
        setScreen(serverGameScreen);
        serverConnection.connect();
        serverLogic = new ServerLogic(serverGameScreen, serverConnection);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
        Textures.dispose();
        Skins.dispose();
        serverConnection.disconnect();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Logger.d("Set screen " + screen.getClass().getSimpleName());
    }

    @Override
    public void onMessage(final int playerId, final Envelope envelope) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                onMessageUi(playerId, envelope);
            }
        });
    }

    @Override
    public void onPlayerConnected(int playerId) {
        serverLogic.onPlayerConnected(playerId);
    }

    @Override
    public void onPlayerDisconnected(int playerId) {
        serverLogic.onPlayerDisconnected(playerId);
    }

    private void onMessageUi(int id, Envelope envelope) {
        if (envelope instanceof Envelope.PlayerSelectColor) {
            Color color = ((Envelope.PlayerSelectColor) envelope).getColor();
            serverLogic.onPlayerChangeColor(id, color);
        } else if (envelope instanceof Envelope.PlayerSelectName) {
            String name = ((Envelope.PlayerSelectName) envelope).name;
            serverLogic.onPlayerChangeName(id, name);
        } else if (envelope instanceof Envelope.PlayerReady) {
            boolean ready = ((Envelope.PlayerReady) envelope).ready;
            serverLogic.onPlayerReady(id, ready);
        } else if (envelope instanceof Envelope.SendCards) {
            List<BoardAction> cards = ((Envelope.SendCards) envelope).cards;
            serverLogic.onCardsReceived(id, cards);
        } else if (envelope instanceof Envelope.UnReadyMyCards) {
            serverLogic.onCardsReady(id, false);
        }
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof TerminalEvent) {
            String[] args = ((TerminalEvent) event).args;
            Logger.d("onTerminalMessage " + TextUtils.join(args, ", "));
            TerminalHandler terminalHandler = new TerminalHandler();
            try {
                terminalHandler.handleMessage(args);
            } catch (TerminalException e) {
                System.out.print("Error " + e.getMessage());
                serverGameScreen.onTerminalError(e.getMessage());
            }
            return true;
        } else if (event instanceof GameBoard.OnPlayerReachedCheckpoints) {
            System.out.println("One player reached all stops");
            Player player = ((GameBoard.OnPlayerReachedCheckpoints) event).player;
            setScreen(new GameFinishedScreen(player.getName()));
        }
        return false;
    }

    private class TerminalHandler {

        private String[] args;

        public void handleMessage(String[] args) throws TerminalException {
            this.args = args;

            String command = get(0, "No command provided");
            if ("player".equals(command)) {
                int playerId = getInt(1, "No valid player id");
                String operation = get(2, "No operation provided");
                if ("join".equals(operation)) {
                    onPlayerConnected(playerId);
                } else if ("quit".equals(operation)) {
                    onPlayerDisconnected(playerId);
                } else if ("ready".equals(operation)) {
                    onMessageUi(playerId, new Envelope.PlayerReady(true));
                } else if ("unready".equals(operation)) {
                    onMessageUi(playerId, new Envelope.PlayerReady(false));
                } else if ("color".equals(operation)) {
                    Color color = getColor(3, "No valid color");
                    if (color != null) {
                        onMessageUi(playerId, new Envelope.PlayerSelectColor(color));
                    }
                } else if ("name".equals(operation)) {
                    String name = get(3, "No name provided");
                    if (name != null) {
                        onMessageUi(playerId, new Envelope.PlayerSelectName(name));
                    }
                } else if ("cards".equals(operation)) {
                    int cardIndex = 4;
                    List<BoardAction> boardActions = new ArrayList<BoardAction>();
                    boardActions.add(new MoveForward(5));
                    boardActions.add(new Rotate(Rotation.LEFT));
                    boardActions.add(new MoveForward(2));
                    boardActions.add(new Rotate(Rotation.LEFT));
                    boardActions.add(new MoveForward(2));
                    String cardStr;
                    while ((cardStr = get(cardIndex)) != null) {

                    }
                    onMessage(playerId, new Envelope.SendCards(boardActions));
                }
            }
        }

        private Color getColor(int index, String error) throws TerminalException {
            String color = get(index, error);
            if ("black".equals(color)) {
                return Color.BLACK;
            } else if ("orange".equals(color)) {
                return Color.ORANGE;
            } else if ("white".equals(color)) {
                return Color.WHITE;
            } else if ("red".equals(color)) {
                return Color.RED;
            } else if ("green".equals(color)) {
                return Color.GREEN;
            } else if ("yellow".equals(color)) {
                return Color.YELLOW;
            } else if ("blue".equals(color)) {
                return Color.BLUE;
            }
            throw new TerminalException(error);
        }

        private String get(int index, String error) throws TerminalException {
            if (args.length > index) {
                return args[index];
            }
            throw new TerminalException(error);
        }

        private String get(int index) {
            if (args.length > index) {
                return args[index];
            }
            return null;
        }

        private int getInt(int index, String error) throws TerminalException {
            String id = get(index, error);
            try {
                return Integer.valueOf(id);
            } catch (NumberFormatException e) {
                throw new TerminalException(error);
            }
        }
    }
}