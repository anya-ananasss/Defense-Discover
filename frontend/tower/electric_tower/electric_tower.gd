extends Tower

var current_enemy: Area2D
var tween: Tween
var dps = 100
var MIN_DPS = 10
var MAX_DPS = 100
var additional_price_to_up := 50
const INCREASING_TIME = 3

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
	MIN_DPS += 10
	MAX_DPS += 20
	

func _ready() -> void:
	summary_price = Global.electric_tower_price

func _process(delta: float) -> void:
	var enemies = $AttackArea.get_overlapping_areas()
	if is_instance_valid(current_enemy):
		if current_enemy not in enemies:
			current_enemy = null 
	if not is_instance_valid(current_enemy):
		%TowerSprite.frame = 0
		%Attack.visible = false
		%AttackAnimation.stop()
		var mx = 0
		var max_enemy: Area2D = null
		for enemy_area: Area2D in enemies:
			var enemy = enemy_area.get_parent()
			if enemy.progress_ratio > mx:
				mx = enemy.progress_ratio
				max_enemy = enemy_area
		current_enemy = max_enemy
		if current_enemy != null:
			tween = create_tween()
			tween.tween_property(self, "dps",  MAX_DPS, INCREASING_TIME).from(MIN_DPS)
			%TowerSprite.play('default')
			%AttackAnimation.play("start_attack")
	if is_instance_valid(current_enemy):
		%Attack.visible = true
		var direction: Vector2 = current_enemy.global_position - %Attack.global_position
		%Attack.region_rect = Rect2(0, 0, 448, direction.length() - 10)
		%Attack.rotation = direction.angle() - PI / 2
		current_enemy.hit(delta * dps, Global.Types.ELECTRIC)
	
	
