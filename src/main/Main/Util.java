package main.Main;

import java.sql.Timestamp;

public class Util {
    public static long differenceBetweenTimestamps(Timestamp downEnd, Timestamp downStart) {
        long milliseconds1 = downStart.getTime();
        long milliseconds2 = downEnd.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
//        long diffMinutes = diff / (60 * 1000);
//        long diffHours = diff / (60 * 60 * 1000);
//        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffSeconds;
    }
}
