package sk.upjs.ics.android.big6.provider;

import android.provider.BaseColumns;

public class Big6Provider {
    public static class TrainingHistory implements BaseColumns {
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

    public static class PhotoUri implements BaseColumns {
        /**
         * Table name for this content provider
         */
        public static final String TABLE_NAME = "photoUri";

        public static final String URI = "uri";

        public static final String YEAR = "year";

        public static final String MONTH = "month";

        public static final String DAY = "day";

        public static final String DESCRIPTION = "description";
    }
}
