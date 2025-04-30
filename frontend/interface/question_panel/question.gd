extends Control
var question_time = 7
var await_time = 2
signal question_completed(result: Results)
signal button_pressed(i) 
enum Results {Correct, Incorrect, ConnectionProblem}

@onready var children = %GridContainer.get_children()
@onready var filling: StyleBoxFlat = %ProgressBar.get_theme_stylebox("fill")

func _on_any_button_pressed(i):
	button_pressed.emit(i)

func _ready() -> void:
	for i in range(len(children)):
		children[i].pressed.connect(_on_any_button_pressed.bind(i))

func start_question(category: String, difficulty: int):
	get_tree().paused = true
	var http_res = await Http.get_question(category, difficulty)
	if http_res == null:
		question_completed.emit(Results.ConnectionProblem)
		get_tree().paused = false
		visible = false
		return
	http_res = http_res as Http.QuestionDto
	visible = true
	for i in range(len(children)):
		var button: Button = children[i]
		button.text = http_res.options[i]
	%Label.text = http_res.question
	var prev_color = filling.bg_color
	var tween := get_tree().create_tween()
	tween.set_parallel()
	tween.set_pause_mode(Tween.TWEEN_PAUSE_PROCESS)
	
	tween.finished.connect(_on_any_button_pressed.bind(-1))
	tween.tween_property(%ProgressBar,  "value", 0, question_time).from(%ProgressBar.max_value)
	
	var modulate_tween := create_tween()
	modulate_tween.tween_interval(0.6 * question_time)
	modulate_tween.tween_property(filling, "bg_color", Color.RED, 0.4 * question_time)
	
	tween.tween_subtween(modulate_tween)
	
	var res = await button_pressed
	var choosen_question = ''
	if res != -1:
		choosen_question = http_res.options[res] 
	tween.stop()
	http_res = await Http.post_answer(http_res.qeustion_id, choosen_question)
	if http_res == null:
		question_completed.emit(Results.ConnectionProblem)
		get_tree().paused = false
		visible = false
		return
	http_res = http_res as Http.AnswerDto
	var timer := get_tree().create_timer(await_time)
	for x: Button in children:
		if x.text == http_res.correct_answer:
			x.modulate = Color.GREEN
	
	if !http_res.is_correct:
		question_completed.emit(Results.Incorrect)
		if res != -1:
			children[res].modulate = Color.INDIAN_RED
		else:
			for x: Button in children:
				if x.text != http_res.correct_answer:
					x.modulate = Color.INDIAN_RED
					
	else:
		question_completed.emit(Results.Correct)
	await timer.timeout
	visible = false
	get_tree().paused = false
	filling.bg_color = prev_color
	for i in range(len(children)):
		children[i].modulate = Color.WHITE
	
