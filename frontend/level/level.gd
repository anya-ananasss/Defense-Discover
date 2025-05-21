extends Node2D
const CAMERA_MOVEMENT_SPEED : float = 150
const CAMERA_ZOOM_SPEED : Vector2 = Vector2(0.25, 0.25)
const CAMERA_ZOOM_DEFAULT : Vector2 = Vector2(1.0, 1.0)
const CAMERA_ZOOM_MIN : Vector2 = Vector2(1, 1)
const CAMERA_ZOOM_MAX : Vector2 = Vector2(2.0, 2.0)	
const CAMERA_TWEEN_DURATION : float = 0.3
const CAMERA_SPEED = 1400
var viewport_width = ProjectSettings.get_setting("display/window/size/viewport_width")
var viewport_height = ProjectSettings.get_setting("display/window/size/viewport_height")
var MAX_RATIO = 16 / 9
var MIN_RATIO = 16 / 9
var skip_question: bool = false
var seconds_passed: int
var plot := Global.plot


@export var camera: Camera2D; 

var wave = 0
var enemy_count = 0
var hp:
	set(value):
		%HpInput.text = str(value)
		hp = value
		if hp <= 0:
			#Global.send_analytics("defeat")
			get_tree().paused = true
			%DefeatMenu.visible = true

var camera_tween: Tween = null 

func start_stopwatch():
	var timer = Timer.new()
	timer.process_mode = Node.PROCESS_MODE_PAUSABLE
	timer.autostart = true
	timer.timeout.connect(func(): seconds_passed += 1)
	add_child(timer)

func _ready() -> void:
	Global.gold = 600
	center_field()
	for x in %Places.get_children():
		x.start_question.connect(start_qestion)
	
	get_viewport().size_changed.connect(center_field)
	Global.gold_changed.connect(update_gold_label)
	%GoldInput.text = str(Global.gold)
	hp = 30
	start_stopwatch()
	if Global.is_campaign:
		%PlotMenu.show_text(plot[0])

func start_qestion(topic: String, is_ok: Signal, level: int):
	var quest = %Question
	if skip_question:
		is_ok.emit.call_deferred(true)
		return
	quest.start_question(topic, level)
	var res = await quest.question_completed
	if res == quest.Results.Correct:
		is_ok.emit(true)
	else:
		is_ok.emit(false)
	
	

func center_field():
	var screen_size = get_viewport().get_visible_rect().size
	#print(screen_size)
	camera.limit_right = (viewport_width + screen_size.x) / 2 
	camera.limit_top = viewport_height - screen_size.y
	
var waves = [
	[Global.Enemies.SLIME, Global.Enemies.SLIME],
	[Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME ]
]

var i = 0
func _process(delta: float) -> void:
	var viewport_size = get_viewport().size
	i += 1
	if i % 100 == 0:
		i = 0
		#print(get_global_mouse_position(), get_viewport().get_visible_rect().size)
		#print(camera.get_local_mouse_position(), viewport_size)
	if Input.is_action_just_pressed("zoom_in"):
		if (camera.zoom < CAMERA_ZOOM_MAX):
			if (camera_tween == null or not camera_tween.is_running()):
				camera_tween = create_tween()
				camera_tween.tween_property(camera, "zoom", camera.zoom * (CAMERA_ZOOM_DEFAULT + CAMERA_ZOOM_SPEED),CAMERA_TWEEN_DURATION)
	elif Input.is_action_just_pressed("zoom_out"):
		if (camera.zoom > CAMERA_ZOOM_MIN):
			if (camera_tween == null or not camera_tween.is_running()):
				camera_tween = create_tween()
				camera_tween.tween_property(camera, "zoom", camera.zoom / (CAMERA_ZOOM_DEFAULT + CAMERA_ZOOM_SPEED),CAMERA_TWEEN_DURATION)
	var mouse_pos := camera.get_local_mouse_position()
	#print(mouse_pos)
	if mouse_pos[0] < -(viewport_size.x / camera.zoom.x) / 2 + 10:
		camera.position.x -= CAMERA_SPEED * delta / camera.zoom.x / viewport_width * viewport_size.x
	if mouse_pos[1] < -(viewport_size.y / camera.zoom.y) / 2 + 10:
		camera.position.y -= CAMERA_SPEED * delta / camera.zoom.y / viewport_height * viewport_size.y
	if mouse_pos[0] > (viewport_size.x / camera.zoom.x) / 2 - 10:
		camera.position.x += CAMERA_SPEED * delta / camera.zoom.x / viewport_width * viewport_size.x
	if mouse_pos[1] > (viewport_size.y / camera.zoom.y) / 2 - 10:
		camera.position.y += CAMERA_SPEED * delta / camera.zoom.y / viewport_height * viewport_size.y
		
		

func update_gold_label():
	%GoldInput.text = str(Global.gold)

func start_wave():
	enemy_count = 0
	%StartWave.visible = false
	%Wave_generator.start_wave()
	

func next_enemy():
	var enemy = Global.enemy_properties[waves[wave][enemy_count]].scene.instantiate()
	%Path2D.add_child(enemy)
	enemy_count += 1
	if enemy_count == waves[wave].size():
		%Control/StartWave.visible = true
		$EnemyTimer.stop()
		wave += 1
			


func _on_button_pressed() -> void:
	get_tree().paused = !get_tree().paused


func _on_home_area_entered(area: Area2D) -> void:
	hp -= 1
	area.remove()




func _on_wave_generator_add_enemy(enemy: Enemy) -> void:
	enemy.offset = randi_range(-20, 20)
	%Path2D.add_child(enemy)


func _on_wave_generator_wave_ended() -> void:
	pass
	#%StartWave.visible = true


func _on_button_2_pressed() -> void:
	$%Question.start_question("math", 1)


func _on_menu_cross_button_pressed() -> void:
	%Menu.visible = false
	get_tree().paused = false


func _on_menu_button_pressed() -> void:
	%Menu.visible = not %Menu.visible
	get_tree().paused = not get_tree().paused


func _on_settings_button_pressed() -> void:
	%Settings.visible = true


func _on_exit_button_pressed() -> void:
	Global.send_analytics("exit", {"duration": seconds_passed})
	get_tree().paused = false
	get_tree().change_scene_to_packed(Global.main_menu_scene)


func _on_restart_button_pressed() -> void:
	get_tree().paused = false
	get_tree().change_scene_to_packed(Global.level_scene)


func _on_place_for_tower_return_menu(control: Control) -> void:
	%Control.add_child(control)


func _on_wave_ended():
	%StartWave.visible = true
	wave += 1
	Http.post_wave(wave)
	if Global.is_campaign and wave < len(plot):
		await %PlotMenu.show_text(plot[wave])
		if wave == len(plot) - 1:
			get_tree().paused = true
			%WinMenu.visible = true



func _on_path_2d_child_exiting_tree(node: Node) -> void:
	if %Path2D.get_child_count() == 1:
		_on_wave_ended()



func _on_question_check_box_toggled(toggled_on: bool) -> void:
	skip_question = toggled_on


func _on_hp_spin_box_value_changed(value: float) -> void:
	hp = int(value)


func _on_gold_spin_box_value_changed(value: float) -> void:
	Global.gold = int(value)
