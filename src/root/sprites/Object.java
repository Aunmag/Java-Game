package root.sprites;

// Created by Aunmag on 28.09.2016.

import java.util.ArrayList;
import java.util.List;

public class Object extends Sprite {

    public static List<Object> allGroundObjects = new ArrayList<>();
    public static List<Object> allDecorationObjects = new ArrayList<>();
    public static List<Object> allMaterialObjects = new ArrayList<>();
    public static List<Object> allAirObjects = new ArrayList<>();

    public boolean isDeletingObject = false;

    public final String type;
    public final String group;
    public final String name;
    public final String title;

    public int state = 1; // 0 - deleted, 1 - saved, 2 - new added

    public Object(String name, boolean isUnique, double x, double y, double degrees) {

        super(x, y, degrees, isUnique, "/objects/" + name + ".png");
        this.name = name;

        String[]groupAndTitle = name.split("/");
        type = groupAndTitle[0];
        group = groupAndTitle[1];
        title = groupAndTitle[2];

//        switch (type) {
//            case "ground":
//                allGroundObjects.add(this);
//                break;
//            case "decoration":
//                allDecorationObjects.add(this);
//                break;
//            case "material":
//                allMaterialObjects.add(this);
//                break;
//            case "air":
//                allAirObjects.add(this);
//                break;
//            default:
//                System.out.println("Type " + type + " not found!");
//        }

    }

    private void denote() {

//        mouse_collide_x = self.x <= screen.mouse_x_level <= self.x + self.image.width
//        mouse_collide_y = self.y <= screen.mouse_y_level <= self.y + self.image.height
//
//        if mouse_collide_x and mouse_collide_y:
//
//        distinguish = pygame.Surface((self.image.width, self.image.height))
//        distinguish.set_alpha(64)
//        distinguish.fill(colors.white)
//        screen.main.blit(distinguish, (screen.x + self.x, screen.y + self.y))
//
//        if self.is_deleting_object:
//        self.state = 0

    }

    @Override
    public void tick() {

//        denote();

    }

}
