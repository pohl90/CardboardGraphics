// Vertex Shader

attribute vec4 vertex_in;
attribute vec4 color_in;
attribute vec3 normal_in;

uniform mat4 pvm;
uniform mat4 vm;

uniform vec4 lightpos;

varying vec4 color_vary;
varying vec3 normal_vary;
varying vec3 lightdir_vary;
varying vec3 viewdir_vary;

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