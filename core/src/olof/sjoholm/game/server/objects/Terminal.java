package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.game.server.TerminalEvent;
import olof.sjoholm.game.shared.ui.BackgroundActor;
import olof.sjoholm.game.shared.ui.ColorDrawable;

public class Terminal extends Group {
    private static final int MARGIN_SIDE = 15;
    private static final int MARGIN_TEXT_FIELD = 20;
    private static final int CURSOR_WIDTH = 5;
    private static final int CURSOR_HEIGHT = 100;
    private static final int TEXT_FIELD_HEIGHT = 50;
    private static final String FONT_TF_RESOURCE = Fonts.FONT_34;
    private static final Color FONT_COLOR = Color.GREEN;
    private static final Color CURSOR_COLOR = Color.WHITE;
    private static final Color SELECTION_COLOR = Color.WHITE;

    private final TextField textField;
    private final BackgroundActor background;
    private final TextArea textArea;

    public Terminal() {
        background = new BackgroundActor(new ColorDrawable(Color.DARK_GRAY));
        addActor(background);

        Drawable cursor = new ColorDrawable(CURSOR_COLOR);
        cursor.setMinWidth(CURSOR_WIDTH);
        cursor.setMinHeight(CURSOR_HEIGHT);
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle(
                Fonts.get(FONT_TF_RESOURCE),
                FONT_COLOR,
                cursor,
                new ColorDrawable(SELECTION_COLOR),
                null
        );
        textField = new TextField("", tfStyle);
        addActor(textField);
        addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.ENTER:
                        onEnterPressed();
                        break;
                    case Input.Keys.UP:

                        break;
                }
                return false;
            }
        });

        textArea = new TextArea("", tfStyle);
        textArea.setDisabled(true);
        addActor(textArea);
    }

    private void onEnterPressed() {
        String text = textField.getText();
        textField.setText("");
        String[] args = text.trim().split(" ");
        if (args.length > 0) {
            fire(new TerminalEvent(args));
        }
        textArea.appendText(text + "\n");
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        textField.setX(MARGIN_SIDE);
        textField.setWidth(getWidth() - MARGIN_SIDE * 2);
        textField.setHeight(TEXT_FIELD_HEIGHT);
        textField.setY(getHeight() - textField.getHeight());
        textArea.setWidth(getWidth() - MARGIN_SIDE * 2);
        textArea.setHeight(getHeight() - TEXT_FIELD_HEIGHT - MARGIN_TEXT_FIELD);
        textArea.setX(MARGIN_SIDE);
        textArea.setY(0);

        background.setWidth(getWidth());
        background.setHeight(getHeight());
    }

    public void setFocus(boolean focus) {
        if (focus) {
            getStage().setKeyboardFocus(textField);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    textField.setDisabled(false);
                }
            });
        } else {
            getStage().setKeyboardFocus(null);
            textField.setDisabled(true);
        }
    }

    public void onError(String message) {
        textArea.appendText(message + "\n");
    }

    public boolean hasFocus() {
        return getStage().getKeyboardFocus() == textField;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, 0.85f);
    }
}
