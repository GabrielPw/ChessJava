package Game.Renderer.Utils;

import Game.Chess.Core.Board;
import Game.Chess.Core.BoardSpace;
import Game.Chess.Core.ColEnum;
import Game.Chess.Core.RowEnum;
import Game.Main;
import Game.Renderer.Base.Entities.Sprite;
import Game.Renderer.Base.Window.Window;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

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

        float x = (float) cursorX.get(0);
        float y = window.getHeight() - (float) cursorY.get(0);

        return new Vector2f(x, y);
    }

    public static BoardSpace consumeClickedCell(Window window, Sprite boardSprite){

        if (leftClick(window)){

            Vector2f bottomLeft = new Vector2f(
                    boardSprite.getPosition().x - boardSprite.getScale().x / 2f,
                    boardSprite.getPosition().y - boardSprite.getScale().y / 2f
            );

            float squareSize = boardSprite.getScale().x / 8f;

            Vector2f mousePos = getPosition(window);

            int col = (int) ((mousePos.x - bottomLeft.x) / squareSize);
            int row = (int) ((mousePos.y - bottomLeft.y) / squareSize);

            // 🔁 inverter o eixo Y (0 embaixo → 0 em cima)
            row = 7 - row;

            // clamp de segurança
            col = Math.max(0, Math.min(7, col));
            row = Math.max(0, Math.min(7, row));

            return new BoardSpace(ColEnum.values()[col], RowEnum.values()[row]);
        }

        return null;
    }

}
