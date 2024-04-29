import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String convertTimeToString(long timeInMillis) {
        return formatter.format(new Date(timeInMillis));
    }

    public static long convertStringToTime(String timeString) throws ParseException {
        Date date = formatter.parse(timeString);
        return date.getTime();
    }
}
