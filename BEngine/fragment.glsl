#version 400

in vec2 pass_texCoords;

out vec4 out_Colors;

uniform sampler2D texture_diffuse;

void main()
{
	out_Colors = texture(texture_diffuse, pass_texCoords);
}