#version 300 es
// Fragment Shader
precision mediump float;

in vec4 color_vary;
in vec3 normal_vary;
in vec3 lightdir_vary;
in vec3 viewdir_vary;

out vec4 frag_color;

struct lighting
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
};

uniform lighting light;


void main()
{
    vec3 nNormal = normalize(normal_vary);
    vec3 nLightdir = normalize(lightdir_vary);
    vec3 nViewdir = normalize(viewdir_vary);

    float d = max(0.f, dot(nNormal, nLightdir));
    vec4 lightcolor = light.ambient + light.diffuse * d;

    //    Only compute expensive spec light when visible at all
    float s = 0.f;
    if(d > 0.f)
    {
        vec3 ref = normalize(reflect(-nLightdir, nNormal));
        s = pow(max(0.f, dot(nViewdir, ref)), 16.f);
    }

    frag_color = lightcolor * color_vary + s * light.specular;
}