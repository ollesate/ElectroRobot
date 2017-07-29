package olof.sjoholm.Client.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

import olof.sjoholm.Client.CardHandModel;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Skins;
import olof.sjoholm.Utils.Constants;
import olof.sjoholm.Utils.Logger;


public class HandStage extends Stage {
    private CardHandTable cardHandTable;
    private CardHandModel cardHandModel;

    private final OurTable ourTable;

    public HandStage() {
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        setViewport(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);


        ourTable = new OurTable();
        ourTable.setDebug(true);
        ourTable.setFillParent(true);
        addActor(ourTable);
        DrawableActor drawableActor = new DrawableActor(new TextureDrawable(Textures.background));
        drawableActor.setColor(Color.RED);
        drawableActor.setWidth(200);
        drawableActor.setHeight(200);
        drawableActor.setTouchable(Touchable.enabled);
        addActor(drawableActor);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Logger.d(String.format("touch down %s %s %s %s", screenX, screenY, pointer, button));
        boolean handled = super.touchDown(screenX, screenY, pointer, button);
//        Logger.d(String.format("Handled %s ", handled));
//
//        Viewport viewport = getViewport();
//        if (screenX < viewport.getScreenX()) {
//            Logger.d("ERROR: outside left side");
//        }
//        if (screenX >= viewport.getScreenX() + viewport.getScreenWidth()) {
//            Logger.d(String.format("x %s width %s", viewport.getScreenY(), viewport.getScreenWidth()));
//            Logger.d("ERROR: outside right side");
//        }
//        if (Gdx.graphics.getHeight() - screenY < viewport.getScreenY()) {
//            Logger.d("ERROR: outside top");
//        }
//        if (Gdx.graphics.getHeight() - screenY >= viewport.getScreenY() + viewport.getScreenHeight()) {
//            Logger.d(String.format("graphics height %s screen y %s screen height %s", Gdx.graphics.getHeight(), viewport.getScreenY(), viewport.getScreenWidth()));
//            Logger.d("ERROR: outside bottom");
//        }
        return handled;
    }

    private static class OurTable extends Table {

    }

    private static class CardActor extends Label {

        public CardActor(String text) {
            super(text, Skins.DEFAULT);
        }

        @Override
        public Actor hit(float x, float y, boolean touchable) {
            if (Float.isNaN(x) || Float.isNaN(y)) {
                return null;
            }
            Actor hitActor = super.hit(x, y, touchable);
            Logger.d(String.format("hit %s %s %s handled: %s", x, y, touchable, hitActor != null));
            return hitActor;
        }
    }

    public static class Card {

        public final String text;
        public Card(String text) {
            this.text = text;
        }

    }

    public void addCard(Card card) {
        ourTable.row();
        ourTable.add(new CardActor(card.text));

        CardActor actor = new CardActor(card.text);
        actor.addListener(new InputListener() {
                              @Override
                              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                  Logger.d("Actually works!!!!!!!");
                                  return super.touchDown(event, x, y, pointer, button);
                              }

                              @Override
                              public boolean handle(Event e) {
                                  if (e instanceof InputEvent) {
                                      Logger.d("message " + e.getClass().getSimpleName());
                                  }
                                  return super.handle(e);
                              }
                          }
        );
    }
}
