package Game.Renderer.Base.Entities;

import Game.Renderer.Base.Utils.Primitives;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TextureLoader;
import Game.Renderer.Base.Utils.TextureType;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Sprite extends PrimitiveEntity{

    protected String label;
    protected int texture;

    public Sprite(Shader shader, String texturePath, Vector3f color) {
        super(shader, color);

        this.texture = TextureLoader.loadTexture(texturePath, TextureType.SPRITE);
    }

    @Override
    public void update(WindowInfo windowInfo) {

        model.identity();
        model.translate(new Vector3f(this.position, 0.f));
        model.scale(this.scale.x, this.scale.y, 1.f);
    }

    @Override
    public void render(WindowInfo windowInfo) {

        shader.use();

        // textura
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);

        // matrizes
        shader.addUniform1f("time", windowInfo.frameCount());
        shader.addUniformMatrix4fv("projection", windowInfo.projection());
        shader.addUniformMatrix4fv("view", windowInfo.view());
        shader.addUniformMatrix4fv("model", this.model);
        shader.addUniform1i("textureBitmap", 0);

        // mesh
        GL30.glBindVertexArray(VAO);
        GL11.glDrawElements(GL11.GL_TRIANGLES, Primitives.squareIndices.length,
                GL11.GL_UNSIGNED_INT, 0);
        GL30.glBindVertexArray(0);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
