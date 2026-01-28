#version 330 core

out vec4 FragColor;

in vec2 fragPos;
in vec3 fragColor;

uniform float time;
uniform float gridScale;
uniform vec2 planeOrigin;

#define MAX_POINTS 64

uniform int pointCount;
uniform vec2 points[MAX_POINTS];
uniform float pointRadius;

#define MAX_LINES 16

uniform int lineCount;
uniform vec3 lines[MAX_LINES];
uniform float lineThickness;

float decisionLine(vec2 p, vec3 params, float thickness)
{
    vec2 mathPos = p / gridScale;

    float numerator = abs(dot(vec3(mathPos, 1.0), params));
    float denom = length(params.xy);

    float d = numerator / denom;

    return 1.0 - smoothstep(0.0, thickness, d);
}

float line(float coord, float thickness)
{
    return 1.0 - smoothstep(0.0, thickness, abs(coord));
}
float grid(vec2 coord, float thickness)
{
    vec2 g = abs(fract(coord) - 0.5);
    float line = max(g.x, g.y);
    return 1.0 - smoothstep(0.5 - thickness, 0.5, line);
}

float drawPoint(vec2 p, vec2 center, float radius)
{
    float d = length(p - center);
    return 1.0 - smoothstep(radius, radius + 0.5, d);
}

void main()
{

    vec2 localPos = fragPos - planeOrigin;
    float axisThickness = 2.7f;

    // ===== EIXOS =====
    float xAxis = line(localPos.y, axisThickness);
    float yAxis = line(localPos.x, axisThickness);
    float axis = max(xAxis, yAxis);

    // ===== GRID =====
    vec2 mathPos = localPos / gridScale;
    float g = 1.0 - grid(mathPos, 0.03);

    // ===== CORES BASE =====
    vec3 bgColor   = vec3(0.1, 0.15, 0.2);
    vec3 axisColor = vec3(1.0);
    vec3 gridColor = vec3(0.3);
    vec3 pointColor = vec3(0.0, 0.5, 7.0);
    vec3 lineColor  = vec3(1.0, 0.6, 0.0);

    // ===== COMPOSIÇÃO =====
    vec3 color = bgColor;

    // grid
    color = mix(color, gridColor, g);

    // eixos
    color = mix(color, axisColor, axis);

    // ===== PONTOS =====
    float pointMask = 0.0;
    for (int i = 0; i < pointCount; i++)
    {
        pointMask = max(
            pointMask,
            drawPoint(localPos, points[i] * gridScale, pointRadius)
        );
    }

    color = mix(color, pointColor, pointMask);

    // ===== LINHAS (BINÁRIA) =====
    float lineMask = 0.0;
    for (int i = 0; i < lineCount; i++)
    {
        float m = decisionLine(localPos, lines[i], lineThickness);
        lineMask = max(lineMask, m);
    }

    // força máscara binária (isso resolve tudo)
    float lineAlpha = step(0.001, lineMask);
    color = mix(color, lineColor, lineAlpha);

    FragColor = vec4(color, 1.0);
}