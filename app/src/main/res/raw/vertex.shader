#version 300 es
// Vertex Shader

layout (location = 0) in vec4 vertex_in;
layout (location = 1) in vec4 color_in;
layout (location = 2) in vec3 normal_in;

uniform mat4 pvm;
uniform mat4 vm;

uniform vec4 lightpos;

out vec4 color_vary;
out vec3 normal_vary;
out vec3 lightdir_vary;
out vec3 viewdir_vary;

void main()
{
    gl_Position = pvm * vertex_in;
    color_vary = color_in;

    vec4 h = vm * vertex_in;
    vec3 mvPos = h.xyz / h.w;

    lightdir_vary = lightpos.xyz - mvPos;
    viewdir_vary = -mvPos;

//    mat3 n = transpose(inverse(mat3(vm)));
//    Normal Matrix = T(I(vm)) if rotation part of vm is orthonormal. Since no scaling has been applied R(vm) is orthonormal, therefore T(I(vm)) = vm
    normal_vary = mat3(vm) * normal_in;

}