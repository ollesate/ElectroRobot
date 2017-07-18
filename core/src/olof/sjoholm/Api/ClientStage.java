package olof.sjoholm.Api;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class ClientStage extends Stage {

    public void connect(int timeout) {

    }

    public abstract void onConnectionFailed();

    public abstract void onConnectionSuccessful();
}
