#version 300 es
precision mediump float;

in vec4 v_Grid;
in vec4 v_color;
out vec4 frag_color;

void main() {
    if ((mod(abs(v_Grid.x), 4.0) < 0.05) || (mod(abs(v_Grid.z), 4.0) < 0.05)) {
       frag_color = vec4(0.85, 0.85, 0.85, 1.0);
    } else {
        frag_color = v_color;
    }
}
