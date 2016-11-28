package root.scripts;

// Created by Aunmag on 03.10.2016.

import java.util.Random;

public class FRandom {

    public static Random random = new Random();
    private static final double randomDepth = 1000000;

    public static double rand(double mid, double offset, double flex) {

        double min = mid - offset;
        double max = mid + offset;

        return random.nextInt((int) ((max - min) * randomDepth)) / randomDepth + min;

    }

}
