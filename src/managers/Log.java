package managers;

// Created by AunmagUser on 18.10.2016.

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    public static void log(String type, String message, Exception e) {

        if (type.isEmpty()) {
            type = "Misc";
        }

        String data = type + " (" + dateFormat.format(new Date()) + ")\n";

        if (!message.isEmpty()) {
            data += "Message: " + message + "\n";
        } else {
            data += "Message: " + "No message." + "\n";
        }

        if (e != null) {
            data += ("Trace: " + e.toString()) + "\n";
            e.printStackTrace();
        }

        System.out.println(data);

    }

}
