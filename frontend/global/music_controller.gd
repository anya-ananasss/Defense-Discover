class_name MusicController
extends Node

@onready
var audio_bus_idx : int = AudioServer.get_bus_index(audio_bus)

var audio_bus : StringName = &"Music"

func stop():
	player.stop()

func play( playback_position : float = 0.0 ):
	player.play()
	
var main_menu_stream := preload("uid://df58nlsra6mkb")
var level_stream := preload("uid://d3q0taf82i5nn")
var player := AudioStreamPlayer.new()

func set_main_menu_music():
	player.stream = main_menu_stream
	player.play()

func set_level_music():
	player.stream = level_stream
	player.play()

func _ready() -> void:
	add_child(player)
	player.process_mode = Node.PROCESS_MODE_ALWAYS
	player.bus = audio_bus
	player.volume_db = -15

var master_volume_db: float = -10:
	set(value):
		master_volume_db = value
		if audio_bus_idx >= 0:
			AudioServer.set_bus_volume_db(audio_bus_idx, master_volume_db)

func set_master_volume(linear):
	if OS.get_name() == 'Web':
		Global.java_script.setCookie("volume", linear, Http.cookie_time)
	elif OS.get_name() == "Android":
		var config = ConfigFile.new()
		var err = config.load(Global.CONFIG_PATH)
		if err == OK:
			config.set_value("session", "volume", linear)
			config.save(Global.CONFIG_PATH)
	master_volume_db = linear_to_db(linear)
