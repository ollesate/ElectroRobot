package olof.sjoholm.Api;

import java.util.HashMap;
import java.util.Map;

import olof.sjoholm.Utils.Logger;

public class Config {
    public static final String STAGE_CARD_TURN_DURATION = "STAGE_CARD_TURN_DURATION";
    public static final String CARD_DELAY_NEXT = "CARD_DELAY_NEXT";
    public static final String CARD_STEP_DURATION = "CARD_STEP_DURATION";
    public static final String CARD_STEP_DELAY = "CARD_STEP_DELAY";

    private static final Config sConfig = new Config();

    private final Map<String, Object> mConfigs;
    private final Map<String, Object> mDefaultValues = new HashMap<String, Object>();

    public Config() {
        mDefaultValues.put(STAGE_CARD_TURN_DURATION, 1f);
        mDefaultValues.put(CARD_DELAY_NEXT, 2f);
        mDefaultValues.put(CARD_STEP_DURATION, 0.5);
        mDefaultValues.put(CARD_STEP_DELAY, 1f);
        mConfigs = new HashMap<String, Object>(mDefaultValues);
    }

    public static <T> T get(String config) {
        return sConfig.internalGet(config);
    }

    private <T> T internalGet(String config) {
        try {
            Object value = mConfigs.get(config);
            if (value == null) {
                throw new IllegalStateException("No value for " + config);
            }
            return (T) value;
        } catch (Exception e) {
            Object defaultValue = mDefaultValues.get(config);
            Logger.e("Failed casting " + config + " will provide default " + defaultValue);
            return (T) defaultValue;
        }
    }
}
