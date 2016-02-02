package fi.joutsijoki;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Sami on 25.1.2016.
 */
public class AudioHandler {

    public enum EFFECT {
        LIGHTNING,
        POISON
    }

    public static void playEffect(EFFECT effect) {
        switch (effect) {
            case LIGHTNING:
                lightningEffect();
                break;
            case POISON:
                poisonEffect();
                break;
        }
    }

    private static void lightningEffect() {
        Sound s = AssetLoader.lightningEffects.get(MathUtils.random(0, AssetLoader.lightningEffects.size - 1));

        s.play(0.5f);
    }

    private static void poisonEffect() {
        Sound s = AssetLoader.poisonEffect;

        s.play(0.5f);
    }
}
