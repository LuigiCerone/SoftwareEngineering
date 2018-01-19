package main.Main;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class Util {
    public static long differenceBetweenTimestamps(Timestamp downEnd, Timestamp downStart) {
        long milliseconds1 = downStart.getTime();
        long milliseconds2 = downEnd.getTime();

        long duration = milliseconds2 - milliseconds1;

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
//        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
//        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);


        return diffInSeconds;
    }

    public static long differenceBetweenTimestamps(long downEnd, long downStart) {
        long duration = downEnd - downStart;
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
//        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
//        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        return diffInSeconds;
    }
}
