package olof.sjoholm.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import olof.sjoholm.configuration.Constants;
import olof.sjoholm.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(
                        Constants.WORLD_WIDTH,
                        Constants.WORLD_HEIGHT
                );
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame();
        }
}