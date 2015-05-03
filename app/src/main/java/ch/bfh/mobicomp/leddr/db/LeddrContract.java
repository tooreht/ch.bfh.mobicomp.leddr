package ch.bfh.mobicomp.leddr.db;

import android.provider.BaseColumns;

public final class LeddrContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LeddrContract() {}

    /* Inner class that defines the table contents */
    public static abstract class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "device";
        public static final String COLUMN_NAME_DEVICE_ID = "device_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_NAME = "name";
    }

    /* Inner class that defines the table contents */
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_NAME = "name";
    }
}
