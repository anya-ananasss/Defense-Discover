extends Control
class_name PlotMenu
var speed := 0.025
@onready
var label := %Label
@onready
var button := %Button

var sprite_sheet = preload("uid://dlx27y65osvdx")
var sprites

var flag = true

#@export
#var debug_text: String
#
#@export_tool_button("Проверить текст")
#var debug_buttob = debug_show
#
#func debug_show():
	#show_text(debug_text)

func _ready() -> void:
	sprites = Global.slice_spritesheet(sprite_sheet, 1, 6)
	#show_text("Жил-был в старом лесу могучий дуб. Он стоял на холме у опушки, раскинув свои ветви к небу, как будто обнимал солнце. Его звали Мудрый Дуб — не потому что он умел говорить, а потому что веками наблюдал за всем, что происходило вокруг, и знал больше, чем кто-либо в лесу.")

@onready
var player := %AudioStreamPlayer

func play_sound(t, i):
	if not player.playing:
		if t not in '.- ,!':
			#await get_tree().create_timer(0.01).timeout
			player.pitch_scale = randf_range(0.55, 0.75)
			player.play()
		else:
			i = 0
		%TextureRect.texture = sprites[i % len(sprites)]
	

func show_text(text: String):
	var min_sz = %PanelContainer.custom_minimum_size
	visible = true
	label.text = text
	button.visible = true
	if flag:
		%VBoxContainer.reset_size()
		flag = false
	#await  %VBoxContainer.resized
	await  %PanelContainer.resized
	var size =  %PanelContainer.size
	
	label.text = ''
	button.visible = false
	%PanelContainer.custom_minimum_size = size
	player.volume_db = Music.master_volume_db - 20
	
	Music.stop()
	get_tree().paused = true

	var tween = get_tree().create_tween()
	tween.set_pause_mode(Tween.TWEEN_PAUSE_PROCESS)
	tween.tween_property(self, "modulate", Color(1, 1, 1, 1), 0.3).from(Color(1, 1, 1, 0))
	await  tween.finished
	await get_tree().create_timer(0.1).timeout
	for i in range(text.length()):
		label.text += text[i]
		play_sound(text[i], i)
		await get_tree().create_timer(speed).timeout
		if text[i] == '.':
			await get_tree().create_timer(4 * speed).timeout
	
	%TextureRect.texture = sprites[0]
	button.visible = true
	await button.pressed
	button.visible = false
	label.text = ''
	%PanelContainer.custom_minimum_size = min_sz
	await  %PanelContainer.resized
	visible = false
	get_tree().paused = false
	Music.play()
	
