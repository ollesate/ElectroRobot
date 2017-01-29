package olof.sjoholm.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Assets {

    public static class Fonts {
        public static BitmapFont bitmapFont_12;


        public static void load() {
            FreeTypeFontGenerator generator =
                    new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 12;
            BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose(); // don't forget to dispose to avoid memory leaks!
        }

        public static BitmapFont generateFont(int size) {
            FreeTypeFontGenerator generator =
                    new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Regular.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose(); // don't forget to dispose to avoid memory leaks!
            return font;
        }

    }
}
