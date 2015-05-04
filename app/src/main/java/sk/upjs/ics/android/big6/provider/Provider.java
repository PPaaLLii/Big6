package sk.upjs.ics.android.big6.provider;

import android.provider.BaseColumns;

public class Provider {
    public static class Big6 implements BaseColumns {
        /**
         * Table name for this content provider
         */
        public static final String TABLE_NAME = "trainingHistory";

        public static final String YEAR = "year";

        public static final String MONTH = "month";

        public static final String DAY = "day";

        public static final String TRAINING = "training";

        public static final String TYPE = "type";
    }
}
