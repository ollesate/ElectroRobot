package olof.sjoholm.Client.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.Api.GraphicsUtil;
import olof.sjoholm.Api.Pair;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Skins;
import olof.sjoholm.Utils.Logger;


public class HandStage extends Stage {
    private final List<Pair<BoardAction, CardActor>> cardActors = new ArrayList<Pair<BoardAction, CardActor>>();
    private final HandGroup handGroup;

    public HandStage() {
        handGroup = new HandGroup();
        addActor(handGroup);
    }

    public void resize(int width, int height) {
        Camera camera = new OrthographicCamera(width, height);
        setViewport(new FitViewport(width, height, camera));
        getViewport().update(width, height, true);
        handGroup.setX(width * 0.1f);
        handGroup.setY(getHeight() - GraphicsUtil.dpToPixels(16f));
        handGroup.setSize(width * 0.8f, height * 0.8f);
        handGroup.setSpacing(GraphicsUtil.dpToPixels(8f));
    }

    private static class CardActor extends Group {
        private Action animateAction;
        private CardActor animateActor;

        private Label label;
        private DrawableActor background;

        public CardActor(String text) {
            background = new DrawableActor(new TextureDrawable(Textures.background));
            setColor(Color.ORANGE);
            addActor(background);

            label = new Label(text, Skins.DEFAULT);
            addActor(label);
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            background.setWidth(getWidth());
            background.setHeight(getHeight());
            label.setX((getWidth() - label.getWidth()) / 2);
            label.setY((getHeight() - label.getHeight()) / 2);
        }

        public CardActor copy() {
            CardActor actor = new CardActor(label.getText().toString());
            actor.setWidth(getWidth());
            actor.setHeight(getHeight());
            actor.setX(getX());
            actor.setY(getY());
            actor.setColor(getColor());
            return actor;
        }

        public void animate(float x, float y) {
            setVisible(false);

            if (animateActor == null) {
                animateActor = copy();
                animateActor.setX(getX());
                animateActor.setY(getY());
                getParent().addActor(animateActor);
            }
            if (!animateActor.isVisible()) {
                animateActor.setVisible(true);
                animateActor.setX(getX());
                animateActor.setY(getY());
            }

            animateActor.removeAction(animateAction);

            animateAction = Actions.sequence(
                    Actions.moveTo(x, y, .2f, Interpolation.circle),
                    finishAction
            );
            animateActor.addAction(animateAction);

            setX(x);
            setY(y);
        }

        @Override
        public void setColor(Color color) {
            super.setColor(color);
            background.setColor(color);
        }

        private final Action finishAction = new Action() {
            @Override
            public boolean act(float delta) {
                setVisible(true);
                animateActor.setVisible(false);
                return true;
            }
        };
    }

    private static class HandGroup extends Group {
        private final List<CardActor> cardActors = new ArrayList<CardActor>();
        private float spacing;

        public void addCard(final CardActor actor) {
            addActor(actor);
            cardActors.add(actor);
            actor.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    onCardPressed(actor, x, y);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    onCardReleased(actor, x, y);
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    onCardDragged(actor, x, y);
                }
            });
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            float height = (getHeight() - spacing * (getChildren().size - 1)) / getChildren().size;
            float yPos = -height;
            for (Actor actor : getChildren()) {
                actor.setWidth(getWidth());
                actor.setHeight(height);
                actor.setY(yPos);
                yPos -= actor.getHeight() + spacing;
            }
        }

        private static class DraggedCard {
            public final CardActor fake;
            public final float initialY;

            public DraggedCard(CardActor fake, float initialY) {
                this.fake = fake;
                this.initialY = initialY;
            }
        }

        private Map<CardActor, DraggedCard> draggedCards = new HashMap<CardActor, DraggedCard>();

        private void onCardPressed(CardActor card, float x, float y) {
            Logger.d("onCardPressed " + x + " " + y);

            card.setVisible(false);

            DraggedCard draggedCard = new DraggedCard(card.copy(), y);
            addActor(draggedCard.fake);
            draggedCards.put(card, draggedCard);
        }

        private void onCardReleased(CardActor card, float x, float y) {
            Logger.d("onCardReleased " + x + " " + y);
            DraggedCard draggedCard = draggedCards.get(card);

            card.setVisible(true);

            removeActor(draggedCard.fake);
            draggedCards.remove(card);
        }

        private void onCardDragged(CardActor card, float x, float y) {
            DraggedCard draggedCard = draggedCards.get(card);

            draggedCard.fake.setY(card.getY() + y - draggedCard.initialY);
            Vector2 tempPos = new Vector2();

            for (CardActor other : cardActors) {
                if (card.equals(other)) {
                    continue;
                }

                float yDir = draggedCard.fake.getY() - card.getY();
                float yPos = draggedCard.fake.getY();
                if (yDir > 0 && card.getY() < other.getY() && yPos > other.getY()) {
                    // Dragged up. Swap position.
                    // Do this in the class instead. Animate to: set pos directly, but animate. :)
                    tempPos.set(other.getX(), other.getY());
                    other.animate(card.getX(), card.getY());
                    card.setPosition(tempPos.x, tempPos.y);
                    break;
                } else if (yDir < 0 && card.getY() > other.getY() && yPos < other.getY()) {
                    // Dragged down. Swap position.
                    tempPos.set(other.getX(), other.getY());
                    other.animate(card.getX(), card.getY());
                    card.setPosition(tempPos.x, tempPos.y);
                    break;
                }
            }
        }

        public void setSpacing(float spacing) {
            this.spacing = spacing;
            sizeChanged();
        }
    }

    public void addCard(BoardAction boardAction) {
        final CardActor actor = new CardActor(boardAction.getText());
        handGroup.addCard(actor);

        cardActors.add(new Pair<BoardAction, CardActor>(boardAction, actor));
    }

    public List<BoardAction> getCards() {
        // Sort cards by y.
        SortedMap<Float, BoardAction> sortedMap = new TreeMap<Float, BoardAction>();
        for (Pair<BoardAction, CardActor> pair : cardActors) {
            sortedMap.put(pair.value.getY(), pair.key);
        }
        return new ArrayList<BoardAction>(sortedMap.values());
    }
}
