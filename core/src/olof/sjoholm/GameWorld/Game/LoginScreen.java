package olof.sjoholm.GameWorld.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.smartcardio.Card;

import olof.sjoholm.GameWorld.Assets.Textures;
import olof.sjoholm.GameWorld.Utils.Backgrounds;
import olof.sjoholm.GameWorld.Utils.Constants;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.GameWorld.Utils.ScreenAdapter;

import static com.badlogic.gdx.math.Plane.PlaneSide.Back;


public class LoginScreen extends ScreenAdapter {
    private LoginActions connectActions;
    private Stage stage;

    public LoginScreen(LoginActions connectActions) {
        this.connectActions = connectActions;
        Camera camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));
    }

    public void initializeLayout() {
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton startServer = new TextButton("Start server", skin);
        startServer.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                connectActions.onStartServer();
            }
        });

        TextButton startClient = new TextButton("Start client", skin);
        startClient.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                connectActions.onStartClient();
            }
        });

        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();
        table.row();
        table.add(startServer).width(100);
        table.add(startClient).width(100);

        MyCardHand cardHand = new MyCardHand();
        cardHand.addCard(new MyCard());
        cardHand.addCard(new MyCard());
        cardHand.addCard(new MyCard());

        stage.addActor(cardHand);
        stage.addActor(table);
    }

    private static class MyCard extends Table {
        private static final float PADDING = 10f;
        private Texture texture;

        public MyCard() {
            BitmapFont bitmapFont = new BitmapFont();
            texture = Textures.rotate_left;

            setDebug(true);
            pad(PADDING);
            top();
            left();
            row();
            add().expandX().height(100f);
            row().spaceBottom(20f).padTop(20f);
            add(card).growX().height(300 - PADDING * 2).center();
            row();
            add().grow();
            row();
            add().growX().height(100f);
        }

        private Widget card = new Widget(){
            private SpriteDrawable background;

            {
                setDebug(true);
                background = Backgrounds.create(new Color(0.35f, 0.35f, 0.35f, 0.35f));
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                background.draw(batch, getX(), getY(), getWidth(), getHeight());
                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
            }

        };
    }

    private static class MyCardHand extends Table {
        private static final float HORIZONTAL_SPACING = 45f;
        private static final float VERTICAL_PADDING = HORIZONTAL_SPACING * 1.5f;

        public MyCardHand() {
            setDebug(true);
            padRight(HORIZONTAL_SPACING);
            padBottom(VERTICAL_PADDING);
            padTop(VERTICAL_PADDING);
            setWidth(Constants.WORLD_WIDTH);
            setHeight(Constants.WORLD_HEIGHT);
            left();
            top();
        }

        public void addCard(MyCard card) {
            addColumn(card);
        }

        private void addColumn(MyCard card) {
            add(card).width(300f).height(600f).growY().padLeft(20).top();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        initializeLayout();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public interface LoginActions {

        void onStartServer();

        void onStartClient();

    }
}
