package olof.sjoholm.game.shared.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.configuration.Constants;
import olof.sjoholm.game.shared.AppPrefs;
import olof.sjoholm.utils.ColorUtils;
import olof.sjoholm.utils.GraphicsUtil;

public class IpAddressDialog extends Group {
    private final BackgroundDrawable background;
    private final Table table;
    private DialogListener listener;
    private TextField ipTextField;

    public interface DialogListener {

        void onAccept(String ipAddress);

        void onCancel();
    }

    public IpAddressDialog() {
        background = new BackgroundDrawable(ColorUtils.alpha(Color.LIGHT_GRAY, 0.65f));
        table = new Table();
        table.background(new BackgroundDrawable(Color.WHITE));
        table.top();
        table.setHeight(GraphicsUtil.dpToPixels(250));
        addViews(table);
        addActor(table);
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Actor hitActor = hit(x, y, false);
                if (IpAddressDialog.this.equals(hitActor)) {
                    remove();
                }
                return true;
            }
        });
    }

    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    public void setText(String text) {
        System.out.println("Set text " + text);
        ipTextField.setText(text);
    }

    @Override
    protected void sizeChanged() {
        table.setX(getWidth() / 2, Align.center);
        table.setY(getHeight() / 2, Align.center);
        table.setWidth(Math.min(getWidth() - GraphicsUtil.dpToPixels(32), GraphicsUtil.dpToPixels(400)));
    }

    private void addViews(Table table) {
        final Label label = ThemedUi.createLabel(Constants.DEFAULT_THEME);
        label.setAlignment(Align.center);
        label.getStyle().font = Fonts.get(Fonts.FONT_60);
        label.setText("Ip address");
        table.add(label).growX().height(GraphicsUtil.dpToPixels(48)).padTop(GraphicsUtil.dpToPixels(16));
        table.row();
        ipTextField = ThemedUi.createTextField(Constants.DEFAULT_THEME);
        ipTextField.setMessageText("Write here");
        ipTextField.setAlignment(Align.center);
        table.add(ipTextField).growX().height(GraphicsUtil.dpToPixels(48)).padTop(GraphicsUtil.dpToPixels(16));
        table.row();

        Table tableButtons = new Table();
        tableButtons.left().bottom();
        TextButton acceptButton = ThemedUi.createTextButton(Constants.DEFAULT_THEME);
        acceptButton.setText("Accept");
        acceptButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onAccept(ipTextField.getText());
                }
            }
        });
        TextButton cancelButton = ThemedUi.createTextButton(Constants.DEFAULT_THEME);
        cancelButton.setText("Cancel");
        cancelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
        tableButtons.add(acceptButton).growX().padLeft(GraphicsUtil.dpToPixels(16))
                .padRight(GraphicsUtil.dpToPixels(16));
        tableButtons.add(cancelButton).growX().padRight(GraphicsUtil.dpToPixels(16));
        table.add(tableButtons).grow().padBottom(GraphicsUtil.dpToPixels(16));
    }

    public void dismiss() {
        remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage != null) {
            setWidth(stage.getWidth());
            setHeight(stage.getHeight());
        }
    }
}
