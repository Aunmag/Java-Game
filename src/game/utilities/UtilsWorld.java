package game.utilities;

import nightingale.structures.Texture;
import nightingale.utilities.UtilsMath;
import game.sprites.Object;

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

            Texture[] images = {
                    Texture.getOrCreate("images/objects/air/tree_1"),
                    Texture.getOrCreate("images/objects/air/tree_2"),
                    Texture.getOrCreate("images/objects/air/tree_3"),
            };

            int imageIndex = UtilsMath.random.nextInt(3);
            Object tree = new Object(x, y, 0, images[imageIndex]);
            Object.allAir.add(tree);
        }
    }

}
