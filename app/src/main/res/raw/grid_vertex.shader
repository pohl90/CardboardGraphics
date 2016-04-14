#version 300 es

uniform mat4 u_model;
uniform mat4 u_projection;
uniform mat4 u_view;
uniform vec4 u_lightpos;

in vec4 a_position;
in vec4 a_color;
in vec4 a_normal;

out vec4 v_Grid;
out vec4 v_color;

void main() {
   mat4 mvp = u_projection * u_view * u_model;
   v_Grid = vec4(u_model * a_position);
   v_color = a_color;
   gl_Position = mvp * a_position;
}