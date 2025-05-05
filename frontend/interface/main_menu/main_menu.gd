extends Control

const ALLOWED_SPECIALS = "!@#$%^&*()_+-=[]{}|;:'\",.<>?/ "
const NOT_ALLOWED_EMAIL_ERROR = "Неверный адресс электронной почты" 
const MIN_LEN_PASSWORD_ERROR = "Пароль слишком короткий"
const USERNAME_LEN_ERROR = "Логин не должен быть пустым"
var email_regex :=  RegEx.new() 
const MINIMUM_PASSWORD_LENGTH := 8

var email: String:
	set(value):
		%EmailInput.text = value
		%EmailInput2.text = value
		%EmailLine.text = value
		email = value
var username: String:
	set(value):
		%NicknameInput.text = value
		%NicknameInput2.text = value
		%NicknameLine.text = value
		username = value
		fill_leaderboard()

var row_count = 7
func fill_leaderboard():
	await update_user_data()
	var res = await Http.get_waves()
	if res == null:
		return
	res = res as Http.LeaderBoardDto
	var flag = false
	for i in range(max(row_count - 1, len(res.users))):
		var x: Http.UserScore = res.users[i]
		var row = Global.leaderboard_row_secene.instantiate()
		row.set_params(i + 1, x.username, x.count_wave)
		if x.username == username:
			row.set_bold()
			flag = true
		%LeaderBord.add_child(row)
	var row = Global.leaderboard_row_secene.instantiate()
	if len(res.users) < row_count:
		return
	if flag:
		var x: Http.UserScore = res.users[row_count - 1]
		row.set_params(row_count, x.username, x.count_wave)
	else:
		for i in range(len(res.users)):
			var x: Http.UserScore = res.users[i]
			if x.username == username:
				row.set_bold()
				row.set_params(i + 1, x.username, x.count_wave)
				break
		%LeaderBord.add_child(row)
	
			
func update_user_data():
	if Http.token != '':
		var res = await Http.get_user()
		if 'error' in res:
			return
		username = res['username']
		email = res['email']


func clear_lines():
	for x in find_children("", "LineEdit"):
		x.text = ''

func _ready():
	if Http.token != '':
		%NotificationPanel.visible = false
	fill_leaderboard()
	%TabContainer.set_tab_title(0, 'Профиль')
	%TabContainer.set_tab_title(1, 'Статистика')
	var tab_bar: TabBar = %TabContainer.get_tab_bar()
	email_regex.compile(r"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$")
	for x in  find_children("", "CrossButton"):
		x.pressed.connect(clear_lines)
	
	
	for node in get_tree().get_nodes_in_group("text_fields") + find_children("", "LineEdit"):
		if node is LineEdit:
			node.gui_input.connect(_on_line_edit_gui_input)
	%ConfirmationLineEdit.gui_input.connect(_only_numbers_line_edit)

func _only_numbers_line_edit(event: InputEvent) -> void:
	if event is InputEventKey and event.pressed:
		if event.keycode in [KEY_BACKSPACE, KEY_DELETE, KEY_ENTER, KEY_LEFT, KEY_RIGHT]:
			return

		var char_str = char(event.unicode)

		if not (char_str >= "0" and char_str <= "9"):
			get_viewport().set_input_as_handled()

func _on_line_edit_gui_input(event: InputEvent) -> void:
	if event is InputEventKey and event.pressed:
		if event.keycode in [KEY_BACKSPACE, KEY_DELETE, KEY_ENTER, KEY_LEFT, KEY_RIGHT]:
			return

		var char_str = char(event.unicode)

		if not ((char_str >= "A" and char_str <= "Z") or 
				(char_str >= "a" and char_str <= "z") or 
				(char_str >= "0" and char_str <= "9") or 
				char_str in ALLOWED_SPECIALS):
			get_viewport().set_input_as_handled()

func enable_waiting():
	$WaitingScreen.visible = true

func disable_waiting():
	$WaitingScreen.visible = false

func _on_cross_button_pressed() -> void:
	%Authorization.visible = false
	%Registration.visible = false
	%Profile.visible = false

