package olof.sjoholm.sfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import olof.sjoholm.utils.objects.DrawableActor;
import olof.sjoholm.utils.graphic.TextureDrawable;
import olof.sjoholm.assets.Textures;

public class ParticleEffect {
    private IntRandomizer particles;
    private FloatRandomizer duration;
    private FloatRandomizer speed;
    private FloatRandomizer size;
    private Color color;

    private static class IntRandomizer {
        private final int min;
        private final int max;

        public IntRandomizer(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int get() {
            return MathUtils.random(min, max);
        }
    }

    private static class FloatRandomizer {
        private final float min;
        private final float max;

        public FloatRandomizer(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float get() {
            return MathUtils.random(min, max);
        }
    }

    public ParticleEffect particles(int particles) {
        return particles(particles, particles);
    }

    public ParticleEffect particles(int min, int max) {
        this.particles = new IntRandomizer(min, max);
        return this;
    }

    public ParticleEffect duration(float duration) {
        return duration(duration, duration);
    }

    public ParticleEffect duration(float min, float max) {
        duration = new FloatRandomizer(min, max);
        return this;
    }

    public ParticleEffect speed(float speed) {
        return speed(speed, speed);
    }

    public ParticleEffect speed(float min, float max) {
        speed = new FloatRandomizer(min, max);
        return this;
    }

    public ParticleEffect size(float size) {
        return size(size, size);
    }

    public ParticleEffect size(float min, float max) {
        size = new FloatRandomizer(min, max);
        return this;
    }

    public ParticleEffect color(Color color) {
        this.color = color;
        return this;
    }

    public Actor create() {
        return create(0, 0);
    }

    public Actor create(float x, float y) {
        ParticleGroup pg = new ParticleGroup(
                particles,
                duration,
                speed,
                size,
                color
        );
        pg.setPosition(x, y);
        return pg;
    }

    private static class ParticleGroup extends Group {
        List<Particle> particlesActors = new ArrayList<Particle>();

        public ParticleGroup(IntRandomizer particles, FloatRandomizer duration,
                             FloatRandomizer speed,
                             FloatRandomizer size, Color color) {

            for (int i = 0; i < particles.get(); i++) {
                Particle particle = new Particle();
                float angle = MathUtils.random(0, 360);
                Vector2 dir = new Vector2(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
                float realDuration = duration.get();
                float amount = speed.get() * realDuration;
                particle.addAction(Actions.sequence(
                        Actions.moveBy(dir.x * amount, dir.y * amount, realDuration),
                        Actions.removeActor()
                ));
                particle.addAction(Actions.fadeOut(realDuration));
                float realSize = size.get();
                particle.setSize(realSize, realSize);
                particle.setColor(color);

                addActor(particle);
                particlesActors.add(particle);
            }
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            Iterator<Particle> iterator = particlesActors.iterator();
            while (iterator.hasNext()) {
                Particle particle = iterator.next();
                if (particle.getStage() == null) {
                    iterator.remove();
                }
            }

            if (particlesActors.size() == 0) {
                remove();
            }
        }
    }

    private static class Particle extends DrawableActor {

        public Particle() {
            super(new TextureDrawable(Textures.PARTICLE));
        }
    }
}
