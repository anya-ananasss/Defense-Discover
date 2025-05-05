extends TextureButton

@onready var shader_material := material as ShaderMaterial
var increasing = 1.15

func _ready():
	pivot_offset = size / 2
	connect("mouse_entered", _on_mouse_entered)
	connect("mouse_exited", _on_mouse_exited)

func _on_mouse_entered():
	shader_material.set_shader_parameter("is_hovered", true)
	scale *= Vector2(increasing, increasing)

func _on_mouse_exited():
	shader_material.set_shader_parameter("is_hovered", false)
	scale /= Vector2(increasing, increasing)
