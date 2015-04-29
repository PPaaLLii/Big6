package sk.upjs.ics.android.big6;

import java.sql.Time;

/**
 * Created by Pavol on 23. 4. 2015.
 */
public class Training {

    private Time time;

    private String type;

    private Boolean isTrainingSet;

    public Training(Time time, String type, Boolean isTrainingSet) {
        this.time = time;
        this.type = type;
        this.isTrainingSet = isTrainingSet;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean IsTrainingSet() {
        return isTrainingSet;
    }

    public void setTrainingSet(Boolean isTrainingSet) {
        this.isTrainingSet = isTrainingSet;
    }
}
