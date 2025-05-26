extends Tower

var damage := 82 #было 80
var planned_attack := false
var additional_price_to_up := 50

func get_update_price():
	return Global.tree_tower_price + additional_price_to_up * (level - 1)

func update_tower():
	default_update()
	level += 1
	damage += 17

func _ready() -> void:
	summary_price = Global.tree_tower_price
	

func start_attack_animation():
	var tween = get_tree().create_tween()
	tween.tween_callback(%AnimatedSprite2D.play)
	tween.tween_callback(%AnimatedSprite2D3.play).set_delay(0.2)
	tween.tween_callback(%AnimatedSprite2D2.play).set_delay(0.2)
	
func _physics_process(delta: float) -> void:
	if planned_attack:
		start_attack_animation()
		for enemy_area: Area2D in %Attack.get_overlapping_areas():
			enemy_area.hit(damage, Global.Types.TREE)
		planned_attack = false
	if  $AttackTimer.time_left != 0:
		return
	var enemies = $AttackArea.get_overlapping_areas()
	var mx = 0
	var max_enemy: Enemy = null
	for enemy_area: Area2D in enemies:
		var enemy = enemy_area.get_parent()
		if enemy.progress_ratio > mx:
			mx = enemy.progress_ratio
			max_enemy = enemy
	if max_enemy != null:
		$AttackTimer.start()
		%Attack.global_position = max_enemy.global_position
		planned_attack = true
