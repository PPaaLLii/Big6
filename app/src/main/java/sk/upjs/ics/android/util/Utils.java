package sk.upjs.ics.android.util;

/**
 * Created by Pavol on 4. 5. 2015.
 */
public class Utils {

    public static String convertType(int type) {
        switch(type){
            case TrainingTypes.PUSHUPS:
                return "PUSHUPS";
            case TrainingTypes.SQUAT:
                return "SQUAT";
            case TrainingTypes.PULLUPS:
                return "PULLUPS";
            case TrainingTypes.LEG_RAISE:
                return "LEG RAISE";
            case TrainingTypes.BRIDGE:
                return "BRIDGE";
            case TrainingTypes.HANDSTAND_PUSHUPS:
                return "HANDSTAND PUSHUPS";
            default:
                return "ERROR";
        }
    }
}
