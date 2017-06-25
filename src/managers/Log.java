package managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aunmag on 2016.10.18.
 */

public class Log {

    public static final String typeMisc = "Misc";

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static final String indentation = "\n\n---------\n";

    public static void log(String type, String message) {
        log(type, message, "");
    }

    public static void log(String type, String message, String exception) {
        if (type.isEmpty()) {
            type = typeMisc;
        }

        if (message.isEmpty()) {
            message = "No message.";
        }

        if (exception.isEmpty()) {
            exception = "No exception.";
        }

        String date = dateFormat.format(new Date());

        String data = String.format(
                "%s (%s)\nMessage: %s\nException: %s%s",
                type,
                date,
                message,
                exception,
                indentation
        );

        System.out.println(data);
    }

}
