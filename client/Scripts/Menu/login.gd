extends Node
func _ready():
	align_gui()
func _on_LoginButton_pressed():
	var username = get_node("UsernameBox").text
	var password = get_node("PasswordBox").text
	var data = [username,password]
	if username != null and username != "":
		get_node("..").send_packet(1,data);

#function used to set the error code for login
func set_error(errorString):
	get_node("ErrorLabel").set_bbcode("[center]" + errorString + "[/center]")

func align_gui():
	#get all GUI elements
	var username = get_node("UsernameBox")
	var password = get_node("PasswordBox")
	var loginButton = get_node("LoginButton")
	
	#align them
	var viewportWidth = get_viewport().size.x
	var viewportHeight = get_viewport().size.y
	username.rect_position.x = (viewportWidth / 2) - (username.rect_size.x / 2)
	password.rect_position.x = (viewportWidth / 2) - (password.rect_size.x / 2)
	loginButton.rect_position.x = (viewportWidth / 2) - (loginButton.rect_size.x / 2)
