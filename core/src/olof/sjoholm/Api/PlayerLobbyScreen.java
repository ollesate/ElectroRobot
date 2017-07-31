package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import olof.sjoholm.Client.stages.DrawableActor;
import olof.sjoholm.GameWorld.Actors.PlayerToken;
import olof.sjoholm.GameWorld.Assets.TextureDrawable;
import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;

public class PlayerLobbyScreen extends PlayerScreen {
    private final LobbyStage lobbyStage;

    public PlayerLobbyScreen() {
        lobbyStage = new LobbyStage();
        connect();
    }

    @Override
    public void render(float delta) {
        lobbyStage.act(delta);
        lobbyStage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(lobbyStage);
    }

    @Override
    public void resize(int width, int height) {
        lobbyStage.getViewport().update(width, height);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnected() {
        send(new Envelope.PlayerSelectName(lobbyStage.textField.getText()));
        send(new Envelope.PlayerSelectColor(lobbyStage.playerToken.getColor()));
    }

    @Override
    public void onConnectionFailed(String reason) {

    }

    @Override
    public void onMessage(Envelope envelope) {

    }

    private class LobbyStage extends Stage {

        private final TextField textField;
        private final PlayerToken playerToken;

        public LobbyStage() {
            super();
            Group group = new Group();
            addActor(group);

            Group swatchGroup = createSwatchGroup(getWidth());
            group.addActor(swatchGroup);
            sizeGroup(swatchGroup);
            swatchGroup.setX((getWidth() - swatchGroup.getWidth()) / 2);
            Logger.d("Group " + swatchGroup.getHeight());

            playerToken = new PlayerToken();
            float size = GraphicsUtil.dpToPixels(124);
            playerToken.setSize(size, size);
            playerToken.setX((getWidth() - playerToken.getWidth()) / 2);
            playerToken.setY(swatchGroup.getY(Align.top) + GraphicsUtil.dpToPixels(64));
            group.addActor(playerToken);

            TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle(
                    Fonts.get(Fonts.FONT_34),
                    Palette.PRIMARY,
                    getDrawable(Palette.ACCENT),
                    getDrawable(Palette.ACCENT),
                    getDrawable(Color.WHITE)
            );
            final String hintText = "Name";
            textField = new TextField(hintText, tfStyle);
            textField.setAlignment(Align.center);
            textField.setWidth(getWidth() - 2 * GraphicsUtil.dpToPixels(16));
            textField.setX(GraphicsUtil.dpToPixels(16));
            textField.setY(playerToken.getY(Align.top) + GraphicsUtil.dpToPixels(16));
            textField.setColor(Color.ORANGE);
            group.addActor(textField);
            
            sizeGroup(group);
            group.setY((getHeight() - group.getHeight()) / 2);
            Logger.d("Height " + group.getHeight());
            Logger.d("Stage hieght " + getHeight());


            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Actor hitActor = hit(x, y, false);
                    Logger.d("Hit " + hitActor);
                    if (textField.equals(hitActor)) {
                        if (textField.getText().equals(hintText)) {
                            textField.setText("");
                        }
                    } else {
                        unfocus(textField);
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        if (textField.getText().equals("")) {
                            textField.setText(hintText);
                        } else {
                            if (isConnected()) {
                                send(new Envelope.PlayerSelectName(textField.getText()));
                            }
                        }

                        if (hitActor != null && hitActor instanceof Swatch) {
                            Color swatchColor = ((Swatch) hitActor).getSwatchColor();
                            playerToken.setColor(swatchColor);
                            if (isConnected()) {
                                send(new Envelope.PlayerSelectColor(swatchColor));
                            }
                        }
                    }
                    return false;
                }
            });
        }

        private void sizeGroup(Group group) {
            float highest = Float.MIN_VALUE;
            float lowest = Float.MAX_VALUE;
            for (Actor actor : group.getChildren()) {
                if (actor.getY() + actor.getHeight() > highest) {
                    highest = actor.getY() + actor.getHeight();
                }
                if (actor.getY() < lowest) {
                    lowest = actor.getY();
                }
            }
            group.setHeight(highest - lowest);

            highest = Float.MIN_VALUE;
            lowest = Float.MAX_VALUE;
            for (Actor actor : group.getChildren()) {
                if (actor.getX() + actor.getWidth() > highest) {
                    highest = actor.getX() + actor.getWidth();
                }
                if (actor.getX() < lowest) {
                    lowest = actor.getX();
                }
            }
            group.setWidth(highest - lowest);
        }

        private Group createSwatchGroup(float totalWidth) {
            Group group = new Group();
            float size = GraphicsUtil.dpToPixels(32f);
            float spacing = GraphicsUtil.dpToPixels(16f);
            List<Color> swatchColors = new ArrayList<Color>();
            swatchColors.add(Color.BLACK);
            swatchColors.add(Color.ORANGE);
            swatchColors.add(Color.CORAL);
            swatchColors.add(Color.GREEN);
            swatchColors.add(Color.DARK_GRAY);
            swatchColors.add(Color.GOLD);
            swatchColors.add(Color.ROYAL);
            swatchColors.add(Color.BROWN);
            swatchColors.add(Color.YELLOW);
            swatchColors.add(Color.ORANGE);

            Iterator<Color> iterator = swatchColors.iterator();
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (!iterator.hasNext()) {
                        break;
                    }
                    Color color = iterator.next();
                    iterator.remove();
                    Swatch swatch = new Swatch(color);
                    swatch.setX(col * (size + spacing));
                    swatch.setY(row * (size + spacing));
                    swatch.setSize(size, size);
                    group.addActor(swatch);
                }
            }
            return group;
        }

        private Drawable getDrawable(Color color) {
            MyDrawable myDrawable = new MyDrawable(Textures.background);
            myDrawable.setColor(color);
            return myDrawable;
        }
    }

    private static class Swatch extends DrawableActor {
        private final Color swatchColor;

        public Swatch(Color swatchColor) {
            super(new TextureDrawable(Textures.background));
            this.swatchColor = swatchColor;
            setColor(swatchColor);
        }

        public Color getSwatchColor() {
            return swatchColor;
        }
    }

    private static class MyDrawable extends BaseDrawable {
        private final Texture texture;
        private Color color;

        public MyDrawable(Texture texture) {
            this.texture = texture;
            setMinWidth(texture.getWidth());
            setMinHeight(texture.getHeight());
        }

        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            batch.setColor(color);
            batch.draw(texture, x, y, width, height);
        }
    }
}
