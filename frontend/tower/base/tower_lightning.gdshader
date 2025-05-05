shader_type canvas_item;

uniform float strength = 0.0;

void fragment() {
    COLOR = texture(TEXTURE, UV);
    COLOR.rgb += vec3(strength);
}