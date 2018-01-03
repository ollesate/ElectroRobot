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
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.server.logic.PlaySet;
import olof.sjoholm.game.server.logic.Turn;
import olof.sjoholm.game.shared.logic.cards.BoardAction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.GraphicsUtil;
import olof.sjoholm.utils.ui.objects.LinearLayout;
import olof.sjoholm.utils.ui.TextureDrawable;
import olof.sjoholm.assets.Textures;

public class CardFlowPanel extends LinearLayout implements EventListener, Turn.TurnListener {
    private List<CardActor> cardActors = new ArrayList<CardActor>();
    private int currentCardIndex = -1;

    private static class CardActor extends Group {

        private final TextureDrawable background;

        private final Label nameLabel;

        private final Color backgroundColor;

        private final Label cardLabel;
        private boolean selected;
        public CardActor(String title, String description, Color color) {
            backgroundColor = new Color(color);
            Label.LabelStyle style = new Label.LabelStyle(Fonts.get(Fonts.FONT_24), Color.BLACK);
            nameLabel = new Label(title, style);
            addActor(nameLabel);
            cardLabel = new Label(description, style);
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

    public void setTurn(Turn turn) {
        clear();
        cardActors.clear();

        currentCardIndex = -1;

        int roundCount = 0;
        for (Turn.Round round : turn.getRounds()) {
            addCard("Round " + (roundCount + 1), "", Color.WHITE);
            for (Turn.Event event : round.getEvents()) {
                addCard(event.title, event.description, event.color);
            }
            roundCount++;
        }
        turn.registerEventListener(this);
    }

    @Override
    public void onEventStart(Turn.Event event) {
        next();
    }

    @Override
    public void onEventEnd(Turn.Event event) {

    }

    @Override
    public void onTurnStart() {

    }

    @Override
    public void onTurnFinished() {

    }

    @Override
    public void onRoundStart() {
        next();
    }

    @Override
    public void onRoundFinished() {

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
        CardActor cardActor = new CardActor(null, "Missiles and lasers", Color.ORANGE);
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

    private void addCard(String title, String description, Color color) {
        CardActor cardActor = new CardActor(title, description, color);
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

        return false;
    }

}
