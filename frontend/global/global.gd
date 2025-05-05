extends Node

enum Types {ELECTRIC, ARCHER, ART, TREE}

signal gold_changed
signal start_question(type: Types)
signal question_completed(is_successful: bool)

var gold : int:
	set(v):
		gold = v
		gold_changed.emit()
var archer_tower_price : int = 100
var electric_tower_price : int = 150
var art_tower_price : int = 150
var tree_tower_price : int = 200
var archer_tower := preload("uid://dblmp648wldnf")
var electric_tower := preload("uid://sqp3gbqgta6a")
var art_tower := preload("uid://bdth5uwj86qyt")
var arrow := preload("uid://d2bj3yntbkiuu")
var tree_tower := preload("uid://6a7dry2en0la")
var level_scene := preload("uid://mahg2nbldblf")
var main_menu_scene := preload("uid://dgd30hd5wxqbf")
var place_for_tower_control_scene := preload('uid://dp7m0dcsucwl3')
var leaderboard_row_secene = preload("uid://da306r6rg5ck5")
var topic_names: Dictionary[Types, String] = {Types.ELECTRIC: 'science', Types.ARCHER: 'history', Types.ART: 'culture', Types.TREE: 'nature'}
var tower_prices: Dictionary[Types, int] = {Types.ELECTRIC: electric_tower_price, Types.ARCHER: archer_tower_price, Types.ART: art_tower_price, Types.TREE: tree_tower_price}
var tower_scenes: Dictionary[Types, PackedScene] = {Types.ELECTRIC: electric_tower , Types.ARCHER: archer_tower, Types.ART: art_tower, Types.TREE: tree_tower}
var max_question_level := 5

var java_script := JavaScriptBridge.get_interface("window")
var volume: float = 0.5

class Properties:
	var power: int
	var min_count_in_group: int
	var max_count_in_group: int
	var scene: PackedScene
	func _init(power: int, min_count_in_group: int,  max_count_in_group: int, scene: PackedScene) -> void:
		self.power = power
		self.min_count_in_group = min_count_in_group
		self.max_count_in_group = max_count_in_group
		self.scene = scene
		
	
	
enum Enemies {SLIME}
var enemy_properties: Dictionary[Enemies, Properties]  = {Enemies.SLIME : Properties.new(100, 3, 7, preload("uid://bm1ng04ojad8f"))}

func _ready() -> void:
	gold = 600
