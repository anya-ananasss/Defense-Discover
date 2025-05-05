shader_type canvas_item;

uniform float hover_scale = 1.2;
uniform vec4 base_color : source_color = vec4(1.0);
uniform vec4 hover_tint : source_color = vec4(1.0, 1.0, 1.0, 1.0);

uniform bool is_hovered = false;

void fragment() {
	vec4 tex_color = texture(TEXTURE, UV);
	if (is_hovered) {
		tex_color.rgb = mix(tex_color.rgb, hover_tint.rgb, 0.5);
	}

	COLOR = tex_color * base_color;
}