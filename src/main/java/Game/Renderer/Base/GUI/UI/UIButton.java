package Game.Renderer.Base.GUI.UI;

import Game.Renderer.Base.GUI.TextRenderer;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.TexturePaths;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class UIButton extends UIElement{


    private boolean clicked;

    private TextRenderer textRenderer;
    private Vector3f textColor = new Vector3f(1.f);
    private String innerText;
    private float paddingX = 16.f;  // padding horizontal
    private float paddingY = 4.f;   // padding vertical

    public UIButton(String text, TextRenderer textRenderer, Shader shader, Vector3f color) {
        super(TexturePaths.BOARD_WHITE1X1, shader, color);
        this.innerText = text;
        this.textRenderer = textRenderer;
        this.clicked = false;
    }

    @Override
    protected void update(WindowInfo windowInfo) {
        super.update(windowInfo);

        Vector2f textSize = textRenderer.measureText(innerText);
        setScale( new Vector2f(textSize.x + paddingX * 2, textSize.y + paddingY * 2));
    }

    @Override
    public void render(WindowInfo windowInfo) {

        super.render(windowInfo);

        Vector2f textPos = new Vector2f(
                position.x - scale.x / 2.f + paddingX,
                position.y - scale.y / 4.f
        );

        textRenderer.setTextColor(textColor);
        textRenderer.setScale(1.f);
        textRenderer.renderText(
                innerText,
                textPos,
                new Vector2f(256.f),
                windowInfo.projection(),
                windowInfo.view()
        );

        Vector2f size = textRenderer.measureText(innerText);

    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return clicked;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
    }
}

