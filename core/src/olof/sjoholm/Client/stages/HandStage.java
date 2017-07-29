package olof.sjoholm.Client.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Skins;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;


public class HandStage extends Stage {
    private static final float CARD_WIDTH = 400f;
    private static final float CARD_HEIGHT = 100f;

    private CardHandTable cardHandTable;
    private CardHandModel cardHandModel;

    private final HandGroup handGroup;


    public HandStage() {
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        setViewport(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
        getViewport().update(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, true);

        handGroup = new HandGroup();
        handGroup.setX((getWidth() - CARD_WIDTH) / 2);
        handGroup.setY(getHeight() / 2);
        addActor(handGroup);
    }

    private static class CardActor extends Group {
        private Action animateAction;
        private CardActor animateActor;

        private Label label;
        private DrawableActor background;

        public CardActor(String text, float width, float height) {
            setWidth(width);
            setHeight(height);

            background = new DrawableActor(new TextureDrawable(Textures.background));
            background.setColor(Color.GREEN);
            background.setWidth(getWidth());
            background.setHeight(getHeight());
            addActor(background);

            label = new Label(text, Skins.DEFAULT);
            label.setX((getWidth() - label.getWidth()) / 2);
            label.setY((getHeight() - label.getHeight()) / 2);
            addActor(label);
        }

        public CardActor copy() {
            CardActor actor = new CardActor(label.getText().toString(), getWidth(), getHeight());
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

        private final Action finishAction = new Action() {
            @Override
            public boolean act(float delta) {
                setVisible(true);
                animateActor.setVisible(false);
                return true;
            }
        };
    }

    public static class Card {

        public final String text;
        public Card(String text) {
            this.text = text;
        }

    }

    private static class HandGroup extends Group {
        private static final float ySpacing = 25;
        private final List<CardActor> cardActors = new ArrayList<CardActor>();
        private final Vector2 cardPressedPos = new Vector2();
        private float currentYPos;
        private CardActor draggedObject;
        private CardActor draggedObjectGhost;

        public void addCard(final CardActor actor) {
            actor.setY(currentYPos);
            currentYPos -= actor.getHeight() + ySpacing;

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


        private void onCardPressed(CardActor card, float x, float y) {
            Logger.d("onCardPressed " + x + " " + y);
            draggedObject = card;
            draggedObject.setVisible(false);
            draggedObjectGhost = card.copy();

            cardPressedPos.set(x, y);
            addActor(draggedObjectGhost);
        }

        private void onCardReleased(CardActor card, float x, float y) {
            Logger.d("onCardReleased " + x + " " + y);
            draggedObject.setVisible(true);
            removeActor(draggedObjectGhost);
        }

        private void onCardDragged(CardActor card, float x, float y) {
            draggedObjectGhost.setY(draggedObject.getY() + y - cardPressedPos.y);
            Vector2 tempPos = new Vector2();

            for (CardActor other : cardActors) {
                if (card.equals(other)) {
                    continue;
                }

                float yDir = draggedObjectGhost.getY() - draggedObject.getY();
                float yPos = draggedObjectGhost.getY();
                if (yDir > 0 && draggedObject.getY() < other.getY() && yPos > other.getY()) {
                    // Dragged up. Swap position.
                    // Do this in the class instead. Animate to: set pos directly, but animate. :)
                    tempPos.set(other.getX(), other.getY());
                    other.animate(draggedObject.getX(), draggedObject.getY());
                    draggedObject.setPosition(tempPos.x, tempPos.y);
                    break;
                } else if (yDir < 0 && draggedObject.getY() > other.getY() && yPos < other.getY()) {
                    // Dragged down. Swap position.
                    tempPos.set(other.getX(), other.getY());
                    other.animate(draggedObject.getX(), draggedObject.getY());
                    draggedObject.setPosition(tempPos.x, tempPos.y);
                    break;
                }
            }
        }
    }

    public void addCard(final Card card) {
        final CardActor actor = new CardActor(card.text, CARD_WIDTH, CARD_HEIGHT);
        actor.setColor(Color.RED);
        handGroup.addCard(actor);
    }
}
