package Game.Renderer.Utils;

import Game.ChessLogic.Core.BoardSpace;
import Game.ChessLogic.Core.ColEnum;
import Game.ChessLogic.Core.RowEnum;
import Game.Renderer.Base.Entities.Sprite;
import Game.Renderer.Base.Window.Window;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    private static final DoubleBuffer cursorX = BufferUtils.createDoubleBuffer(1);
    private static final DoubleBuffer cursorY = BufferUtils.createDoubleBuffer(1);
    private static boolean leftPressedLastFrame = false;

    public static boolean leftClick(Window window) {
        boolean pressed = glfwGetMouseButton(
                window.getID(),
                GLFW_MOUSE_BUTTON_LEFT
        ) == GLFW_PRESS;

        boolean clicked = pressed && !leftPressedLastFrame;
        leftPressedLastFrame = pressed;

        return clicked;
    }

    public static Vector2f getPosition(Window window) {

        cursorX.clear();
        cursorY.clear();
        glfwGetCursorPos(window.getID(), cursorX, cursorY);

        // Obter fator de escala da janela (HiDPI/Retina)
        FloatBuffer xScale = BufferUtils.createFloatBuffer(1);
        FloatBuffer yScale = BufferUtils.createFloatBuffer(1);
        glfwGetWindowContentScale(window.getID(), xScale, yScale);

        float scaleX = xScale.get(0);
        float scaleY = yScale.get(0);

        // Converter de window coords → framebuffer coords
        float x = (float) cursorX.get(0) * scaleX;
        float y = (float) cursorY.get(0) * scaleY; // ainda medido do TOPO

        // Inverter Y para sistema com origem embaixo (usando altura do framebuffer)
        y = window.getHeight() - y;

        return new Vector2f(x, y);
    }

    public static BoardSpace consumeClickedCell(Window window, Sprite boardSprite, MouseState mouseState){
        // ✅ Usa o estado isolado do jogo
        boolean leftC = mouseState.consumeClick(window);

        if (leftC){
            System.out.println("consumeClickedCell: true");

            Vector2f bottomLeft = new Vector2f(
                    boardSprite.getPosition().x - boardSprite.getScale().x / 2f,
                    boardSprite.getPosition().y - boardSprite.getScale().y / 2f
            );

            float squareSize = boardSprite.getScale().x / 8f;
            Vector2f mousePos = getPosition(window); // Mantém a correção de HiDPI que já fizemos

            float rawCol = (mousePos.x - bottomLeft.x) / squareSize;
            float rawRow = (mousePos.y - bottomLeft.y) / squareSize;

            int col = (int) Math.floor(rawCol + 1e-5f);
            int row = (int) Math.floor(rawRow + 1e-5f);

            // Clamp de segurança
            col = Math.max(0, Math.min(7, col));
            row = Math.max(0, Math.min(7, row));

            return new BoardSpace(ColEnum.values()[col], RowEnum.values()[row]);
        }
        return null;
    }

}
