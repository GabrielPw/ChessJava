package Game;

import Game.Chess.Chess;
import Game.Renderer.Base.Window.Window;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Main {

    public static void main(String[] args) {

        Window window = new Window("Chess", 848, 580, new Matrix4f());
        GameLoop gameLoop = new GameLoop(window);

        Chess chess = new Chess();

        gameLoop.run(chess);
    }


}
