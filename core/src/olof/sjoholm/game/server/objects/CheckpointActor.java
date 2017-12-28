package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.assets.Textures;
import olof.sjoholm.game.shared.objects.PlayerToken;
import olof.sjoholm.utils.ColorUtils;
import olof.sjoholm.utils.ui.TextureDrawable;

public class CheckpointActor extends GameBoardActor implements SpawnPoint {
    private final BitmapFont bitmapFont;
    private final int checkpointNUmber;

    public CheckpointActor(int checkpointNumber) {
        this.checkpointNUmber = checkpointNumber;
        bitmapFont = Fonts.get(Fonts.FONT_60);
        setDrawable(new TextureDrawable(Textures.BACKGROUND));
        setColor(1f, 1f, 1f, 0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float spaceWidth = bitmapFont.getSpaceWidth();
        float lineHeight = bitmapFont.getLineHeight();
        bitmapFont.draw(batch, String.valueOf(checkpointNUmber), getX() + getWidth() / 2 - spaceWidth,
                getY() + getHeight() / 2  + lineHeight / 4);
    }

    public Action getAction() {
        return new CheckpointAction();
    }

    public int getCheckpointNumber() {
        return checkpointNUmber;
    }

    @Override
    public int getBoardX() {
        return getGameboardPosition().x;
    }

    @Override
    public int getBoardY() {
        return getGameboardPosition().y;
    }

    private final class CheckpointAction extends Action {
        private Action action;

        @Override
        public boolean act(float delta) {
            if (action == null) {
                action = getInternalAction();
                action.setActor(CheckpointActor.this);
            }
            return action.act(delta);
        }

        private Action getInternalAction() {
            return new Action() {
                @Override
                public boolean act(float delta) {
                    for (PlayerToken token : getStage().getGameBoard().getActorsAt(getGameboardPosition(),
                            PlayerToken.class)) {
                        System.out.println("Add checkpoint to player token!");
                        token.addCheckpoint(checkpointNUmber);
                        setColor(ColorUtils.alpha(token.getColor(), 0.45f));
                    }
                    return true;
                }
            };
        }
    }
}
