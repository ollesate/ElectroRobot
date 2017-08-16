package olof.sjoholm.Client.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import olof.sjoholm.Api.BoardAction;
import olof.sjoholm.Api.ColorUtils;
import olof.sjoholm.Api.Fonts;
import olof.sjoholm.Api.GraphicsUtil;
import olof.sjoholm.Api.Pair;
import olof.sjoholm.Api.Palette;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Utils.Constants;


public class HandStage extends Stage {
    private static final Color COLOR_SELECTED = Palette.ACCENT;
    private static final Color COLOR_CONSUMED = Palette.PRIMARY_DESELECTED;

    private final List<Pair<BoardAction, CardActor>> cardActors = new ArrayList<Pair<BoardAction, CardActor>>();
    private final HandGroup handGroup;
    private final LoadingBar loadingBar;

    public HandStage() {
        handGroup = new HandGroup();
        handGroup.selectedColor(Palette.ACCENT);
        handGroup.activeColor(Palette.PRIMARY);
        handGroup.consumedColor(Palette.PRIMARY_DESELECTED);
        addActor(handGroup);

        loadingBar = new LoadingBar();
        loadingBar.setColor(Palette.ACCENT);
        addActor(loadingBar);
    }

    public void resize(int width, int height) {
        Camera camera = new OrthographicCamera(width, height);
        setViewport(new FitViewport(width, height, camera));
        getViewport().update(width, height, true);

        loadingBar.setSize(width, GraphicsUtil.dpToPixels(12));
        loadingBar.setY(getHeight() - loadingBar.getHeight());

        handGroup.setX(width * 0.1f);
        handGroup.setY(getHeight() - GraphicsUtil.dpToPixels(16f) - loadingBar.getHeight());
        handGroup.setSize(width * 0.8f, height * 0.8f);
        handGroup.setSpacing(GraphicsUtil.dpToPixels(8f));
        handGroup.setItemHeight(GraphicsUtil.dpToPixels(54f));
    }

    public void startCountdown(float countdown) {
        loadingBar.start(countdown);
    }

    private static class LoadingBar extends Group {
        private final DrawableActor background;
        private float duration;
        private float currentDuration;

        public LoadingBar() {
            background = new DrawableActor(new TextureDrawable(Textures.BACKGROUND));
            background.setColor(Color.OLIVE);
            addActor(background);
        }

        @Override
        protected void sizeChanged() {
            background.setWidth(getWidth());
            background.setHeight(getHeight());
        }

        public void start(float time) {
            currentDuration = time;
            duration = time;
        }

        @Override
        public void setColor(Color color) {
            background.setColor(color);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            if (currentDuration > 0) {
                background.setWidth(getWidth() * currentDuration / duration);
                currentDuration -= delta;
            } else {
                currentDuration = 0;
                background.setWidth(0);
            }
        }
    }

    private static class CardActor extends Group {
        private Action animateAction;
        private CardActor animateActor;

        private Label label;
        private DrawableActor background;

