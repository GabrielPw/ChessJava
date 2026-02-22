#version 330 core
out vec4 FragColor;
in vec2 TexCoord;
uniform sampler2D textureBitmap;
uniform vec4 color;

void main()
{
    vec4 texColor = texture(textureBitmap, TexCoord);
    FragColor = color * texColor;
}