package olof.sjoholm.GameWorld.Client.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import olof.sjoholm.GameWorld.Utils.ScreenAdapter;

public class GameScreen extends ScreenAdapter {
    private Stage stage;

    public GameScreen() {
        stage = new Stage();
    }

    @Override
    public void show() {
        setBackgroundColor(Color.RED);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
