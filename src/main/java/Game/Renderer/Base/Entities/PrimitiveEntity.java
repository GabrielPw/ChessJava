package Game.Renderer.Base.Entities;

import Game.Renderer.Base.Buffers.EBO;
import Game.Renderer.Base.Buffers.VBO;
import Game.Renderer.Base.Utils.Primitives;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.Vertex;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

public abstract class PrimitiveEntity {

    protected Vector2f position;
    protected Vector2f scale;
    protected Matrix4f model;
    protected Vector3f color;

    protected Shader shader;

    protected int VAO;
    protected VBO VBO;
    protected EBO EBO;

    public PrimitiveEntity(Shader shader, Vector3f color){

        this.shader = shader;

        this.position  = new Vector2f(0.f, 0.f);
        this.scale     = new Vector2f(1.f, 1.f);
        this.color     = color;
        this.model     = new Matrix4f().identity();

        createBuffers();
    }

    public void render(WindowInfo windowInfo) {

        GL30.glBindVertexArray(VAO);
        VBO.bind();
        EBO.bind();

        GL11.glDrawElements(GL11.GL_TRIANGLES, Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
    }

    protected void createBuffers(){

        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Primitives.squareVertices.length * 8);

        for (Vertex vertex : Primitives.squareVertices) {
            verticesBuffer.put(vertex.position.x);
            verticesBuffer.put(vertex.position.y);
            verticesBuffer.put(vertex.position.z);

            verticesBuffer.put(color.x);
            verticesBuffer.put(color.y);
            verticesBuffer.put(color.z);

            verticesBuffer.put(vertex.textureCoord.x);
            verticesBuffer.put(vertex.textureCoord.y);
        }

        verticesBuffer.flip();

        VBO = new VBO(verticesBuffer, GL30.GL_STATIC_DRAW);
        EBO = new EBO(Primitives.squareIndices, GL30.GL_STATIC_DRAW);

        int stride = 8 * Float.BYTES;

        GL30.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, stride, 0);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, 3 * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);

        GL30.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, stride, 6 * Float.BYTES);
        GL30.glEnableVertexAttribArray(2);

        verticesBuffer.clear();
    }

    protected abstract void update(WindowInfo windowInfo);


    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setPosition(float positionX, float positionY){
      this.position = new Vector2f(positionX, positionY);
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public void setScale(float scale) {
        this.scale = new Vector2f(scale);
    }
}
