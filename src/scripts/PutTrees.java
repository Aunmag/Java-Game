package scripts;

import managers.MathManager;
import managers.image.ImageManager;
import sprites.Object;

/**
 * Created by Aunmag on 2016.11.23.
 */

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

            ImageManager[] images = {
                    new ImageManager("objects/air/tree_1"),
                    new ImageManager("objects/air/tree_2"),
                    new ImageManager("objects/air/tree_3"),
            };

//            int imageIndex = 1;
//            int imageIndex = MathManager.random.nextInt(3) + 1;
//            int imageIndex = MathManager.random.nextInt(3);
//            String imagePath = "objects/air/tree_" + imageIndex;
            Object tree = new Object(x, y, "objects/air/tree_1");
//            Object tree = new Object(x, y, images[0]);
            Object.allAir.add(tree);
        }
    }

}
