package aunmag.shooter.environment.decorations;

import aunmag.nightingale.math.BodyPoint;
import aunmag.nightingale.utilities.Operative;

public class Decoration extends Operative {

    public final DecorationType type;
    public final BodyPoint body;

    public Decoration(DecorationType type, float x, float y, float radians) {
        this.type = type;
        body = new BodyPoint(x, y, radians);
    }

    public void render() {
        type.texture.renderOnWorld(
                body.position.x,
                body.position.y,
                body.radians
        );
    }

}
