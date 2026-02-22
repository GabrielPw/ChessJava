package Game.Renderer.Base.Utils;


import Game.Renderer.Base.Window.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;

public class TextureLoader {

    public static int loadTexture(String path, TextureType type) {

        if (!Paths.get(path).toFile().exists()) {
            System.err.println("Texture file not found: " + path);
            System.exit(1);
        }

        // Prepare buffers for width, height and channels
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load image
        ByteBuffer data = STBImage.stbi_load(path, width, height, channels, 4);
        if (data == null) {throw new RuntimeException("Failed to load a texture file!"+ System.lineSeparator() + STBImage.stbi_failure_reason());}

        // Create a new OpenGL texture
        int textureId = GL30.glGenTextures();
        //GL30.glActiveTexture(glTextureUnit);
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Set texture parameters
        switch (type) {
            case FONT -> {
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
            }
            case SPRITE -> {
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST_MIPMAP_NEAREST);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
            }
        }

        // Upload
        GL30.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGBA,
                width.get(0),
                height.get(0),
                0,
                GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE,
                data
        );

        // Mipmaps só para sprite
        if (type == TextureType.SPRITE) {
            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        }

        // Free the loaded image data
        STBImage.stbi_image_free(data);
        GL30.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        return textureId;
    }

    public static Object[] loadWindowIconFromClasspath(String resourcePath) {
        try {

            InputStream inputStream = TextureLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            if (inputStream == null) {
                System.err.println("❌ Ícone não encontrado no classpath: " + resourcePath);
                return null;
            }

            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();

            if (bytes.length == 0) {
                System.err.println("❌ Ícone vazio: " + resourcePath);
                return null;
            }

            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes);
            buffer.flip();

            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1); // ← ESSENCIAL: não pode ser null!

            ByteBuffer imageBuffer = STBImage.stbi_load_from_memory(buffer, w, h, comp, 4);

            if (imageBuffer == null) {
                System.err.println("❌ Falha ao decodificar imagem: " + STBImage.stbi_failure_reason());
                return null;
            }

            System.out.println("✅ Ícone carregado: " + w.get(0) + "x" + h.get(0) + "px (" +
                    resourcePath + "), canais originais: " + comp.get(0));
            return new Object[]{imageBuffer, new int[]{w.get(0), h.get(0)}};
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar ícone: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void freeWindowIcon(ByteBuffer pixels) {
        if (pixels != null) {
            STBImage.stbi_image_free(pixels);
        }
    }

}