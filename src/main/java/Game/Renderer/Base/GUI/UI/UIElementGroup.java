package Game.Renderer.Base.GUI.UI;

import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Window.WindowInfo;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class UIElementGroup extends UIElement{

    private List<UIElement> childElements = new ArrayList<>();

    protected UIElementGroup(String texturePath, Shader shader, Vector3f color) {
        super(texturePath, shader, color);
    }

    void addChild(UIElement child){

        childElements.add(child);
        child.getScale().y = childElements.size();
    }

    @Override
    protected void update(WindowInfo windowInfo) {
        super.update(windowInfo);

        childElements.forEach(child -> {

            child.setPosition(new Vector2f(position).add(0, -(child.getScale().y + 10.f) * childElements.indexOf(child)));
            child.update(windowInfo);
        });
    }

    @Override
    public void render(WindowInfo windowInfo) {

        super.render(windowInfo);
        childElements.forEach(child -> child.render(windowInfo));
    }

    public List<UIElement> getChildElements() {
        return childElements;
    }
}

