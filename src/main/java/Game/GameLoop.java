package Game;

import Game.ChessLogic.Chess;
import Game.ChessLogic.Core.BoardSpace;
import Game.ChessLogic.Core.ColEnum;
import Game.Renderer.Base.ChessRenderer;
import Game.Renderer.Base.GUI.TextRenderer;
import Game.Renderer.Base.GUI.UI.UIManager;
import Game.Renderer.Base.Utils.*;
import Game.Renderer.Base.Window.Window;
import Game.Renderer.Base.Window.WindowInfo;
import Game.Renderer.Utils.Mouse;
import Game.Renderer.Utils.MouseState;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLoop {

    Shader fontShader = new Shader("text.vert", "text.frag");
    int arialBlackBitmap = TextureLoader.loadTexture(FontsPath.BITMAP_ARIALBLACK_REGULAR, TextureType.FONT);

    private Window window;
    private double previousTime;
    private double frameTimeAccumulator;
    private int frameCount;

    public GameLoop(Window window){

        this.window = window;
        previousTime = glfwGetTime();
        frameTimeAccumulator = 0.0; // Acumulador para o tempo decorrido
        frameCount = 0;

        window.setZoom(1.f);
        window.updateProjectionMatrix();

        glfwSetFramebufferSizeCallback(window.getID(), (windowID, w, h) -> {
            glViewport(0, 0, w, h);
            window.setWidth(w);
            window.setHeight(h);
            window.updateProjectionMatrix();
        });

        GL30.glEnable(GL30.GL_VERTEX_PROGRAM_POINT_SIZE);
        GL30.glFrontFace( GL30.GL_CCW );
        GL30.glCullFace(GL30.GL_BACK);
        GL30.glEnable(GL30.GL_CULL_FACE);

        window.setWindowIcon("images/chess/logo.png");
        glfwSwapInterval(1); // 1 to enable vSync according to monitor.

    }

    public void run(Chess chess){

        TextRenderer textRenderer = new TextRenderer(arialBlackBitmap, FontsPath.FNTINFO_ARIALBLACK_REGULAR, fontShader, 0.f);
        UIManager uiManager = new UIManager(textRenderer);
        ChessRenderer chessRenderer = new ChessRenderer(this.window, chess.getBoard());

        MouseState uiMouseState = new MouseState();
        MouseState gameMouseState = new MouseState();
        while (!glfwWindowShouldClose(window.getID())){

            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            WindowInfo windowInfo = frameManagement();
            uiManager.renderAll(windowInfo);

            uiManager.checkUIClick(window, uiMouseState);
            boolean hasGameStarted = uiManager.isGameStarted();

            if (hasGameStarted){

                BoardSpace clicked = Mouse.consumeClickedCell(window, chessRenderer.getBoardSprite(), gameMouseState);
                if (clicked != null) {
                    System.out.println("CLICKED: " + ColEnum.fromValue(clicked.col().getIndex()) + clicked.row().getValue());
                    chess.onCellClicked(clicked);
                }
                chessRenderer.run(windowInfo, textRenderer, chess.getSelectedCell());
            }

            glfwSwapBuffers(window.getID());
        }

        glfwDestroyWindow(window.getID());
        glfwTerminate();
    }

    WindowInfo frameManagement(){
        double currentTime = glfwGetTime();
        float deltaTime = (float) (currentTime - previousTime); // Calcular deltaTime
        previousTime = currentTime; // Atualizar previousTime

        frameTimeAccumulator += deltaTime; // Acumular o tempo decorrido
        frameCount++;

        if (frameTimeAccumulator >= 1.0) { // Se passou um segundo
            glfwSetWindowTitle(window.getID(), window.getTitle() + "[" + frameCount + "]");
            frameCount = 0; // Resetar contagem de frames
            frameTimeAccumulator = 0.0; // Resetar o acumulador
        }

        return new WindowInfo(window, frameCount, new Matrix4f(), window.getProjection(), deltaTime);
    }
}
