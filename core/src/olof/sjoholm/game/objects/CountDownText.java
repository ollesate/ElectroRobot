package olof.sjoholm.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.Locale;

public class CountDownText extends Table {
    private final TextField textField;
    private float countdown;

    public CountDownText() {
        setWidth(Gdx.graphics.getWidth());
        setHeight(Gdx.graphics.getHeight());

        textField = new TextField("", new Skin(Gdx.files.internal("skin/uiskin.json")));
        setFillParent(true);
        top();
        add(textField).width(Gdx.graphics.getWidth() * 0.75f).center();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (countdown > 0) {
            countdown -= delta;
            int seconds = (int) countdown;
            textField.setText(
                    String.format(Locale.ENGLISH, "Round starts in %s seconds.", seconds)
            );
        } else {
            // TODO: animate out
            remove();
        }
    }

    public void startCountDown(float seconds) {
        countdown = seconds;
    }
}
