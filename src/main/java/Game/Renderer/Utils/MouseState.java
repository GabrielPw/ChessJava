package Game.Renderer.Utils;

import Game.Renderer.Base.Window.Window;
import org.lwjgl.glfw.GLFW;

public class MouseState {
    private boolean leftPressedLastFrame = false;

    // Detecta clique (edge: pressionado AGORA, mas não estava no frame anterior)
    public boolean consumeClick(Window window) {
        boolean pressed = GLFW.glfwGetMouseButton(window.getID(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean clicked = pressed && !leftPressedLastFrame;
        leftPressedLastFrame = pressed; // Atualiza APENAS este estado
        return clicked;
    }

    // Apenas verifica se está pressionado (sem edge detection, sem alterar estado)
    public boolean isPressed(Window window) {
        return GLFW.glfwGetMouseButton(window.getID(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    }

    // Getter útil para debug
    public boolean wasPressedLastFrame() {
        return leftPressedLastFrame;
    }
}
