extends Node

var base_url := "http://176.98.178.10:8080"
var token := ''
var user_name := ''
var email = ''
var cookie_time = 30
const TIMEOUT_SECONDS := 4.0

func  _ready() -> void:
	if OS.get_name() == 'Web':
		var dop = Global.java_script.getCookie("token")
		if dop != null:
			token = dop
		dop = Global.java_script.getCookue("email")
		email = dop if dop != null else ''
			

func _create_http_request() -> HTTPRequest:
	var http_request := HTTPRequest.new()
	http_request.process_mode =Node.PROCESS_MODE_ALWAYS
	add_child(http_request)
	http_request.timeout = TIMEOUT_SECONDS
	return http_request

func _send_request(http_request: HTTPRequest, method: int, url: String, data: Dictionary = {}, auth: bool = false):
	var headers := ["Content-Type: application/json"]
	if auth:
		headers.append("Authorization: Bearer %s" % token)

	var body := JSON.stringify(data)
	var error := http_request.request(base_url + url, headers, method, body)
	if error != OK:
		http_request.queue_free()
		return {'error':''}

	var result = await http_request.request_completed
	http_request.queue_free()
	if result[0] != HTTPRequest.Result.RESULT_SUCCESS:
		return {'error':''}
	var response_code = result[1]
	var response_body = result[3].get_string_from_utf8()
	
	if response_code == 403:
		token = ''
		if OS.get_name() == 'Web':
			Global.java_script.setCookie("token", "", 0)
	
	if response_code / 100 == 2:
		var json = JSON.parse_string(response_body)
		if json == null:
			return response_body
		return json
	return {'error':''}

# ========== AUTH ==========

func register_user(email: String, username:String, password: String) -> Dictionary:
	self.email = email 
	return await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/createUser", {
		"email": email,
		"password": password,
		'username': username
	})

func set_cookie(token ,email):
		if OS.get_name() == 'Web':
			Global.java_script.setCookie("token", token, cookie_time)
			Global.java_script.setCookie("email", email, cookie_time)
		self.token = token
		self.email = email
	
	

func confirm_user(email: String, username:String, password: String, code: String):
	var response = await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/confirmationUser?code=" + str(code), {
		"email": email,
		"password": password,
		'username': username
	})
	if response is String:
		set_cookie(response, email)
	return response



func login_user(email: String, password: String):
	self.email = email
	var response = await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/login", {
		"email": email,
		"password": password
	})
	if response is String:
		set_cookie(response, email)
	return response


# ========== USER ==========
class StatsDto:
	var history: int
	var science: int
	var culture: int
	var nature: int
	func _init(json):
		var dct = {}
		for x in json:
			dct[x['topic']] = x['score']
		history = int(dct['history'])
		science = int(dct['science'])
		culture = int(dct['culture'])
		nature = int(dct['nature'])

func get_user_stat(username):
	var res = await  _send_request(_create_http_request(), HTTPClient.METHOD_GET, "/api/v1/getUserStat?username=" + username, {}, true)
	if 'error' in res:
		return null
	return StatsDto.new(res)	

func post_wave(waves):
	fill_user_name()
	if user_name != '':
		return
	_send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/postWave", {
		"countWave": waves,
		"username": user_name
	}, true)

class UserScore:
	var username: String
	var count_wave: int
	func _init(dct) -> void:
		username = dct['username']
		count_wave = int(dct['countWave'])
	

class LeaderBoardDto:
	var users: Array[UserScore] = []
	func _init(json):
		for x in json:
			users.append(UserScore.new(x))
			
func get_waves():
	var res = await _send_request(_create_http_request(), HTTPClient.METHOD_GET, "/api/v1/getWaves", {})
	if 'error' in res:
		return null
	return LeaderBoardDto.new(res)
	
	
func get_user() -> Dictionary:
	return await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/getUser?email=" + email, {}, true)

func delete_user() -> Dictionary:
	return await _send_request(_create_http_request(), HTTPClient.METHOD_DELETE, "/api/v1/user", {}, true)

func update_user(email: String, password: String) -> Dictionary:
	return await _send_request(_create_http_request(), HTTPClient.METHOD_PATCH, "/api/v1/user", {
		"email": email,
		"password": password
	}, true)

# ========== QUIZ ==========
class QuestionDto:
	var qeustion_id: String
	var question: String
	var options: Array
	func _init(dct: Dictionary):
		qeustion_id = dct['questionId']
		question = dct['question']
		options = dct['options'] 

func get_question(category: String, difficulty: int):
	fill_user_name()
	var dct = await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/getQuestion", {
		'username': user_name,
		'promt': {
			"topic": category,
			"difficulty": str(difficulty),
			"keyWords": []
		}
	})
	if 'error' in dct:
		return null
	return QuestionDto.new(dct[0])

class AnswerDto:
	var user_name: String
	var is_correct: bool
	var correct_answer: String
	func _init(dct: Dictionary):
		user_name = dct['userName']
		is_correct = dct['correct']
		correct_answer = dct['correctAnswer']
	
func post_answer(question_id: String, answer: String):
	var res = await _send_request(_create_http_request(), HTTPClient.METHOD_POST, "/api/v1/postAnswer", {
		'userName': user_name,
		'questionId': question_id,
		'answer': answer
	})
	if 'error' in res:
		return null
	return AnswerDto.new(res)

func fill_user_name():
	if user_name == '' and token != '':
		var res = await get_user()
		if 'error' not in res:
			user_name = res['username']
