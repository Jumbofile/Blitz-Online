extends Node

# Declare member variables here. Examples:
# var a = 2
# var b = "text"

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass
func _on_back_pressed():
	get_node("../Gameselect").hide()
	get_node("../Menu").show()

func _on_Button_pressed():
	var lobbyName = get_node("CreateDialog/LineEdit").text
	var data = [lobbyName, 1, "tempName"]
	get_node("..").send_packet(2,data);
	get_node("CreateDialog").hide()

func _on_create_pressed():
	get_node("CreateDialog").show()


