package olof.sjoholm.Client.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

        DrawableActor drawableActor = new DrawableActor(new TextureDrawable(Textures.background));
        drawableActor.setColor(Color.RED);
        drawableActor.setWidth(200);
        drawableActor.setHeight(200);
        drawableActor.setTouchable(Touchable.enabled);
        addActor(drawableActor);

        handGroup = new HandGroup();
        handGroup.setX(getWidth() / 2);
        handGroup.setY(getHeight() / 2);
        addActor(handGroup);
    }

    private static class CardActor extends Label {

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

        public void swap(CardActor other) {
            StringBuilder temp = other.getText();
            other.setText(getText());
            setText(temp);
        }
    }

    public static class Card {

        public final String text;
        public Card(String text) {
            this.text = text;
        }

    }

    float y = 500;

    private static class HandGroup extends Group {
        private static final float ySpacing = 200;
        private final List<CardActor> cardActors = new ArrayList<CardActor>();
        private float currentYPos;
        private CardActor draggedObject;
        private CardActor swapObject;

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
            Logger.d("onCardPressed");
            card.setVisible(false);
            draggedObject = card.copy();
            addActor(draggedObject);
        }

        private void onCardReleased(CardActor card, float x, float y) {
            Logger.d("onCardReleased");
            card.setVisible(true);
            removeActor(draggedObject);
        }

        private void onCardDragged(CardActor card, float x, float y) {
            draggedObject.setX(card.getX() + x);
            draggedObject.setY(card.getY() + y);

            Logger.d("onCardDragged " + x + " " + y);
            int index = cardActors.indexOf(card);

            swapObject = null;
            if (y > 0) { // dragged up
                for (int i = 0; i < index; i++) {
                    Logger.d("Check index " + i);
                    CardActor other = cardActors.get(i);
                    float distance = Math.abs(other.getY() - card.getY());
                    if (y > distance) {
                        swapObject = other;
                        card.setColor(Color.YELLOW);
                        other.setColor(Color.GREEN);
                    }
                }
            } else { // dragged down
                for (int i = cardActors.size() - 1; i > index; i--) {
                    CardActor other = cardActors.get(i);
                    float distance = Math.abs(other.getY() - card.getY());
                    if (Math.abs(y) > distance) {
                        swapObject = other;
                        card.setColor(Color.ORANGE);
                        other.setColor(Color.GREEN);
                    }
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
