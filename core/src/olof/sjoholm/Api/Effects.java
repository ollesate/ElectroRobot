package olof.sjoholm.Api;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Effects {

    public static class MUZZLE {
        private static final ParticleEffect WHITE_SMOKE = new ParticleEffect()
                .color(Color.WHITE)
                .duration(.3f, .6f)
                .particles(10)
                .size(20f)
                .speed(20f, 50f);

        private static final ParticleEffect FIRE = new ParticleEffect()
                .color(Color.ORANGE)
                .duration(.05f, .15f)
                .particles(20)
                .size(15f)
                .speed(50f, 100f);

        public static Actor create(float x, float y) {
            Group effects = new Group();
            effects.setPosition(x, y);
            effects.addActor(WHITE_SMOKE.create());
            effects.addActor(FIRE.create());
            return effects;
        }
    }
}
