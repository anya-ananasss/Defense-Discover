extends Node2D
var viewport_width = ProjectSettings.get_setting("display/window/size/viewport_width")
var viewport_height = ProjectSettings.get_setting("display/window/size/viewport_height")
var layers: Array[TileMapLayer]
@export var camera: Camera2D

func is_filled(vec):
	for x: TileMapLayer in layers:
		if x.get_cell_source_id(vec) != -1:
			return true
	return false

func filling():
	var tile_set: TileSet = $object2.tile_set
	
	var pattern1 = tile_set.get_pattern(1)
	var pattern2 = tile_set.get_pattern(2)
	var pattern3 = tile_set.get_pattern(3)
	var view_size = get_viewport().get_visible_rect().size
	var tile_size =  tile_set.tile_size
	var map_size = Vector2i(view_size.x / tile_size.x, view_size.y / tile_size.y) 
	var mn_x = -((view_size.x - viewport_width) / 2)
	var mx_x = (view_size.x + viewport_width) / 2
	var mn_y = -((view_size.y - viewport_height) / 2)
	var mx_y = (view_size.y + viewport_height) / 2
	for i in range(mn_x - tile_size.x , mx_x + tile_size.x, tile_size.x):
		for j in range(mn_y - tile_size.y, mx_y + tile_size.y, tile_size.y):
			var vec = $object2.local_to_map(Vector2(i, j) - %object2.global_position)
			var vec1 = vec + Vector2i(0, 1)
			var vec2 = vec
			var vec3 = vec + Vector2i(0,-1)
			if not is_filled(vec2) and ((abs(vec.y) % 3 == 2 and abs(vec.x) % 2 == 0) or (abs(vec.y) % 3 == 1 and abs(vec.x) % 2 == 1)):
				var hsh = hash(vec2)
				if 	hsh % 4 != 0:
					%object.set_pattern(vec1, pattern1)
					%object2.set_pattern(vec2,pattern2)
					%object3.set_pattern(vec3,pattern3)
				if hsh % 5 != 0:
					%object4.set_cell(vec2, 2, Vector2i(6, 4))
			if not is_filled(vec2):
				$grass.set_cell(vec2, 2, Vector2i(11, 1))


func _ready() -> void:
	for x in get_children():
		if x is TileMapLayer:
			layers.append(x)
	filling()
	get_viewport().size_changed.connect(filling)
