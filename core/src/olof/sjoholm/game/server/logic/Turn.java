package olof.sjoholm.game.server.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;

public class Turn {
    private final List<Round> rounds;
    private TurnListener turnListener;

    public interface TurnListener {

        void onEventStart(Event event);

        void onEventEnd(Event event);

        void onTurnStart();

        void onTurnFinished();

        void onRoundStart();

        void onRoundFinished();
    }

    private Turn(List<Round> rounds) {
        this.rounds = rounds;
    }

    public SequenceAction getSequence() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (turnListener != null) {
                    turnListener.onTurnStart();
                }
                return true;
            }
        });
        for (Round round : rounds) {
            sequenceAction.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    if (turnListener != null) {
                        turnListener.onRoundStart();
                    }
                    return true;
                }
            });
            for (final Event event : round.getEvents()) {
                sequenceAction.addAction(Actions.sequence(
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                if (turnListener != null) {
                                    turnListener.onEventStart(event);
                                }
                                return true;
                            }
                        },
                        event.action,
                        new Action() {
                            @Override
                            public boolean act(float delta) {
                                if (turnListener != null) {
                                    turnListener.onEventEnd(event);
                                }
                                return true;
                            }
                        }
                ));
            }
            sequenceAction.addAction(new Action() {
                @Override
                public boolean act(float delta) {
                    if (turnListener != null) {
                        turnListener.onRoundFinished();
                    }
                    return true;
                }
            });
        }
        sequenceAction.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (turnListener != null) {
                    turnListener.onTurnFinished();
                }
                return true;
            }
        });
        return sequenceAction;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void registerEventListener(TurnListener turnListener) {
        this.turnListener = turnListener;
    }

    public static class Event {
        public final Action action;
        public final String title;
        public final String description;
        public final Color color;

        public Event(Action action, String title) {
            this(action, title, "", Color.ORANGE);
        }

        public Event(Action action, String title, String description, Color color) {
            this.action = action;
            this.title = title;
            this.description = description;
            this.color = color;
        }
    }

    public static class BoardActionEvent extends Event {
        private final BoardAction boardAction;
        private final PlayerToken playerToken;

        public BoardActionEvent(BoardAction boardAction, PlayerToken playerToken) {
            super(new BoardActionRunner(boardAction, playerToken), boardAction.getText(), "", playerToken.getColor());
            this.boardAction = boardAction;
            this.playerToken = playerToken;
        }
    }

    public static class Builder {
        private List<Round.Builder> roundBuilders = new ArrayList<Round.Builder>();

        public Round.Builder addRound() {
            Round.Builder roundBuilder = new Round.Builder();
            roundBuilders.add(roundBuilder);
            return roundBuilder;
        }

        public Turn build() {
            List<Round> rounds = new ArrayList<Round>();
            for (Round.Builder roundBuilder : roundBuilders) {
                rounds.add(roundBuilder.build());
            }
            return new Turn(rounds);
        }
    }

    public static class Round {
        private final List<Event> events;

        public Round(List<Event> events) {
            this.events = events;
        }

        public List<Event> getEvents() {
            return events;
        }

        public static class Builder {
            private final List<Event> events = new ArrayList<Event>();

            private Builder() {

            }

            public Builder addEvent(Event event) {
                events.add(event);
                return this;
            }

            public Round build() {
                return new Round(events);
            }
        }
    }

    private static class BoardActionRunner extends ActionRunner {
        public final BoardAction boardAction;
        public final PlayerToken playerToken;

        public BoardActionRunner(BoardAction boardAction, PlayerToken playerToken) {
            this.boardAction = boardAction;
            this.playerToken = playerToken;
        }

        @Override
        Action getAction() {
            return boardAction.getAction(playerToken);
        }
    }

    private abstract static class ActionRunner extends Action {
        private Action action;

        abstract Action getAction();

        private Action get() {
            if (action == null) {
                action = getAction();
            }
            return action;
        }

        @Override
        public boolean act(float delta) {
            return get().act(delta);
        }

        @Override
        public void restart() {
            get().restart();
        }

        @Override
        public void setActor(Actor actor) {
            get().setActor(actor);
        }

        @Override
        public Actor getActor() {
            return get().getActor();
        }

        @Override
        public void setTarget(Actor target) {
            get().setTarget(target);
        }

        @Override
        public Actor getTarget() {
            return get().getTarget();
        }

        @Override
        public void reset() {
            get().reset();
        }

        @Override
        public Pool getPool() {
            return get().getPool();
        }

        @Override
        public void setPool(Pool pool) {
            get().setPool(pool);
        }
    }
}
