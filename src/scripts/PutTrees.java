package scripts;

// Created by Aunmag on 23.11.2016.

import sprites.Object;

public class PutTrees {

    public static void put(int number, int size) {

        newTree: for (int i = 0; i < number; i++) {

            int x = FRandom.random.nextInt(size) - size / 2;
            int y = FRandom.random.nextInt(size) - size / 2;

            for (Object air: Object.allAir) {
                if (Math.abs(x - air.x) < 128 && Math.abs(y - air.y) < 128) {
                    continue newTree;
                }
            }

            int type = FRandom.random.nextInt(3) + 1;

            Object.allAir.add(new Object("air/tree_" + type, false, x, y, 0));

        }

    }

}
