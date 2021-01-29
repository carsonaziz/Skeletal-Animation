#version 330 core

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

out vec4 FragColour;

uniform vec3 color;
uniform vec3 lightPos;

uniform sampler2D texture1;
uniform sampler2D texture2;

void main()
{
	float ambientStrength = 0.2f;
	vec3 ambient = color * ambientStrength;
	
	vec3 norm = normalize(Normal);
	vec3 lightDir = normalize(lightPos - FragPos);
	float diff = max(dot(norm, lightDir), 0.0);
	vec3 diffuse = diff * color;
	
	vec3 FragColor = mix(texture(texture1, TexCoord), texture(texture2, TexCoord), 1.0).xyz;
	vec3 result = (ambient + diffuse) * FragColor;
	
	FragColour =  vec4(FragColor, 1.0);
}