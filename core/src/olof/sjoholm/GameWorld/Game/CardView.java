package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Backgrounds;
import olof.sjoholm.GameWorld.Utils.Logger;

public class CardView extends Table {
    private static final float PADDING = 10f;
    private Texture actionTexture;
    private SpriteDrawable background;
    private Color backgroundColor;
    private final Label powerLabel;
    private final Label titleLabel;

    public CardView() {
        actionTexture = Textures.rotate_left;

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        titleLabel = new Label("Title", skin);
        titleLabel.setFontScale(3.5f);

        powerLabel = new Label("" + (int)(Math.random() * 5), skin);
        powerLabel.setFontScale(6f);

        pad(PADDING);
        top();
        left();
        row();
        add(titleLabel).expandX();
        row().spaceBottom(20f);
        add(card).growX().height(300 - PADDING * 2).center();
        row();
        add(powerLabel).expand().center();

        background = Backgrounds.create(Color.WHITE);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setPowerText(String text) {
        powerLabel.setText(text);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(backgroundColor);
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        batch.setColor(getColor());

        super.draw(batch, parentAlpha);
    }

    private Widget card = new Widget() {
        private SpriteDrawable background;

        {
            setDebug(true);
            background = Backgrounds.create(new Color(0.35f, 0.35f, 0.35f, 0.35f));
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            background.draw(batch, getX(), getY(), getWidth(), getHeight());
            batch.draw(actionTexture, getX(), getY(), getWidth(), getHeight());
        }

    };

    public void setActionTexture(Texture texture) {
        actionTexture = texture;
    }
}
