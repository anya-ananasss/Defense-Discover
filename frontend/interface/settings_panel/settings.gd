extends Control


func _on_settings_cross_button_pressed() -> void:
	visible = false
	
func _ready() -> void:
	%VolumeSlider.value_changed.connect(Music.set_master_volume)
	var dop = null
	if OS.get_name() == 'Web':
		dop = Global.java_script.getCookie("volume")
	elif OS.get_name() == "Android":
		var config = ConfigFile.new()
		var err = config.load(Global.CONFIG_PATH)
		if err == OK:
			dop = config.get_value("session", "volume", null)
	if dop != null:
		Music.set_master_volume(float(dop))
		%VolumeSlider.value = float(dop)
				
	%VolumeSlider.value = db_to_linear(Music.master_volume_db)