        public CardActor(String text) {
            background = new DrawableActor(new TextureDrawable(Textures.BACKGROUND));
            addActor(background);

            BitmapFont font = Fonts.get(Fonts.FONT_20);
            label = new Label(text, new Label.LabelStyle(font, Color.WHITE));
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

        public void setColor(Color backgroundColor, Color textColor) {
            this.setColor(backgroundColor);
            label.setColor(textColor);
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
        private final Set<CardActor> lockedCards = new HashSet<CardActor>();
        private final List<CardActor> cardActors = new ArrayList<CardActor>();
        private float spacing;
        private float itemHeight;
        private Color selectedColor;
        private Color activeColor;

        private Color consumedColor;

        public void addCard(final CardActor actor) {
            cardActors.add(actor);

            addActor(actor);
            actor.setColor(activeColor);
            actor.addListener(new InputListener() {

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return onCardPressed(actor, x, y);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    onCardReleased(actor);
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
            float yPos = -itemHeight;
            for (Actor actor : getChildren()) {
                actor.setWidth(getWidth());
                actor.setHeight(itemHeight);
                actor.setY(yPos);
                yPos -= actor.getHeight() + spacing;
            }
        }

        public void setItemHeight(float itemHeight) {
            this.itemHeight = itemHeight;
        }

        public void selectedColor(Color selectedColor) {
            this.selectedColor = selectedColor;
        }

        public void activeColor(Color activeColor) {
            this.activeColor = activeColor;
        }

        public void consumedColor(Color consumedColor) {
            this.consumedColor = consumedColor;
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

        private boolean onCardPressed(CardActor card, float x, float y) {
            if (lockedCards.contains(card)) {
                return false;
            }
            card.setVisible(false);

            DraggedCard draggedCard = new DraggedCard(card.copy(), y);
            addActor(draggedCard.fake);
            draggedCards.put(card, draggedCard);
            return true;
        }

        private void onCardReleased(CardActor card) {
            DraggedCard draggedCard = draggedCards.get(card);
            if (draggedCard == null) {
                // We've force released this card.
                return;
            }

            card.setVisible(true);

            removeActor(draggedCard.fake);
            draggedCards.remove(card);
        }

        private void onCardDragged(CardActor card, float x, float y) {
            if (lockedCards.contains(card)) {
                return;
            }
            DraggedCard draggedCard = draggedCards.get(card);

            draggedCard.fake.setY(card.getY() + y - draggedCard.initialY);
            Vector2 tempPos = new Vector2();

            for (CardActor other : cardActors) {
                if (card.equals(other)) {
                    continue;
                }
                if (lockedCards.contains(other)) {
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

        public void select(CardActor cardActor) {

        }

        public void lockCards() {
            setColor(ColorUtils.alpha(Color.WHITE, Constants.ALPHA_DISABLED));
            lockedCards.addAll(cardActors);
            // Drop cards if currently dragged.
            for (CardActor actor : draggedCards.keySet()) {
                onCardReleased(actor);
            }
        }

        public void lockCard(CardActor card) {
            lockedCards.add(card);
        }

        public void unlockCards() {
            lockedCards.clear();
            setColor(new Color(1f, 1f, 1f, Constants.ALPHA_ENABLED));
        }
    }

    public void addCard(BoardAction boardAction) {
        final CardActor actor = new CardActor(boardAction.getText());
        handGroup.addCard(actor);

        cardActors.add(new Pair<BoardAction, CardActor>(boardAction, actor));
    }

    public void select(BoardAction boardAction) {
        for (Pair<BoardAction, CardActor> pair : cardActors) {
            if (pair.key.getId() == boardAction.getId()) {
                pair.value.setColor(COLOR_SELECTED);
            }
        }
    }

    public void deselect(BoardAction boardAction) {
        for (Pair<BoardAction, CardActor> pair : cardActors) {
            if (pair.key.getId() == boardAction.getId()) {
                pair.value.setColor(COLOR_CONSUMED, Color.BLACK);
            }
        }
    }

    private void block(BoardAction boardAction) {
        for (Pair<BoardAction, CardActor> pair : cardActors) {
            if (pair.key.getId() == boardAction.getId()) {
                CardActor card = pair.value;
                handGroup.lockCard(card);
                card.setColor(Color.BLACK, Color.WHITE);
            }
        }
    }

    public void update() {
        handGroup.sizeChanged();
    }

    public List<BoardAction> getSortedCards() {
        // Sort cards by y.
        SortedMap<Float, BoardAction> sortedMap = new TreeMap<Float, BoardAction>(Collections.reverseOrder());
        for (Pair<BoardAction, CardActor> pair : cardActors) {
            sortedMap.put(pair.value.getY(), pair.key);
        }
        return new ArrayList<BoardAction>(sortedMap.values());
    }

    public void lockCards() {
        handGroup.lockCards();
    }

    public void blockCards(int blockedCards) {
        List<BoardAction> cards = getSortedCards();

        for (int i = cards.size() - blockedCards; i < cards.size(); i++) {
            BoardAction boardAction = cards.get(i);
            block(boardAction);
        }
    }
}
