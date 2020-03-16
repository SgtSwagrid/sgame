#version

in vec3 vertex_, normal_;
in vec2 texmap_;

out vec4 pixel;

uniform sampler2D sampler;
uniform vec4 colour;
uniform bool textured;

void main() {

    pixel = texture(sampler, texmap_) * colour;
}