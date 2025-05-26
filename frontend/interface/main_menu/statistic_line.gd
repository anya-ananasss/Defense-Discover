@tool
extends HBoxContainer
signal name_ready()
signal value_ready()
signal value2_ready()


@export var property: String:
	set(value):
		if $Name == null:
			await name_ready
		$Name.text = value
	get():
		return $Name.text
		
@export var value: String:
	set(value):
		if $Value == null:
			await value_ready
		$Value.text = value
	get():
		return $Value.text
		
@export var value2: String:
	set(value):
		if $%Value2 == null:
			await value2_ready
		$Value2.text = value
	get():
		return $Value2.text


func _on_name_ready() -> void:
	name_ready.emit()


func _on_value_ready() -> void:
	value_ready.emit()


func _on_value_2_ready() -> void:
	value2_ready.emit()
