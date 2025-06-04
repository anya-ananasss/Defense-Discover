extends Node2D
const CAMERA_MOVEMENT_SPEED : float = 150
const CAMERA_ZOOM_SPEED : Vector2 = Vector2(0.25, 0.25)
const CAMERA_ZOOM_DEFAULT : Vector2 = Vector2(1.0, 1.0)
const CAMERA_ZOOM_MIN : Vector2 = Vector2(1, 1)
const CAMERA_ZOOM_MAX : Vector2 = Vector2(2.0, 2.0)	
const CAMERA_TWEEN_DURATION : float = 0.3
var viewport_width = ProjectSettings.get_setting("display/window/size/viewport_width")
var viewport_height = ProjectSettings.get_setting("display/window/size/viewport_height")
var MAX_RATIO = 16 / 9
var MIN_RATIO = 16 / 9
var skip_question: bool = false
var seconds_passed: int
var plot := Global.plot
var wave_ended: bool = true
var is_gamemaster: bool


@export var camera: Camera2D; 

var wave = 0
var enemy_count = 0
var hp:
	set(value):
		if value <= 0:
			value = 0
			#Global.send_analytics("defeat")
			get_tree().paused = true
			%DefeatMenu.visible = true
		%HpInput.text = str(value)
		hp = value
var camera_tween: Tween = null 

func start_stopwatch():
	var timer = Timer.new()
	timer.process_mode = Node.PROCESS_MODE_PAUSABLE
	timer.autostart = true
	timer.timeout.connect(func(): seconds_passed += 1)
	add_child(timer)

func set_game_master():
	var res = await Http.get_user()
	if 'error' not in res:
		is_gamemaster = res['gameMaster']
		%GmPanel.visible = is_gamemaster

func _ready() -> void:
	Global.gold = 600
	set_game_master()
	
	center_field()
	for x in %Places.get_children():
		x.start_question.connect(start_qestion)
	
	get_viewport().size_changed.connect(center_field)
	Global.gold_changed.connect(update_gold_label)
	%GoldInput.text = str(Global.gold)
	hp = 30
	start_stopwatch()
	if Global.is_campaign:
		await  get_tree().create_timer(0.1)
		%PlotMenu.show_text(plot[0])
	Music.set_level_music()

func start_qestion(topic: String, is_ok: Signal, level: int):
	var quest = %Question
	if skip_question:
		is_ok.emit.call_deferred(PlaceForTower.Result.CORRECT)
		return
	quest.start_question(topic, level)
	var res = await quest.question_completed
	if res == quest.Results.Correct:
		is_ok.emit(PlaceForTower.Result.CORRECT)
	elif res == quest.Results.Incorrect:
		is_ok.emit(PlaceForTower.Result.INCORRECT)
	else:
		is_ok.emit(PlaceForTower.Result.CANCEL)
	


func center_field():
	var screen_size = get_viewport().get_visible_rect().size
	#print(screen_size)
	camera.limit_right = max(((viewport_width + screen_size.x / camera.zoom.x) / 2), viewport_width)
	camera.limit_top = min((viewport_height - screen_size.y / camera.zoom.y), 0)
	
var waves = [
	[Global.Enemies.SLIME, Global.Enemies.SLIME],
	[Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME, Global.Enemies.SLIME ]
]


func _input(event: InputEvent) -> void:
	if Input.is_action_just_pressed("zoom_in"):
		if (camera.zoom < CAMERA_ZOOM_MAX):
			if (camera_tween == null or not camera_tween.is_running()):
				camera_tween = create_tween()
				camera_tween.tween_method(zoom_at_mouse, camera.zoom, camera.zoom * (CAMERA_ZOOM_DEFAULT + CAMERA_ZOOM_SPEED), CAMERA_TWEEN_DURATION)
	elif Input.is_action_just_pressed("zoom_out"):
		if (camera.zoom > CAMERA_ZOOM_MIN):
			if (camera_tween == null or not camera_tween.is_running()):
				camera_tween = create_tween()
				camera_tween.tween_method(zoom_at_mouse, camera.zoom, camera.zoom / (CAMERA_ZOOM_DEFAULT + CAMERA_ZOOM_SPEED), CAMERA_TWEEN_DURATION)
	
	if event is InputEventMagnifyGesture:
		zoom_to_point((camera.zoom + Vector2(1, 1) * (event.factor - 1)).clamp(CAMERA_ZOOM_MIN, CAMERA_ZOOM_MAX), event.position)
	elif event is InputEventScreenDrag:
		camera.position -= event.screen_relative / camera.zoom

