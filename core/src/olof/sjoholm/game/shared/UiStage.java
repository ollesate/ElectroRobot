package olof.sjoholm.game.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class UiStage extends Stage {

    public UiStage() {
        super();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        System.out.println("Widt and height " + width + ", " + height);
        OrthographicCamera camera = new OrthographicCamera(width, height);
        camera.position.set(width / 2, height / 2, 0);
        setViewport(new ScalingViewport(Scaling.none, width, height, camera));
    }
}
