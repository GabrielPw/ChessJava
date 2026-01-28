#version 330 core

in vec2 vertexTextureCoord;
uniform sampler2D textureBitmap;
uniform vec3 textColor;

out vec4 FragColor;

void main() {
    vec4 sampled = texture(textureBitmap, vertexTextureCoord);

    // Usa intensidade do branco como alpha
    float alpha = sampled.r; // ou sampled.g / sampled.b

    if (alpha < 0.01)
    discard;

    FragColor = vec4(textColor, alpha);
}