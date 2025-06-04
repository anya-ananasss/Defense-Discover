@tool
extends VSplitContainer

@export var texture : Texture2D		

@onready var button_material : ShaderMaterial = preload("tower_card_shader.tres")

@export var price : int:
	set(v):
		price = v
		if price >= 0:
			%Label.text = str(price)
		else:
			%Label.text = '+' + str(-price)
		change_availiable()
signal cardPressed()

func change_availiable():
	if price > Global.gold:
		%TextureButton.disabled = true
		%TextureButton.material = button_material
	else:
		%TextureButton.disabled = false
		%TextureButton.material = null
	
		

func _ready() -> void:
	Global.gold_changed.connect(change_availiable)
	%TextureButton.texture_normal = texture
	%Label.text = str(price)

func _on_texture_button_pressed() -> void:
	cardPressed.emit()

func _physics_process(delta: float) -> void:
	if Engine.is_editor_hint():
		%TextureButton.texture_normal = texture
		if price >= 0:
			%Label.text = str(price)
		else:
			%Label.text = '+' + str(-price)
