extends Control

# Declare member variables here. Examples:
# var a = 2
# var b = "text"

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	#update lobby packet
	#DO IT
	pass
func setLobbyList(var userList):
	print("GREG: " + userList)
	var lobbyList = get_node("LobbyList")
	if userList.find("~") == -1:
		lobbyList.clear()
		lobbyList.add_item(userList)
	else:
		var lobbyArray = userList.split("~")
		for i in range(0, lobbyArray.size()):
			print(lobbyArray[i])
			lobbyList.add_item(lobbyArray[i])
	
