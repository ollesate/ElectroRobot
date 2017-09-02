package olof.sjoholm.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import olof.sjoholm.utils.ScreenAdapter;
import olof.sjoholm.configuration.Constants;


public class LoginScreen extends ScreenAdapter {
    private LoginActions connectActions;
    private Stage stage;

    public LoginScreen(LoginActions connectActions) {
        this.connectActions = connectActions;
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
    }

    public void initializeLayout() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton startServer = new TextButton("Start server", skin);
        startServer.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                connectActions.onStartServer();
            }
        });

        TextButton startClient = new TextButton("Start client", skin);
        startClient.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                connectActions.onStartClient();
            }
        });

        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();
        table.row();
        table.add(startServer).width(100);
        table.add(startClient).width(100);

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        initializeLayout();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public interface LoginActions {

        void onStartServer();

        void onStartClient();

    }
}
