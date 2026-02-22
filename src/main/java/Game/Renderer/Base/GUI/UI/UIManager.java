package Game.Renderer.Base.GUI.UI;

import Game.Renderer.Base.GUI.TextRenderer;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TexturePaths;
import Game.Renderer.Base.Window.Window;
import Game.Renderer.Base.Window.WindowInfo;
import Game.Renderer.Utils.Mouse;
import Game.Renderer.Utils.MouseState;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class UIManager {

    UIButton btnStart;
    private List<UIElementGroup> uiGroups = new ArrayList<>();

    public UIManager(TextRenderer textRenderer){

        btnStart = new UIButton("Novo Jogo", textRenderer,
                new Shader("UI/btn.vert", "UI/btn.frag"),
                new Vector3f(0.2f, 0.4f, 0.6f)
        );

        UIButton btnExit = new UIButton("Fechar.", textRenderer,
                new Shader("UI/btn.vert", "UI/btn.frag"),
                new Vector3f(0.2f, 0.4f, 0.6f)
        );

        UIElementGroup initialGroup = new UIElementGroup(
                TexturePaths.BOARD_WHITE1X1,
                new Shader("UI/btn.vert", "UI/btn.frag"),
                new Vector3f(54, 16, 2).div(255)  // preto transparente
        );

        initialGroup.setScale(580.f); // tamanho do "container" (opcional)
        initialGroup.addChild(btnStart);
        initialGroup.addChild(btnExit);
        uiGroups.add(initialGroup);

        btnStart.setPosition(new Vector2f(0, 0)); // centro do grupo
    }

    public void renderAll(WindowInfo windowInfo){

        GL30.glDisable(GL30.GL_DEPTH_TEST);          // UI não usa depth
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

        uiGroups.forEach(uiGroup -> {
            Vector2f windowCenter = new Vector2f(
                    windowInfo.window().getWidth() / 2.f,
                    windowInfo.window().getHeight() / 2.f
            );

            uiGroup.setPosition(windowCenter); // grupo centralizado
            uiGroup.setTransparency(0.86f);

            uiGroup.update(windowInfo);
            uiGroup.render(windowInfo);
        });
    }

    public void checkUIClick(Window window, MouseState mouseState){
        uiGroups.forEach(group -> {
            group.getChildElements().forEach(child -> {
                if (child instanceof UIButton button){  // Pattern matching (Java 16+)
                    // ✅ Usa o estado isolado da UI
                    if (button.isMouseOver(Mouse.getPosition(window)) && mouseState.consumeClick(window)) {
                        button.setClicked(true);
                        //System.out.println("🔘 UI Button clicked: " + button.getText());
                    }
                }
            });
        });
    }

    public boolean isGameStarted() {
        return this.btnStart.isClicked();
    }

    public void addGroup(UIElementGroup newGroup){
        uiGroups.add(newGroup);
    }

}
