extends Path2D

var enemy;
var tower_pos: Vector2 = Vector2(300, 300)
var enemy_pos: Vector2 = Vector2.ZERO
const SPEED = 350
var damage

func polinom(x):
	return 2.38095 * x ** 3 - 3.57143 * x ** 2 + 2.19048 * x 

var prev_ratio = 0;
var t: float = 0:
	set(value):
		var dif = value - t
		if $PathFollow2D.progress_ratio >= 1:
			$PathFollow2D.progress = 0
			t = 0
		else:
			$PathFollow2D.progress_ratio =  prev_ratio
			$PathFollow2D.progress += dif
			prev_ratio = $PathFollow2D.progress_ratio
			if $PathFollow2D.global_position.distance_to(enemy_pos) < 5:
				if is_instance_valid(enemy):
					enemy.hit(damage, Global.Types.ARCHER)
				queue_free()
		
func is_back():
	var angle := (enemy_pos - tower_pos).angle()
	return angle < 0

func _physics_process(delta: float) -> void:
	if is_instance_valid(enemy):
		enemy_pos = enemy.global_position
	curve.clear_points()
	curve.add_point(Vector2.ZERO)
	var angle := (enemy_pos - tower_pos).angle()
	if angle < 0:
		z_index = -1
	else:
		z_index = 0
	var middle_point = (enemy_pos + tower_pos) / 2
	var second_point = Vector2(middle_point.x, middle_point.y - 100 * abs(cos(angle)))
	curve.add_point(second_point - tower_pos, (tower_pos - middle_point) / 2, (enemy_pos - middle_point) / 2)
	curve.add_point(enemy_pos - tower_pos)
	t += delta * SPEED
	
	

	
