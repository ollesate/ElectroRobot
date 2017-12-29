package olof.sjoholm.game.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

import olof.sjoholm.utils.ScreenAdapter;

public class GameFinishedScreen extends ScreenAdapter {
    private final String winner;
    private TextField textField;
    private SpriteBatch batch;

    public GameFinishedScreen(String winner) {
        this.winner = winner;
    }

    @Override
    public void show() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        setBackgroundColor(Color.GREEN);
        textField = new TextField(winner + " is the winner!", skin);
        batch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        textField.setX(width / 2, Align.center);
        textField.setY(height / 2, Align.center);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        textField.draw(batch, 1f);
        batch.end();
    }
}
