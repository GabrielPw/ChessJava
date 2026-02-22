#version 330 core

out vec4 FragColor;

in vec2 TexCoord;
in float time;
uniform sampler2D textureBitmap;
uniform ivec2 selectedCell;

void main()
{
    float tiles = 8.0;

    float x = floor(TexCoord.x * tiles);
    float y = floor(TexCoord.y * tiles);

    float checker = mod(x + y, 2.0);

    vec3 baseColor = mix(
        vec3(0.3, 0.3, 0.7),
        vec3(1.0, 1.0, 1.0),
        checker
    );

    int col = int(x);
    int row = int(y);

    bool isSelected = all(equal(ivec2(col, row), selectedCell));

    if (isSelected) {
        baseColor = mix(baseColor, vec3(0.2, 0.8, 0.2), 0.6);
    }

    float alpha = mix(
        1.0,   // casa escura (opaca)
        0.5,   // casa branca (mais transparente)
        checker
    );

    FragColor = vec4(baseColor, alpha);

}