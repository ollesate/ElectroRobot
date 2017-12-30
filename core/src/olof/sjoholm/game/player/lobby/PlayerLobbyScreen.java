package olof.sjoholm.game.player.lobby;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.assets.Palette;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.player.PlayerScreen;
import olof.sjoholm.game.player.PlayerScreenHandler;
import olof.sjoholm.game.shared.AppPrefs;
import olof.sjoholm.game.shared.ui.IpAddressDialog;
import olof.sjoholm.game.shared.ui.ThemedUi;
import olof.sjoholm.utils.GraphicsUtil;
import olof.sjoholm.utils.ui.objects.DrawableActor;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.ui.TextureDrawable;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.net.Envelope;
import olof.sjoholm.utils.Logger;

public class PlayerLobbyScreen extends PlayerScreen {
    private final LobbyStage lobbyStage;
    private final PlayerScreenHandler playerScreenHandler;
    private boolean justPressed;

    public PlayerLobbyScreen(PlayerScreenHandler playerScreenHandler) {
        super();
        this.playerScreenHandler = playerScreenHandler;
        lobbyStage = new LobbyStage();
    }

    @Override
    public void render(float delta) {
        lobbyStage.act(delta);
        lobbyStage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(lobbyStage);
        lobbyStage.label.setText("Not connected");
    }

    @Override
    public void resize(int width, int height) {
        lobbyStage.getViewport().update(width, height);
    }

    @Override
    public void onDisconnected() {
        lobbyStage.label.setText("Disconnected");
    }

    @Override
    public void onConnected() {
        send(new Envelope.PlayerSelectName(lobbyStage.textField.getText()));
        send(new Envelope.PlayerSelectColor(lobbyStage.playerToken.getColor()));
    }

    @Override
    public void onConnectionFailed(String reason) {
        lobbyStage.label.setText("Connection failed");
    }

    @Override
    public void onMessage(Envelope envelope) {
        Logger.d("Player Lobby onMessage " + envelope);
        if (envelope instanceof Envelope.StartGame) {
            playerScreenHandler.showScreen(PlayerScreenHandler.GAME);
        }
    }

    private class LobbyStage extends Stage {

        private final TextField textField;
        private final PlayerToken playerToken;
        private final Label label;

        public LobbyStage() {
            super();

            Label.LabelStyle labelStyle = new Label.LabelStyle(Fonts.get(Fonts.FONT_14), Color.BLACK);
            label = new Label("", labelStyle);
            label.setAlignment(Align.center);
            label.setHeight(GraphicsUtil.dpToPixels(48));
            label.setY(getHeight() - GraphicsUtil.dpToPixels(16) - label.getHeight());
            label.setWidth(getWidth());
            addActor(label);

            TextButton connectButton = ThemedUi.createTextButton(Constants.DEFAULT_THEME);
            connectButton.setText("Connect");
            connectButton.setHeight(GraphicsUtil.dpToPixels(48));
            connectButton.setWidth(getWidth() - 2 * GraphicsUtil.dpToPixels(16));
            connectButton.setX(getWidth() / 2, Align.center);
            connectButton.setY(label.getY(Align.bottom) - GraphicsUtil.dpToPixels(8), Align.top);
            connectButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    final IpAddressDialog dialog = new IpAddressDialog();
                    dialog.setText(AppPrefs.getLastIpAddress());
                    dialog.setListener(new IpAddressDialog.DialogListener() {
                        @Override
                        public void onAccept(String ipAddress) {
                            AppPrefs.setLastIpAddress(ipAddress);
                            connect(ipAddress);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel() {
                            dialog.dismiss();
                        }
                    });
                    addActor(dialog);
                }
            });
            addActor(connectButton);

            Group group = new Group();
            addActor(group);

            Group swatchGroup = createSwatchGroup(getWidth());
            group.addActor(swatchGroup);
            sizeGroup(swatchGroup);
            swatchGroup.setX((getWidth() - swatchGroup.getWidth()) / 2);

            playerToken = new PlayerToken();
            float size = GraphicsUtil.dpToPixels(124);
            playerToken.setSize(size, size);
            playerToken.setX((getWidth() - playerToken.getWidth()) / 2);
            playerToken.setY(swatchGroup.getY(Align.top) + GraphicsUtil.dpToPixels(64));
            playerToken.setOrigin(playerToken.getWidth() / 2, playerToken.getHeight() / 2);
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

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Actor hitActor = hit(x, y, false);
                    if (textField.equals(hitActor)) {
                        justPressed = true;
                        if (textField.getText().equals(hintText)) {
                            textField.setText("");
                        }
                    } else {
                        if (textField.equals(getKeyboardFocus())) {
                            unfocus(textField);
                            Gdx.input.setOnscreenKeyboardVisible(false);
                            if (textField.getText().equals("")) {
                                textField.setText(hintText);
                            }
                        }
                        if (isConnected()) {
                            if (justPressed) {
                                send(new Envelope.PlayerSelectName(textField.getText()));
                            }
                        }
                        justPressed = false;

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

            TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle(
                    getDrawable(Palette.PRIMARY),
                    getDrawable(Palette.PRIMARY_DESELECTED),
                    getDrawable(Palette.ACCENT),
                    Fonts.get(Fonts.FONT_24)
            );
            final String notReady = "Press when ready";
            final TextButton textButton = new TextButton(notReady, tbStyle);
            textButton.setWidth(getWidth() - 2 * GraphicsUtil.dpToPixels(16));
            textButton.setHeight(GraphicsUtil.dpToPixels(40));
            textButton.setY(GraphicsUtil.dpToPixels(16));
            textButton.setX((getWidth() - textButton.getWidth()) / 2);
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (textButton.isChecked()) {
                        playerToken.setTouchable(Touchable.enabled);
                        textButton.setText("Ready");
                        playerToken.startAnimation();

                        send(new Envelope.PlayerReady(true));
                    } else {
                        playerToken.setTouchable(Touchable.disabled);
                        textButton.setText(notReady);
                        playerToken.stopAnimation();

                        send(new Envelope.PlayerReady(true));
                    }
                }
            });
            addActor(textButton);

            playerToken.setTouchable(Touchable.disabled);
            playerToken.addListener(new InputListener() {
                private Vector2 touchScreen;
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    touchScreen = new Vector2(playerToken.getWidth() / 2, playerToken.getHeight() / 2);
                    playerToken.localToStageCoordinates(touchScreen);
                    return true;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    Vector2 direction = new Vector2(event.getStageX() - touchScreen.x,
                            event.getStageY() - touchScreen.y);
                    if (direction.len() > GraphicsUtil.dpToPixels(40)) {
                        playerToken.setRotation(direction.angle());
                    }
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
            MyDrawable myDrawable = new MyDrawable(Textures.BACKGROUND);
            myDrawable.setColor(color);
            return myDrawable;
        }
    }

    private static class Swatch extends DrawableActor {
        private final Color swatchColor;

        public Swatch(Color swatchColor) {
            super(new TextureDrawable(Textures.BACKGROUND));
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
