package scripts;

/**
 * Created by Aunmag on 2016.11.23.
 */

import managers.MathManager;
import sprites.Object;

public class PutTrees {

    public static void put(int quantity, int spreading) {
        newTree: for (int i = 0; i < quantity; i++) {
            int x = MathManager.randomizeBetween(-spreading, spreading);
            int y = MathManager.randomizeBetween(-spreading, spreading);

            for (Object air: Object.allAir) {
                if (Math.abs(x - air.getX()) < 128 && Math.abs(y - air.getY()) < 128) {
                    continue newTree;
                }
            }

            int type = MathManager.random.nextInt(3) + 1;
            Object.allAir.add(new Object("air/tree_" + type, false, x, y, 0));
        }
    }

}
