#version 330 core

in vec2 position;
in vec4 color;
in vec2 texcoord;

out vec4 vertexColor;
out vec2 textureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertexColor = color;
    textureCoord = vec2(texcoord.x,1.0 - texcoord.y);
    gl_Position = projection * view * model * vec4(position, 0.0, 1.0);
}