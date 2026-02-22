package Game.Renderer.Base.GUI;

import Game.Renderer.Base.Buffers.EBO;
import Game.Renderer.Base.Buffers.VBO;
import Game.Renderer.Base.Utils.Primitives;
import Game.Renderer.Base.Utils.Shader;
import Game.Renderer.Base.Utils.Vertex;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextRenderer {

    private Map<Character, GlyphData> glyphMap = new HashMap<>();
    private Game.Renderer.Base.Utils.Shader shader;
    private Matrix4f model;
    private Vector2f position;
    private Vector2f scale;
    private VBO VBO;
    private EBO EBO;
    private int VAO;
    private int bitmapTexture;
    private final int MAX_CHARACTERS_QUANTITY = 100;
    private Vector3f textColor;

    private int textSize;
    private float letterSpacing; // Novo: espaçamento entre letras (ajustável)
    private int baseLine;
    private int lineHeight;

    public TextRenderer(int bitmapTexture, String fntInfoFilePath, Game.Renderer.Base.Utils.Shader shader, float letterSpacing) {
        this.model = new Matrix4f().identity();
        this.position = new Vector2f(0, 0);
        this.scale = new Vector2f(1.f, 1.f);
        this.bitmapTexture = bitmapTexture;
        this.textColor = new Vector3f(1.f, 1.f, 1.f); // white default
        this.shader = shader;
        this.letterSpacing = letterSpacing;

        loadGlyphInfoFromBMFont(fntInfoFilePath);

        // buffer stuff
        VAO = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(VAO);

        this.VBO = new VBO((long) (Primitives.squareVertices.length * 4 * MAX_CHARACTERS_QUANTITY) * Float.BYTES, GL30.GL_DYNAMIC_DRAW);
        this.EBO = new EBO((long) (Primitives.squareIndices.length * MAX_CHARACTERS_QUANTITY) * Integer.BYTES, GL30.GL_DYNAMIC_DRAW);

        GL30.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES); // UV

        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
    }

    public void renderText(String text, Vector2f position, Vector2f bitmapDimensions, Matrix4f projection, Matrix4f view) {
        this.position = position;
        int numOfGliphs = text.length();

        if (numOfGliphs > MAX_CHARACTERS_QUANTITY) {
            throw new RuntimeException("Texto a ser renderizado excede a quantidade máxima de caracteres permitidos (" + MAX_CHARACTERS_QUANTITY + ")");
        }

        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(Primitives.squareVertices.length * 4 * numOfGliphs);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(Primitives.squareIndices.length * numOfGliphs);

        float currentXOffset = 0; // Novo: rastreia o deslocamento horizontal acumulado
        int letterIndex = 0;

        for (char letter : text.toCharArray()) {
            GlyphData glyph = glyphMap.get(letter);
            if (glyph == null) {
                System.err.println("Caractere não encontrado: '" + letter + "' (código: " + (int) letter + ")");
                letterIndex++;
                continue; // Pula caracteres não mapeados
            }

            float texelX = 0.5f / bitmapDimensions.x;
            float texelY = 0.5f / bitmapDimensions.y;

            float u1 = (glyph.x + texelX) / bitmapDimensions.x;
            float v1 = (glyph.y + texelY) / bitmapDimensions.y;
            float u2 = (glyph.x + glyph.width - texelX) / bitmapDimensions.x;
            float v2 = (glyph.y + glyph.height - texelY) / bitmapDimensions.y;

            for (Vertex squareVertex : Primitives.squareVertices) {
                float mappedU = squareVertex.textureCoord.x == 0.0f ? u1 : u2;
                float mappedV = squareVertex.textureCoord.y == 0.0f ? v1 : v2;



                // Adiciona currentXOffset à posição x do vértice
                float xPos = currentXOffset + glyph.xoffset + glyph.width * 0.5f;
                float yPos = baseLine - glyph.yoffset - glyph.height * 0.5f;


                verticesBuffer.put(squareVertex.position.x * glyph.width + xPos);
                verticesBuffer.put(squareVertex.position.y * glyph.height + yPos);

                verticesBuffer.put(mappedU);
                verticesBuffer.put(mappedV);
            }

            int baseIndex = letterIndex * 4;
            for (int i = 0; i < Primitives.squareIndices.length; i++) {
                indicesBuffer.put(Primitives.squareIndices[i] + baseIndex);
            }

            // Incrementa o deslocamento com base no xadvance do glifo e no espaçamento adicional

            currentXOffset += glyph.xadvance + letterSpacing;

            letterIndex++;
        }

        verticesBuffer.flip();
        indicesBuffer.flip();

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.bitmapTexture);

        VBO.updateData(verticesBuffer);
        EBO.updateData(indicesBuffer);

        model.identity();
        model.translate(new Vector3f(this.position, 0.f));
        model.scale(this.scale.x, this.scale.y, 1.f);

        shader.use();
        shader.addUniformMatrix4fv("projection", projection);
        shader.addUniformMatrix4fv("view", view);
        shader.addUniformMatrix4fv("model", this.model);
        shader.addUniform3fv("textColor", this.textColor);
        shader.addUniform1i("textureBitmap", 0);

        GL30.glBindVertexArray(VAO);
        VBO.bind();
        EBO.bind();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDrawElements(GL11.GL_TRIANGLES, numOfGliphs * Primitives.squareIndices.length, GL11.GL_UNSIGNED_INT, 0);

        //System.out.println("BaseLine: " + baseLine);
        //System.out.println("LineHeight: " + lineHeight);

        GL30.glBindVertexArray(0);
    }

    private void loadGlyphInfoFromBMFont(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {

                // 1️⃣ Linha com métricas globais
                if (line.startsWith("common ")) {
                    String[] tokens = line.split("\\s+");
                    for (String token : tokens) {
                        if (token.startsWith("base=")) {
                            baseLine = Integer.parseInt(token.split("=")[1]);
                        } else if (token.startsWith("lineHeight=")) {
                            lineHeight = Integer.parseInt(token.split("=")[1]);
                        }
                    }
                }

                // 2️⃣ Linha de glifo
                else if (line.startsWith("char ")) {

                    String[] tokens = line.split("\\s+");
                    Map<String, Integer> values = new HashMap<>();

                    for (String token : tokens) {
                        if (token.contains("=")) {
                            String[] kv = token.split("=");
                            values.put(kv[0], Integer.parseInt(kv[1]));
                        }
                    }

                    int id = values.get("id");
                    if (id < 32 || id > 126) continue;

                    GlyphData glyph = new GlyphData(
                            values.get("x"),
                            values.get("y"),
                            values.get("width"),
                            values.get("height"),
                            values.get("xoffset"),
                            values.get("yoffset"),
                            values.get("xadvance")
                    );

                    glyphMap.put((char) id, glyph);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector2f measureText(String text) {
        float totalWidth = 0;
        int maxHeight = 0;
        int maxDescender = 0;

        for (char letter : text.toCharArray()) {
            GlyphData glyph = glyphMap.get(letter);
            if (glyph == null) continue;

            totalWidth += glyph.xadvance + letterSpacing;
            maxHeight = Math.max(maxHeight, glyph.height + glyph.yoffset);
            maxDescender = Math.max(maxDescender, glyph.height - (baseLine - glyph.yoffset));
        }

        // Altura total = ascender + descender (aproximado via lineHeight)
        float totalHeight = lineHeight; // ou: maxHeight + maxDescender

        return new Vector2f(totalWidth, totalHeight);
    }

    public Shader getShader() {
        return shader;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setTextColor(Vector3f textColor) {
        this.textColor = textColor;
    }

    public void setTextColor(float r, float g, float b) {
        this.textColor = new Vector3f(r, g, b);
    }

    // Novo: método para ajustar o espaçamento entre letras
    public void setLetterSpacing(float spacing) {
        this.letterSpacing = spacing;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public void setScale(float scale) {
        this.scale = new Vector2f(scale);
    }
}
