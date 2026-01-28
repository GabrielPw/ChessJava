#version 330 core

layout (location = 0) in vec2 aPos; // (x, y) do dataset

uniform mat4 projection;
uniform mat4 view;

uniform vec2 planeOrigin;   // origem do plano
uniform float unitScale;    // mesma escala da grid

void main()
{
    // Converte do espaço lógico (dataset)
    // para o espaço do plano cartesiano
    vec2 worldPos = planeOrigin + aPos * unitScale;

    gl_Position = projection * view * vec4(worldPos, 0.0, 1.0);

    gl_PointSize = 3.3; // CONTROLA O TAMANHO DO PONTO
}