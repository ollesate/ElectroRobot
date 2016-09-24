package olof.sjoholm.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.GameManagers.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(
                        Constants.ViewportWidth,
                        Constants.ViewportHeight
                );
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame();
        }
}