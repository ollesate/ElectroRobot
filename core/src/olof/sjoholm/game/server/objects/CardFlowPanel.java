package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.game.server.logic.PlaySet;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.GraphicsUtil;
import olof.sjoholm.utils.ui.objects.LinearLayout;
import olof.sjoholm.utils.ui.TextureDrawable;
import olof.sjoholm.assets.Textures;

public class CardFlowPanel extends LinearLayout implements EventListener {
    private List<CardActor> cardActors = new ArrayList<CardActor>();
    private int currentCardIndex = -1;

    private static class CardActor extends Group {

        private final TextureDrawable background;

        private final Label nameLabel;

        private final Color backgroundColor;

        private final Label cardLabel;
        private boolean selected;
        public CardActor(String player, String cardText, Color color) {
            backgroundColor = new Color(color);
            Label.LabelStyle style = new Label.LabelStyle(Fonts.get(Fonts.FONT_24), Color.BLACK);
            nameLabel = new Label(player, style);
            addActor(nameLabel);
            cardLabel = new Label(cardText, style);
            addActor(cardLabel);
            background = new TextureDrawable(Textures.BACKGROUND);
        }
        @Override
        protected void sizeChanged() {
            nameLabel.setHeight(getHeight());
            nameLabel.setX(0.05f * getWidth());
            cardLabel.setHeight(getHeight());
            cardLabel.setX(50 + nameLabel.getX(Align.topRight));
        }
        @Override
        public void draw(Batch batch, float parentAlpha) {
            backgroundColor.set(backgroundColor.r, backgroundColor.g, backgroundColor.b,
                    selected ? 1f : 0.3f);
            background.draw(batch, parentAlpha, getX(), getY(), getWidth(), getHeight(),
                    1f, 1f, 0f, 0f, 0f, backgroundColor);
            super.draw(batch, parentAlpha);
        }

        public void select() {
            selected = true;
        }

        public void deselect() {
            selected = false;
        }

    }

    private static class RoundTitleActor extends Group {

        private final Label label;

        public RoundTitleActor(String round) {
            Label.LabelStyle style = new Label.LabelStyle(Fonts.get(Fonts.FONT_24), Color.BLACK);
            label = new Label(round, style);
            label.setAlignment(Align.center);
            addActor(label);
        }

        @Override
        protected void sizeChanged() {
            label.setSize(getWidth(), getHeight());
        }
    }

    public void setTurn(List<PlaySet> playSets) {
        clear();
        cardActors.clear();

        currentCardIndex = -1;

        int round = 0;
        while (PlaySet.hasMoreRounds(playSets, round)) {
            RoundTitleActor roundTitleActor = new RoundTitleActor("Round " + (round + 1));
            roundTitleActor.setHeight(GraphicsUtil.dpToPixels(50));
            addActor(roundTitleActor);
            for (PlaySet playSet : playSets) {
                if (playSet.hasRound(round)) {
                    addPlayerAction(playSet.getPlayerToken(), playSet.getRound(round));
                }
            }
            addShootingAction();
            addConveyorBeltAction();

            round++;
        }
    }

    public void addPlayerAction(PlayerToken token, BoardAction boardAction) {
        Color color;
        String name;
        if (token.getPlayer() != null) {
            color = token.getPlayer().getColor();
            name = token.getPlayer().getName();
        } else {
            color = new Color(Color.BLUE);
            name = "token " + token.getId();
        }

        String description = boardAction.getText();
        CardActor cardActor = new CardActor(name, description, color);
        cardActor.setHeight(GraphicsUtil.dpToPixels(50));
        addActor(cardActor);
        cardActors.add(cardActor);
    }

    private void addShootingAction() {
        CardActor cardActor = new CardActor(null, "All players shoot", Color.ORANGE);
        cardActor.setHeight(GraphicsUtil.dpToPixels(50));
        addActor(cardActor);
        cardActors.add(cardActor);
    }

    private void addConveyorBeltAction() {
        CardActor cardActor = new CardActor(null, "Conveyor belt", Color.ORANGE);
        cardActor.setHeight(GraphicsUtil.dpToPixels(50));
        addActor(cardActor);
        cardActors.add(cardActor);
    }

    public void next() {
        if (cardActors.size() <= currentCardIndex + 1) {
            return;
        }

        if (currentCardIndex != -1) {
            cardActors.get(currentCardIndex).deselect();
        }
        cardActors.get(++currentCardIndex).select();
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof GameBoard.TurnStartEvent) {
            setTurn(((GameBoard.TurnStartEvent) event).playSets);
        } else if (event instanceof GameBoard.TurnFinishedEvent) {

        } else if (event instanceof GameBoard.AllPlayersShootEvent) {
            next();
        } else if (event instanceof GameBoard.RunConveyorBeltEvent) {
            next();
        } else if (event instanceof GameBoard.RunLasersEvent) {

        } else if (event instanceof OnStartActionEvent) {
            next();
        } else if (event instanceof OnEndActionEvent) {

        }
        return false;
    }
}
