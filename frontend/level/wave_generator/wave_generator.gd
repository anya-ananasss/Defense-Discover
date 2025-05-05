extends Node
class_name  Wave_generator
@export var seed : int
var level = 1
var power = 1000
var power_per_level = 200
var hp_increasing_per_level = 0.1
var timer: Timer;
var short_range = 0.4
var long_range = 3
signal add_enemy(enemy: Enemy)
signal wave_ended

var rng := RandomNumberGenerator.new()

func _ready() -> void:
	rng.seed = seed
	timer = Timer.new()
	timer.one_shot = true
	add_child(timer)


func start_wave():
	var current_power = level * power_per_level + power
	while current_power > 0:
		var enemies_type := Global.Enemies.values()
		var enemy := Global.enemy_properties[rng.randi_range(0, len(enemies_type) - 1)]
		var count := rng.randi_range(enemy.min_count_in_group, enemy.max_count_in_group)
		for i in range(count):
			var en = enemy.scene.instantiate()
			en.hp *= 1 + level * hp_increasing_per_level
			add_enemy.emit(en)
			timer.start(short_range)
			await timer.timeout
		current_power -= count * enemy.power
		timer.start(long_range)
		await timer.timeout
	level += 1
	wave_ended.emit()
	
	
	
	
