#version 330 core
out vec4 FragColor;

uniform vec4 pointColor;

void main()
{
    // círculo suave
    vec2 c = gl_PointCoord - vec2(0.5);
    float d = length(c);

    if (d > 0.5)
    discard;

    FragColor = vec4(pointColor);
}