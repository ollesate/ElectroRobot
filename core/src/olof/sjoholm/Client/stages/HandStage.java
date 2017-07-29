package olof.sjoholm.Client.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.StringBuilder;
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
    private CardHandTable cardHandTable;
    private CardHandModel cardHandModel;

    private final HandGroup handGroup;


    public HandStage() {
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        setViewport(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
        getViewport().update(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, true);

        handGroup = new HandGroup();
        handGroup.setX(getWidth() / 2);
        handGroup.setY(getHeight() / 2);
        addActor(handGroup);
    }

    private static class CardActor extends Label {
        private Action animateAction;
        private CardActor animateActor;

        public CardActor(String text) {
            super(text, Skins.DEFAULT);
        }

        public CardActor copy() {
            CardActor actor = new CardActor(getText().toString());
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
        private static final float ySpacing = 200;
        private final List<CardActor> cardActors = new ArrayList<CardActor>();
        private final Vector2 cardPressedPos = new Vector2();
        private float currentYPos;
        private CardActor draggedObject;
        private CardActor draggedObjectGhost;

        public void addCard(final CardActor actor) {
            actor.setY(currentYPos);
            currentYPos -= ySpacing;

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

            cardPressedPos.set(draggedObject.getX(), draggedObject.getY());
            addActor(draggedObjectGhost);
        }

        private void onCardReleased(CardActor card, float x, float y) {
            Logger.d("onCardReleased " + x + " " + y);
            draggedObject.setVisible(true);
            removeActor(draggedObjectGhost);
        }

        private void onCardDragged(CardActor card, float x, float y) {
            Vector2 tempPos = new Vector2();
            draggedObjectGhost.setY(draggedObject.getY() + y);

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
        final CardActor actor = new CardActor(card.text);
        actor.setColor(Color.RED);
        handGroup.addCard(actor);
    }
}
