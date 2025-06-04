extends PathFollow2D
class_name Enemy
@export var texture: AnimatedSprite2D
@export var lightning: AnimatedSprite2D

@export var speed = 80
var slow_time: float
var slowing := 0.7
var prev_pos := Vector2.ZERO
var offset : int;
var sm_delta: float
@export var award : int = 8; #было 10
@export var mooving_while_dead: bool
@export var damage: int = 1
var is_dead: bool

@export var max_hp: float:
	set(value):
		$HpBar.max_value = value
		hp = value
		max_hp = value

var hp: float:
	set(value):
		if hp <= 0 and value <= 0:
			return
		$HpBar.value = value
		if value <= 0 and hp > 0:
			is_dead = true
			Global.gold += award
			hp = 0
			$Body.queue_free()
			$HpBar.queue_free()
			if texture.sprite_frames and texture.sprite_frames.has_animation("death"):
				texture.play("death")
				await texture.animation_finished
			queue_free()
		else:
			hp = value

func on_art_tower_hit():
	slow_time = 0.17 #было 20

func on_electric_tower_hit():
	if lightning.sprite_frames:
		lightning.play("default")

func hit(damage, type):
	match type:
		Global.Types.ELECTRIC:
			on_electric_tower_hit()
		Global.Types.ART:
			on_art_tower_hit()
	hp -= damage
	
			
func change_texture():
	var frames := texture.sprite_frames
	frames.remove_animation("death")
	frames.remove_animation("move")
	frames.duplicate_animation("death_1", "death")
	frames.duplicate_animation("move_1", "move")
	%Texture.play("move")
	

func calculate_offset_vector():
	var path := get_parent() as Path2D
	var curve := path.curve

	var pos := curve.sample_baked(progress)
	var next_pos := curve.sample_baked(progress + 1.0)

	var tangent := (next_pos - pos).normalized()
	var normal := Vector2(-tangent.y, tangent.x) 
	h_offset = offset * normal.x
	v_offset = offset * normal.y


func mooving(delta):
	slow_time = max(0, slow_time - delta)
	var ds =  delta * speed
	if slow_time > 0:
		progress += ds * slowing
	else:
		progress += ds
	calculate_offset_vector()
	
	sm_delta += delta
	if sm_delta >= 0.1:
		sm_delta -= 0.1
		#var gl_ps = Vector2(global_position.x, -global_position.y)
		#var deg = rad_to_deg((gl_ps - prev_pos).angle())
		#if -85 <= deg and deg <= 85:
			#$Texture.flip_h = false
		#elif (-180 <= deg and deg <= -95) or (95 <= deg and deg <= 180):
			#$Texture.flip_h = true
		if (global_position - prev_pos).x > 0:
			%Texture.scale.x = abs(%Texture.scale.x)
		else:
			%Texture.scale.x = -abs(%Texture.scale.x)
		
		prev_pos = global_position


func _physics_process(delta: float) -> void:
	if not is_dead or mooving_while_dead:
		mooving(delta)
	
	

	
	
