package olof.sjoholm.GameWorld.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;

import olof.sjoholm.Utils.Backgrounds;

public class CardView extends Table {
    private static final float DEFAULT_PADDING = 10f;
    private static final float TITLE_SCALE = 1.2f;
    private static final float POWER_SCALE = 2f;
    private SpriteDrawable background;
    private Color backgroundColor;
    private final Label powerLabel;
    private final Label titleLabel;
    private float minWidth;
    private float minHeight;
    private float padding;
    private boolean initializedLayout;

    public CardView(Resource resource) {
        this(resource, DEFAULT_PADDING);
    }

    public CardView(Resource resource, float padding) {
        this.padding = padding;
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        titleLabel = new Label("Title", skin);
        titleLabel.setFontScale(TITLE_SCALE);

        powerLabel = new Label("" + (int)(Math.random() * 5), skin);
        powerLabel.setFontScale(POWER_SCALE);

        background = Backgrounds.create(Color.WHITE);

        setActionTexture(resource.texture);
        setPowerText(resource.powerText);
        setTitle(resource.title);

        initializeLayout(padding);
    }

    private void initializeLayout(float padding) {
        pad(padding);
        top();
        left();
        row();
        add(titleLabel).expandX();
        row();
        add(card).grow();
        row();
        add(powerLabel);
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public void setMinSize(float width, float height, float fontScale) {
        minWidth = width;
        minHeight = height;
        titleLabel.setFontScale(TITLE_SCALE * fontScale);
        powerLabel.setFontScale(POWER_SCALE * fontScale);
    }

    @Override
    public void layout() {
        if (!initializedLayout) {
            initializeLayout(padding);
            initializedLayout = true;
        }
        super.layout();
    }

    @Override
    public float getMinWidth() {
        return minWidth != 0 ? minWidth : super.getMinHeight();
    }

    @Override
    public float getMinHeight() {
        return minHeight != 0 ? minHeight : super.getMinHeight();
    }

    @Override
    public float getPrefWidth() {
        return minWidth;
    }

    @Override
    public float getWidth() {
        return minWidth;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        // Ignore regular hit matching having to do with children, only focus on bounds
        return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this : null;
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

    private Image card = new Image() {
        private SpriteDrawable background;

        {
            setScaling(Scaling.fit);
            background = Backgrounds.create(new Color(0.35f, 0.35f, 0.35f, 0.35f));
        }
    };

    public void setActionTexture(Texture texture) {
        Sprite sprite = new Sprite(texture);
        SpriteDrawable spriteDrawable = new SpriteDrawable(sprite);
        card.setDrawable(spriteDrawable);
    }

    public static class Resource {
        public Texture texture;
        public String powerText;
        public String title;
    }
}
