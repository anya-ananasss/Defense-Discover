extends Tower

var current_enemy: Area2D
var tween: Tween
var dps = 100
var MIN_DPS = 15 #было 10
var MAX_DPS = 130
var additional_price_to_up := 50
const INCREASING_TIME = 4

var cooldown : float:
	set(value):
		atackTimer.wait_time = value
		var frames : SpriteFrames = %TowerSprite.sprite_frames
		frames.set_animation_speed("default", frames.get_frame_count("default") / value)
		cooldown = value
var damage : float = 30
var atackTimer : Timer = Timer.new()

func get_update_price():
	return Global.electric_tower_price + additional_price_to_up * (level - 1)
	
func update_tower():
	default_update()
	MIN_DPS += 8
	MAX_DPS += 18
	

func _ready() -> void:
	summary_price = Global.electric_tower_price

func _physics_process(delta: float) -> void:
	var enemies = $AttackArea.get_overlapping_areas()
	if is_instance_valid(current_enemy):
		if current_enemy not in enemies:
			current_enemy = null 
	if not is_instance_valid(current_enemy):
		%TowerSprite.frame = 0
		%Attack.visible = false
		%AttackAnimation.stop()
		var mn = INF
		var min_enemy: Area2D = null
		for enemy_area: Area2D in enemies:
			var enemy = enemy_area.get_parent()
			if enemy.progress_ratio < mn:
				mn = enemy.progress_ratio
				min_enemy = enemy_area
		current_enemy = min_enemy
		if current_enemy != null:
			tween = create_tween()
			tween.tween_property(self, "dps",  MAX_DPS, INCREASING_TIME).from(MIN_DPS)
			%TowerSprite.play('default')
			%AttackAnimation.play("start_attack")
	if is_instance_valid(current_enemy):
		%Attack.visible = true
		var direction: Vector2 = current_enemy.get_parent().texture.global_position - %Attack.global_position
		%Attack.region_rect = Rect2(0, 0, 448, direction.length() - 10)
		%Attack.rotation = direction.angle() - PI / 2
		current_enemy.hit(delta * dps, Global.Types.ELECTRIC)
	
	
