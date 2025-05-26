extends Area2D

func hit(damage, type):
	$"..".hit(damage, type)
	
func remove():
	$"..".queue_free()

func get_damage():
	return  $"..".damage
