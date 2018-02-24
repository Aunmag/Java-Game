package aunmag.shooter.environment.terrain;

import aunmag.nightingale.structures.Shader;
import org.joml.Matrix4fc;

public class ShaderTerrain extends Shader {

    private final int uniformLocationProjection;
    private final int uniformLocationQuantity;

    public ShaderTerrain() {
        super(ShaderTerrain.class);

        uniformLocationProjection = getUniformLocation("projection");
        uniformLocationQuantity = getUniformLocation("quantity");

        Shader.setUniform(getUniformLocation("sampler"), 0);
    }

    /* Setters */

    public void setUniformProjection(Matrix4fc projection) {
        Shader.setUniform(uniformLocationProjection, projection);
    }

    public void setUniformQuantity(int scale) {
        Shader.setUniform(uniformLocationQuantity, scale);
    }

}
