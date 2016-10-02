package olof.sjoholm.GameWorld.GameManagers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import olof.sjoholm.Interfaces.ConnectActions;

/**
 * Created by sjoholm on 02/10/16.
 */

public class LoginScreen {
    private Stage stage;
    private ConnectActions connectActions;
    private boolean isButtonDisabled;

    public LoginScreen(ConnectActions connectActions) {
        this.connectActions = connectActions;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        initialize();
    }

    public void draw() {
        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);

    }

    public void initialize() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextField nameText = new TextField("", skin);
        TextButton button = new TextButton("Connect", skin);
        button.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (isButtonDisabled) {
                    return;
                }
                connectActions.onConnectClicked();
            }
        });

        TextButton broadcast = new TextButton("Broadcast", skin);
        broadcast.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                connectActions.onBroadcastClicked();
            }
        });

        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();
        table.add("Status : ");
        table.add(nameText).width(100);
        table.row();
        table.add(button).width(100);
        table.add(broadcast).width(100);

        stage.addActor(table);
    }

    public void disableButton() {
        isButtonDisabled = true;
    }
}
