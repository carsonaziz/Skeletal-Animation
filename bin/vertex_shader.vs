#version 330 core

const int MAX_WEIGHTS = 4;
const int MAX_JOINTS = 150;

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;
layout (location = 3) in vec4 jointWeights;
layout (location = 4) in ivec4 jointIndices;

out vec2 TexCoord;
out vec3 Normal;
out vec3 FragPos;

uniform mat4 transformation;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 jointsMatrix[MAX_JOINTS];

void main()
{
	vec4 initPos = vec4(0, 0, 0, 0);
	vec4 initNormal = vec4(0, 0, 0, 0);

	int count = 0;
	for(int i = 0; i < MAX_WEIGHTS; i++) {
		float weight = jointWeights[i];
		if(weight > 0) {
			count++;
			int jointIndex = jointIndices[i];
			vec4 tmpPos = jointsMatrix[jointIndex] * vec4(aPos, 1.0);
			initPos += weight * tmpPos;
		}
	}
	if(count == 0) {
		initPos = vec4(aPos, 1.0);
	}

	gl_Position = projection * view * transformation * initPos;
	TexCoord = aTexCoord;
}