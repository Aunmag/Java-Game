package client;

import managers.Timer;

/**
 * Created by Aunmag on 2017.06.03.
 */

public class PerformanceManager {

    public static boolean isMonitoring = false;
    public static Timer timerUpdating = new Timer(true);
    public static Timer timerRendering = new Timer(true);
    public static Timer timerFinishing = new Timer(true);

}
