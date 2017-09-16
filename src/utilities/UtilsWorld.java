package utilities;

import nightingale.utilities.UtilsMath;
import managers.ImageManager;
import sprites.Object;

/**
 * Created by Aunmag on 2016.11.23.
 */

public class UtilsWorld {

    public static void putTrees(int quantity, int spreading) {
        positionChoosing: for (int i = 0; i < quantity; i++) {
            int x = UtilsMath.randomizeBetween(-spreading, spreading);
            int y = UtilsMath.randomizeBetween(-spreading, spreading);

            for (Object air: Object.allAir) {
                if (Math.abs(x - air.getX()) < 128 && Math.abs(y - air.getY()) < 128) {
                    continue positionChoosing;
                }
            }

            ImageManager[] images = {
                    new ImageManager("objects/air/tree_1"),
                    new ImageManager("objects/air/tree_2"),
                    new ImageManager("objects/air/tree_3"),
            };

            int imageIndex = UtilsMath.random.nextInt(3);
            Object tree = new Object(x, y, images[imageIndex]);
            Object.allAir.add(tree);
        }
    }

}