func fix_center():
	var viewport_size = get_viewport().get_visible_rect().size
	var half_size = viewport_size / (camera.zoom * 2)
	
	var pos = camera.global_position
	pos.x = clamp(pos.x, camera.limit_left + half_size.x, camera.limit_right - half_size.x)
	pos.y = clamp(pos.y, camera.limit_top + half_size.y, camera.limit_bottom - half_size.y)
	camera.global_position = pos
	

	
func _physics_process(delta: float) -> void:
	#print(camera.get_screen_center_position() - Vector2(viewport_width, viewport_height) / 2 / camera.zoom)
	fix_center.call_deferred()
	center_field()
	var viewport_size = get_viewport().size
	#print(get_global_mouse_position(), get_viewport().get_visible_rect().size)
	#print(camera.get_local_mouse_position(), viewport_size)
	var mouse_pos := camera.get_local_mouse_position()
	#print(mouse_pos)
		

func zoom_at_mouse(zoom) -> void:
	var world_before := get_global_mouse_position()
	camera.zoom = zoom
	center_field()
	var world_after := get_global_mouse_position()
	camera.position += (world_before - world_after)
	
func zoom_to_point(new_zoom: Vector2, point: Vector2):
	var world_point_before = camera.get_screen_center_position() + (point - get_viewport_rect().size / 2) / camera.zoom
	
	camera.zoom = new_zoom
	center_field()
	
	var world_point_after = camera.get_screen_center_position() + (point - get_viewport_rect().size / 2) / camera.zoom
	
	camera.global_position += world_point_before - world_point_after

func update_gold_label():
	%GoldInput.text = str(Global.gold)

func start_wave():
	wave_ended = false
	enemy_count = 0
	%StartWave.visible = false
	%Wave_generator.start_wave()
	await  %Wave_generator.wave_ended
	wave_ended = true
	
	

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

func damage_animation():
	var tween = get_tree().create_tween()
	tween.tween_property(%Gates, "modulate", Color(1, 1, 1), 0.1).from(Color(1, 0, 0))

func _on_home_area_entered(area: Area2D) -> void:
	hp -= area.get_damage()
	damage_animation()
	area.remove()




func _on_wave_generator_add_enemy(enemy: Enemy) -> void:
	enemy.offset = randi_range(-20, 20)
	if wave > 5:
		enemy.change_texture()
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
	Global.send_analytics("exit_plot" if Global.is_campaign else "exit", {"duration": seconds_passed / 60})
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
	Global.gold += Global.gold_after_wave
	if not is_gamemaster:
		Http.post_wave(wave)
	%WaveCount.text = str(wave + 1)
	if Global.is_campaign and wave < len(plot):
		await %PlotMenu.show_text(plot[wave])
		if wave == len(plot) - 1:
			get_tree().paused = true
			%WinMenu.visible = true



func _on_path_2d_child_exiting_tree(node: Node) -> void:
	if %Path2D.get_child_count() == 1 and wave_ended and hp > 0:
		_on_wave_ended()



func _on_question_check_box_toggled(toggled_on: bool) -> void:
	skip_question = toggled_on


func _on_hp_spin_box_value_changed(value: float) -> void:
	hp = int(value)


func _on_gold_spin_box_value_changed(value: float) -> void:
	Global.gold = int(value)


func _on_continue_button_pressed() -> void:
	%WinMenu.visible = false
	get_tree().paused = false


func _on_skip_wave_button_pressed() -> void:
	if wave_ended:
		%Wave_generator.start_wave(true)
		_on_wave_ended()


func _on_help_cross_button_pressed() -> void:
	%Help.visible = false
	get_tree().paused = false


func _on_help_button_pressed() -> void:
	%Help.visible = true
	get_tree().paused = true
