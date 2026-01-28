#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec3 aColor;

out vec2 fragPos;
out vec3 fragColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    vec4 worldPos = model * vec4(aPos, 0.0, 1.0);

    fragPos   = worldPos.xy;   // <-- ESSENCIAL
    fragColor = aColor;

    gl_Position = projection * view * worldPos;
}