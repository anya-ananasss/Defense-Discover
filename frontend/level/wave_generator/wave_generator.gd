extends Node
class_name Wave_generator

@export var seed : int
var level = 1
var power = 1150
var power_per_level = 275  # Увеличиваем базовую мощность врагов с каждым уровнем было 200
var hp_increasing_per_level = 0.22  # Увеличиваем здоровье врагов с каждым уровнем было 0.1
var timer: Timer
var short_range_min = 0.25  # Минимальная задержка
var short_range_max = 1.45  # Максимальная задержка
var long_range_min = 1.35
var long_range_max = 1.95

signal add_enemy(enemy: Enemy)
signal wave_ended

var rng := RandomNumberGenerator.new()

func _ready() -> void:
	rng.seed = seed
	timer = Timer.new()
	timer.one_shot = true
	add_child(timer)



func start_wave(only_generate = false):
	var current_power = level * power_per_level + power
	while current_power > 0:
		var enemies_type := Global.Enemies.values()
		var enemy := Global.enemy_properties[rng.randi_range(0, len(enemies_type) - 1)]
		var count := rng.randi_range(enemy.min_count_in_group, enemy.max_count_in_group)

		# Увеличиваем количество врагов с каждым уровнем начиная с 2 уровня
		if level >= 2:
			count += rng.randi_range(0, 2)  # Увеличиваем количество врагов случайным образом от 0 до 2

		# Увеличиваем здоровье врагов с каждым уровнем
		for i in range(count):
			var en = enemy.scene.instantiate()
			en.max_hp *= 1 + level * hp_increasing_per_level
			if not only_generate:
				add_enemy.emit(en)
				
			#wave_ended.emit()
			#return
			
			# Генерируем случайную задержку для появления следующего врага
			var random_delay = rng.randf_range(short_range_min, short_range_max)
			if not only_generate:
				timer.start(random_delay)
				await timer.timeout  # Задержка между появлением врагов

		current_power -= count * enemy.power
		var wave_delay = rng.randf_range(long_range_min, long_range_max)
		if not only_generate:
			timer.start(wave_delay)
			await timer.timeout

	level += 1
	wave_ended.emit()
#Снизу старый код димана до изменения баланса
"""
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
			en.max_hp *= 1 + level * hp_increasing_per_level
			add_enemy.emit(en)
			timer.start(short_range)
			await timer.timeout
		current_power -= count * enemy.power
		timer.start(long_range)
		await timer.timeout
	level += 1
	wave_ended.emit()
	"""
	
	
	
	
