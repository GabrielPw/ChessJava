package Game.Renderer.Base.Window;

import Game.Renderer.Base.Utils.TextureLoader;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private String title;
    private long ID;

    private int width;
    private int height;
    private float zoom;
    private float min_zoom = 1.f;
    private float max_zoom = 8.0f;
    private float scrollSpeed = 0.1f;
    private Map<Integer, Boolean> keyState;
    private Matrix4f projection;
    public Window(String title, int width, int height, Matrix4f projection){

        this.title = title;
        this.width  = width;
        this.height = height;

        glfwInit();
        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        this.ID = glfwCreateWindow(width,height,title, NULL , NULL);
        this.zoom = 1.f;
        this.projection = projection;

        if (this.ID == NULL)
        {
            System.err.println("Error creating a window");
            System.exit(1);
        }

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);

        // Calcular a posição central
        int xPos = (vidMode.width() - width) / 2;
        int yPos = (vidMode.height() - height) / 2;

        // Definir a posição da janela
        glfwSetWindowPos(this.ID, xPos, yPos);

        glfwMakeContextCurrent(this.ID);
        GL.createCapabilities();
        glDisable(GL_DEPTH_TEST); // ? for 2d or 3D?
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Configurar o callback de entrada
        keyState = new HashMap<>();
        org.lwjgl.glfw.GLFW.glfwSetKeyCallback(ID, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                keyState.put(key, true);
            } else if (action == GLFW.GLFW_RELEASE) {
                keyState.put(key, false);
            }
        });

        setupScrollCallback(ID);

    }

    public void setWindowIcon(String resourcePath) {
        Object[] iconData = TextureLoader.loadWindowIconFromClasspath(resourcePath);
        if (iconData == null) {
            System.err.println("⚠️ Ícone não definido — continuando sem ícone personalizado.");
            return;
        }

        ByteBuffer pixels = (ByteBuffer) iconData[0];
        int[] dims = (int[]) iconData[1];
        int width = dims[0];
        int height = dims[1];

        // Cria GLFWImage
        GLFWImage icon = GLFWImage.create();
        icon.width(width);
        icon.height(height);
        icon.pixels(pixels); // setter: associa o buffer

        // Buffer com 1 imagem
        GLFWImage.Buffer icons = GLFWImage.create(1);
        icons.put(0, icon);
        icons.position(0); //  IMPORTANTE: resetar posição do buffer!

        // Define o ícone
        glfwSetWindowIcon(this.getID(), icons);

        // Libera memória (GLFW já copiou os dados)
        TextureLoader.freeWindowIcon(pixels);

        System.out.println("🖼️ Ícone da janela definido com sucesso!");
    }

    public void setupScrollCallback(long window) {
        glfwSetScrollCallback(window, (windowHandle, xOffset, yOffset) -> {
            float sensitivity = 0.2f;
            float newZoom = this.zoom + (float) yOffset * sensitivity;
            setZoom(newZoom);
        });
    }

    public boolean isKeyPressed(int key) {
        return keyState.getOrDefault(key, false);
    }


    public void updateProjectionMatrix() {
        this.projection = new Matrix4f().ortho2D(
                0, width / zoom,
                0, height / zoom
        );
    }

    public void setZoom(float zoom) {
        this.zoom = Math.max(min_zoom, Math.min(zoom, max_zoom));
        updateProjectionMatrix();
    }

    public long getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getZoom() {
        return zoom;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public void setMaxZoom(float max_zoom){
        this.max_zoom = max_zoom;
    }

    public void setMin_zoom(float min_zoom) {
        this.min_zoom = min_zoom;
    }
}
