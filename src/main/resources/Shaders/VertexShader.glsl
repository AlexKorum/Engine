#version 400 core

in vec3 position;
in vec3 normals;

out vec3 colour;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    colour = vec3(0.7f);
}