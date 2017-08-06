package olof.sjoholm.Api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class Fonts {
    private static String RIFFICFREE = "fonts/OpenSans-Regular.ttf";

    public static final String FONT_60 = "FONT_60.ttf";
    public static final String FONT_34 = "FONT_34.ttf";
    public static final String FONT_24 = "FONT_24.ttf";
    public static final String FONT_20 = "FONT_20.ttf";
    public static final String FONT_16 = "FONT_16.ttf";
    public static final String FONT_14 = "FONT_14.ttf";
    public static final String FONT_12 = "FONT_12.ttf";
    private static AssetManager manager;

    public static void initialize() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager = new AssetManager();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        loadRifficFreeFont(60, FONT_60);
        loadRifficFreeFont(34, FONT_34);
        loadRifficFreeFont(24, FONT_24);
        loadRifficFreeFont(20, FONT_20);
        loadRifficFreeFont(16, FONT_16);
        loadRifficFreeFont(14, FONT_14);
        loadRifficFreeFont(12, FONT_12);

        manager.finishLoading();
    }
    
    public static BitmapFont get(String name) {
        return manager.get(name);
    }

    private static void loadRifficFreeFont(int size, String fontName) {
        // Sizes taken from: http://gamedev.stackexchange.com/questions/77658/how-to-match-font-size-with-screen-resolution
                /*
                 the above link is not valid since we use a viewport with fixed size. One for high dpi phones (highres) and one for low dpi phones (lowres)
                 instead we use a fixed density/scale factor of 2.5. This will firther be scaled up/down by libgdx using viewports for our scenes.

                 This was primarily done to solve a bug where devices with very high dpi (density 4.0) had really big text.
                  */
        float density = Gdx.graphics.getDensity();

        FreetypeFontLoader.FreeTypeFontLoaderParameter parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontParameters.size = (int) (size * density);
        parameter.fontFileName = RIFFICFREE;

        manager.load(fontName, BitmapFont.class, parameter);
    }
}
