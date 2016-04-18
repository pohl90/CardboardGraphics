#version 300 es

uniform mat4 u_model;
uniform mat4 u_projection;
uniform mat4 u_view;

in vec4 a_position;
in vec4 a_color;

out vec4 v_color;

void main() {
   mat4 mvp = u_projection * u_view * u_model;
   v_color = a_color;
   gl_Position = mvp * a_position;
}