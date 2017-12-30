package olof.sjoholm.game.player;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.assets.Skins;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.shared.AppPrefs;
import olof.sjoholm.utils.Logger;

public class PlayerGame extends Game {
    private static final boolean debug = false;

    @Override
    public void create () {
        Logger.d("Game create");
        Textures.initialize();
        Skins.initialize();
        Fonts.initialize();
        PlayerScreenHandler screenHandler = new PlayerScreenHandler(this);
        screenHandler.showScreen(PlayerScreenHandler.LOBBY);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose () {
        super.dispose();
        Textures.dispose();
        Skins.dispose();
        AppPrefs.save();
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Logger.d("Set screen " + screen.getClass().getSimpleName());
    }
}
