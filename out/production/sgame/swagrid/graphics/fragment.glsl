#version 120

in vec3 vertex, normal;
in vec2 texmap;

out vec3 vertex_, normal_;
out vec2 texmap_;

uniform mat4 transform, view, projection, viewport;

void main() {

    gl_Position = viewport * projection * view * transform * vec4(vectex, 1.0);

    vertex_ = (transform * vec4(vertex, 1.0)).xyz;
    normal_ = (transform * vec4(normal, 0.0)).xyz;
    texmap_ = texmap;
}