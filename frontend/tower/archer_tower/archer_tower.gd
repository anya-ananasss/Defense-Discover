extends Tower

var arrow_class := Global.arrow
var damage = 42 #было 50
var additional_price_to_up := 30
var radius_attac = $AttackArea.get_overlapping_areas()

func _ready() -> void:
	summary_price = Global.archer_tower_price
	#for i in range(100):
		#var arrow = arrow_class.instantiate()
		#arrow.tower_pos = Vector2(100 * i, 100 * i)
		#arrow.enemy_pos = Vector2(100 * i + 300, 100 * i)
		#$Arrows.add_child(arrow)

func get_update_price():
	return Global.archer_tower_price + additional_price_to_up * (level - 1)

func update_tower():
	default_update()
	damage += 14 #было 15

func _physics_process(delta: float) -> void:
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
		var arrow := arrow_class.instantiate()
		arrow.tower_pos = global_position + %Arrows.position
		arrow.damage = damage
		arrow.enemy = max_enemy
		arrow.enemy_pos = max_enemy.texture.global_position
		if not arrow.is_back():
			%TowerSprite.play("default")
		$Arrows.add_child(arrow)
		$AttackTimer.start()
		
			
		
