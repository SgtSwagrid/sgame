#version 400 core

in vec3 vertex, normal;
in vec2 texmap;

out vec3 vertex_, normal_;
out vec2 texmap_;

uniform mat4 transform, view, projection, viewport;

void main() {

    gl_Position = projection * view * transform * vec4(vertex, 1.0F);

    vertex_ = (transform * vec4(vertex, 1.0)).xyz;
    normal_ = (transform * vec4(normal, 0.0)).xyz;
    texmap_ = texmap;
}