extends Tower
var dps = 18

var tween: Tween
var visiable = false
const THIKNESS = 0.025
const TWEEN_TIME = 1.25
var additional_price_to_up := 25

func get_update_price():
	return Global.art_tower_price + additional_price_to_up * (level - 1)

func _ready() -> void:
	summary_price = Global.art_tower_price

func update_tower():
	default_update()
	level += 1
	dps += 6 #было 5

func _physics_process(delta: float) -> void:
	var enemies = $AttackArea.get_overlapping_areas()
	if len(enemies) == 0:
		if visiable == true:
			tween = create_tween()
			tween.tween_property(%Shader.material, "shader_parameter/dop_alpha", 1, TWEEN_TIME)
		visiable = false
		%TowerSprite.pause()
	else:
		%Shader.visible = true
		if visiable == false:
			tween = create_tween()
			%Shader.material.set_shader_parameter("start_time", Time.get_ticks_msec() * 0.001)
			tween.tween_property(%Shader.material, "shader_parameter/dop_alpha", 0, TWEEN_TIME)
		visiable = true
		%TowerSprite.play("default")
		for enemy_area: Area2D in enemies:
			enemy_area.hit(delta * dps, Global.Types.ART)
