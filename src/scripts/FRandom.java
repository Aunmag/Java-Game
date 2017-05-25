package scripts;

// Created by Aunmag on 03.10.2016.

import java.util.Random;

public class FRandom {

    public static Random random = new Random();
    private static final float randomDepth = 1_000_000;

    public static float rand(float mid, float offset, float flex) {

        float min = mid - offset;
        float max = mid + offset;

        return random.nextInt((int) ((max - min) * randomDepth)) / randomDepth + min;

    }

}
