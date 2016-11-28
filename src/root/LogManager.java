package root;

// Created by AunmagUser on 18.10.2016.

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static final String tDate = "Date: ";
    private static final String tType = "Type: ";
    private static final String tMessage = "Message: ";
    private static final String tTrace = "Trace: ";

    public static void log(String type, String message, Exception e) {

        String data = tDate + dateFormat.format(new Date()) + "\n";

        if (!type.isEmpty()) {
            data += tType + type + "\n";
        } else {
            data += tType + "Misc" + "\n";
        }

        if (!message.isEmpty()) data += tMessage + message + "\n";
        else data += tMessage + "No message." + "\n";

        if (e != null) {
            data += (tTrace + e.toString()) + "\n";
            e.printStackTrace();
        }

        System.out.println(data);

    }

}
