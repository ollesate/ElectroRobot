package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.awt.Point;
import java.util.List;

import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.server.logic.Direction;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.ui.TextureDrawable;
import olof.sjoholm.utils.ui.objects.DrawableActor;

public class Laser extends GameBoardActor {
    private final Direction direction;
    private final LaserActor laserActor = new LaserActor();

    public Laser(Direction direction) {
        this.direction = direction;
        setDrawable(new TextureDrawable(getTexture(direction)));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private Texture getTexture(Direction direction) {
        switch (direction) {
            case LEFT:
                return Textures.LASER_LEFT;
            case UP:
                return Textures.LASER_UP;
            case RIGHT:
                return Textures.LASER_RIGHT;
            case DOWN:
                return Textures.LASER_DOWN;
            default:
                throw new IllegalStateException("Oops");
        }
    }

    public Action getAction() {
        return getLaserAnimation();
    }

    @Override
    public boolean isPassable(Direction direction) {
        return this.direction != direction;
    }

    @Override
    public boolean isExitable(Direction direction) {
        return this.direction.getOpposite() != direction;
    }

    private Action getLaserAnimation() {
        int length = 1000;
        int offset = 35;
        getParent().addActor(laserActor);
        laserActor.setRotation(direction.rotation);
        Vector2 nozzlePos = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        nozzlePos.sub(direction.getVector().scl(getWidth() / 2, getHeight() / 2));
        nozzlePos.add(direction.getVector().scl(offset, offset));
        laserActor.setBounds(nozzlePos.x, nozzlePos.y, length, 10f);

        GameBoard gameBoard = getStage().getGameBoard();
        Point pos = getGameboardPosition();
        for (int i = 0; i < 5; i++) {
            List<PlayerToken> targets = gameBoard.getActorsAt(pos, PlayerToken.class);
            if (targets.size() > 0) {
                targets.get(0).damage(1);
            }
            pos.translate(direction.dirX, direction.dirY);
        }

        return Actions.delay(1f, new Action() {
            @Override
            public boolean act(float delta) {
                laserActor.remove();
                return true;
            }
        });
    }

    private final class LaserActor extends DrawableActor {

        public LaserActor() {
            super(new TextureDrawable(Textures.BACKGROUND));
            setColor(Color.RED);
            setOrigin(0, 0);
        }
    }
}