func set_user_stats():
	var res = await Http.get_user_stat(username)
	if res == null:
		return
	res = res as Http.StatsDto
	%HistoryLine.text = res.history
	%ScienceLine.text = res.science
	%CultureLine.text = res.culture
	%NatureLine.text = res.nature


func _on_profile_button_pressed() -> void:
	%NotificationPanel.visible = false
	%Registration.visible = false
	if Http.token == "":
		%Authorization.visible = true
	else:
		enable_waiting()
		var dct = await Http.get_user()
		if 'error' in dct:
			if Http.token == "":
				%Authorization.visible = true
			else:
				%AcceptDialog.visible = true
		else:
			set_user_stats()
			update_user_data()
			%Profile.visible = true
		disable_waiting()
			

func _on_create_account_pressed() -> void:
	%Authorization.visible = false
	%Registration.visible = true

func _on_login_button_pressed() -> void:
	%Authorization.visible = true
	%Registration.visible = false


func _on_start_game_button_pressed() -> void:
	get_tree().change_scene_to_packed(Global.level_scene)


func _on_check_statistic_pressed() -> void:
	%StatisticMenu.visible = true


func _on_statistic_cross_button_pressed() -> void:
	%StatisticMenu.visible = false


func _on_volume_slider_value_changed(value: float) -> void:
	Global.volume = value
	

func _on_settings_button_pressed() -> void:
	%Settings.visible = true

func is_valid_username(username: String):
	return len(username) > 0

func _on_registration_button_pressed() -> void:
	var error = ''
	if not is_valid_email(%EmailLineEdit.text):
		error = NOT_ALLOWED_EMAIL_ERROR
	elif not is_valid_passoword(%PasswordLineEdit.text):
		error = MIN_LEN_PASSWORD_ERROR
	elif not is_valid_username(%LoginLineEdit.text):
		error = USERNAME_LEN_ERROR
	
	if error != '':
		%ErrorRegistrationLabel.text = error
		return
	
	enable_waiting()
	var ret = await Http.register_user(%EmailLineEdit.text, %LoginLineEdit.text, %PasswordLineEdit.text)
	if 'error' not in ret:
		%Registration.visible = false
		$ConfirmationMenu.visible = true	
	disable_waiting()


func _on_confirmation_cross_button_pressed() -> void:
	$ConfirmationMenu.visible = false
	%ConfirmationLineEdit.text = ''


func _on_confirmation_button_pressed() -> void:
	enable_waiting()
	var ret = await Http.confirm_user(%EmailLineEdit.text, %LoginLineEdit.text, %PasswordLineEdit.text, %ConfirmationLineEdit.text)
	if 'error' not in ret:
		$ConfirmationMenu.visible = false
		%ConfirmationLineEdit.text = ''
	disable_waiting()


func _on_exit_button_pressed() -> void:
	Http.token = ''
	if OS.get_name() == 'Web':
		Global.java_script.setCookie("token", "", 0)
	%Profile.visible = false

func is_valid_email(email: String) -> bool:
	var res :=  email_regex.search(email)
	return res != null and res.get_end() == email.length()

func is_valid_passoword(password: String) -> bool:
	return len(password) >= MINIMUM_PASSWORD_LENGTH


func _on_authorization_button_pressed() -> void:
	var error = ''
	if not is_valid_email(%EmailLineEdit2.text):
		error = NOT_ALLOWED_EMAIL_ERROR
	#elif not is_valid_passoword(%PasswordLineEdit2.text):
		#error = MIN_LEN_PASSWORD_ERROR
	
	if error != '':
		%ErrorAutharizationLabel.text = error
		return
		
	
	enable_waiting()
	var ret = await Http.login_user(%EmailLineEdit2.text, %PasswordLineEdit2.text)
	if 'error' not in ret:
		%Authorization.visible =false
	disable_waiting()


func _on_recover_password_button_pressed() -> void:
	%GetPassword.visible = true
	%Authorization.visible = false


func _on_get_password_cross_button_pressed() -> void:
	%GetPassword.visible = false
	%Authorization.visible = true


func _on_change_password_cross_button_pressed() -> void:
	%ChangePasswordCrossButton.visible = false
