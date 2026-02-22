package Game.Renderer.Base.GUI.UI;

import Game.Renderer.Base.Entities.PrimitiveEntity;
import Game.Renderer.Base.Utils.Primitives;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TextureLoader;
import Game.Renderer.Base.Utils.TextureType;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class UIElement extends PrimitiveEntity {


    private UIElement parent;
    private float transparency;

    private int texture;

    protected UIElement(String texturePath, Shader shader, Vector3f color){
        super(shader, color);
        this.texture = TextureLoader.loadTexture(texturePath, TextureType.SPRITE);

        transparency = 1.f;
    }

    public void setParent(UIElement parent) {
        this.parent = parent;
    }

    public boolean isMouseOver(Vector2f mousePos) {
        float left = position.x - scale.x / 2f;
        float right = position.x + scale.x / 2f;
        float bottom = position.y - scale.y / 2f;
        float top = position.y + scale.y / 2f;

        return mousePos.x >= left && mousePos.x <= right &&
                mousePos.y >= bottom && mousePos.y <= top;
    }

    @Override
    public void render(WindowInfo windowInfo) {
        shader.use();

        // textura
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);

        // matrizes
        shader.addUniform1f("time", windowInfo.frameCount());
        shader.addUniform4fv("color", new Vector4f(color, transparency));
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

    @Override
    protected void update(WindowInfo windowInfo) {

        model.identity();
        model.translate(new Vector3f(this.position, 0.f));
        model.scale(this.scale.x, this.scale.y, 1.f);
    }

    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    public UIElement getParent() {
        return parent;
    }
}
