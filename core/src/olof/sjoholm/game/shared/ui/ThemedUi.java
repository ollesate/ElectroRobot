package olof.sjoholm.game.shared.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.utils.ColorUtils;
import olof.sjoholm.utils.GraphicsUtil;
import sun.font.TextLabel;

public class ThemedUi {

    public static <T> SelectBox<T> createSelectBox(Theme theme) {
        return new SelectBox<T>(createSelectBoxStyle(theme));
    }

    private static SelectBox.SelectBoxStyle createSelectBoxStyle(Theme theme) {
        return new SelectBox.SelectBoxStyle(theme.bitmapFont, theme.textColor, theme.background,
                createScrollPaneStyle(theme), createListStyle(theme));
    }

    public static TextButton createTextButton(Theme theme) {
        TextButton textButton = new TextButton("", createTextButtonStyle(theme));
        textButton.padTop(GraphicsUtil.dpToPixels(8));
        textButton.padBottom(GraphicsUtil.dpToPixels(8));
        return textButton;
    }

    public static Label createLabel(Theme theme) {
        return new Label("", createLabelStyle(theme));
    }

    private static Label.LabelStyle createLabelStyle(Theme theme) {
        return new Label.LabelStyle(Fonts.get(Fonts.FONT_24), theme.textColor);
    }

    private static ScrollPane.ScrollPaneStyle createScrollPaneStyle(Theme theme) {
        return new ScrollPane.ScrollPaneStyle(theme.background, null, null, null, null);
    }

    private static List.ListStyle createListStyle(Theme theme) {
        return new List.ListStyle(theme.bitmapFont, theme.textColor, theme.textColor, theme.selection);
    }

    private static TextButton.TextButtonStyle createTextButtonStyle(Theme theme) {
        Drawable up = new BackgroundDrawable(theme.primaryColor);
        Drawable down = new BackgroundDrawable(ColorUtils.alpha(theme.primaryColor, 0.75f));
        return new TextButton.TextButtonStyle(up, down, up, theme.bitmapFont);
    }

    public static TextField createTextField(Theme theme) {
        return new TextField("", createTextFieldStyle(theme));
    }

    private static TextField.TextFieldStyle createTextFieldStyle(Theme theme) {
        BitmapFont bitmapFont = Fonts.get(Fonts.FONT_24);
        Drawable cursor = new BackgroundDrawable(theme.accentColor);
        Drawable selection = new BackgroundDrawable(theme.accentColor);
        Drawable background = theme.background;
        return new TextField.TextFieldStyle(bitmapFont, theme.accentColor, cursor, selection, background);
    }

    public static class Theme {
        public final Color textColor;
        public final BitmapFont bitmapFont;
        public final Color primaryColor;
        public final Color accentColor;
        public final Drawable selection;
        public final Drawable background;

        private Theme(Color textColor, BitmapFont bitmapFont, Color primaryColor, Color accentColor, Drawable selection, Drawable background) {
            this.textColor = textColor;
            this.bitmapFont = bitmapFont;
            this.primaryColor = primaryColor;
            this.accentColor = accentColor;
            this.selection = selection;
            this.background = background;
        }

        public static Theme createLight(Color primaryColor, Color accentColor) {
            return new Builder().light().primaryColor(primaryColor).accent(accentColor).build();
        }

        public static class Builder {
            boolean lightTheme;
            Color accent;
            Color primaryColor;

            public Builder light() {
                lightTheme = true;
                return this;
            }

            public Builder black() {
                lightTheme = false;
                return this;
            }

            public Builder accent(Color color) {
                accent = color;
                return this;
            }

            public Builder primaryColor(Color color) {
                primaryColor = color;
                return this;
            }

            public Theme build() {
                Color fontColor = lightTheme ? Color.BLACK : Color.WHITE;
                Color backgroundColor = lightTheme ? Color.WHITE : Color.BLACK;
                BitmapFont bitmapFont = Fonts.get(Fonts.FONT_30);
                Drawable selection = new BackgroundDrawable(accent);
                Drawable background = new BackgroundDrawable(backgroundColor);

                return new Theme(fontColor, bitmapFont, primaryColor, accent, selection, background);
            }
        }
    }
}
