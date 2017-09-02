package olof.sjoholm.sfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class Explosion {
    private static final ParticleEffect BLACK_SMOKE = new ParticleEffect()
            .color(Color.BLACK)
            .duration(.6f, 1.0f)
            .particles(5)
            .size(30f)
            .speed(20f, 50f);

    private static final ParticleEffect FIRE = new ParticleEffect()
            .color(Color.ORANGE)
            .duration(.15f, .25f)
            .particles(20)
            .size(20f)
            .speed(50f, 100f);

    public static Actor create(float x, float y) {
        Group effects = new Group();
        effects.setPosition(x, y);
        effects.addActor(BLACK_SMOKE.create());
        effects.addActor(FIRE.create());
        return effects;
    }

    public static Action explodeAt(final Actor targetActor) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                Group parent = targetActor.getParent();
                if (parent != null) {
                    parent.addActor(create(targetActor.getX(Align.center),
                            targetActor.getY(Align.center)));
                }
                return true;
            }
        };
    }
}
