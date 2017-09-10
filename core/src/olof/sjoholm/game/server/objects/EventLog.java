package olof.sjoholm.game.server.objects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.assets.Fonts;
import olof.sjoholm.utils.ui.objects.LabelActor;
import olof.sjoholm.utils.ui.objects.LinearLayout;

public class EventLog extends LinearLayout {
    private final BitmapFont fontSmall;
    private final BitmapFont fontBig;
    private LabelActor prevLabel;
    private List<TextAction> textActions = new ArrayList<TextAction>();

    public static abstract class TextAction {
        private LabelActor label;

        void setLabel(LabelActor label) {
            this.label = label;
        }

        public abstract String onTick(float delta);

        public void update(float delta) {
            String text = onTick(delta);
            label.setText(text);
        }
    }

    public EventLog() {
        fontSmall = Fonts.get(Fonts.FONT_34);
        fontBig = Fonts.get(Fonts.FONT_60);
    }

    public void addEventAction(final TextAction textAction, float duration) {
        if (prevLabel != null) {
            prevLabel.setBitmapFont(fontSmall);
        }

        LabelActor label = createLabel("Text for measure");
        textAction.setLabel(label);
        textActions.add(textAction);
        addActorAt(0, label);

        SequenceAction lifetimeSequence = getLifetimeSequence(duration);
        lifetimeSequence.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                textActions.remove(textAction);
                return true;
            }
        });

        label.addAction(lifetimeSequence);

        prevLabel = label;
    }

    public void addEventText(String text, float duration) {
        if (prevLabel != null) {
            prevLabel.setBitmapFont(fontSmall);
        }

        LabelActor labelActor = createLabel(text);
        addActorAt(0, labelActor);

        labelActor.addAction(getLifetimeSequence(duration));
        prevLabel = labelActor;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (TextAction textAction : textActions) {
            textAction.update(delta);
        }
    }

    private LabelActor createLabel(String text) {
        LabelActor labelActor = new LabelActor(text, fontBig);
        labelActor.setAlignment(Align.center);
        return labelActor;
    }

    private SequenceAction getLifetimeSequence(float duration) {
        return Actions.sequence(
                Actions.delay(duration),
                Actions.fadeOut(1f, Interpolation.circle),
                Actions.removeActor()
        );
    }
}
