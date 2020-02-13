extends Control

# Declare member variables here. Examples:
var time_passed = 0
var calls_per_sec = 1
var lobbyID
# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	if get_node("../Lobby").visible == true:
		#update lobby packet
		time_passed += delta
		
		if time_passed >= 1:
			updateLobby()
			time_passed -= 1 
			
func updateLobby():
	get_node("..").send_packet(4,lobbyID)
	
func setLobbyList(var userList, var lobby):
	lobbyID = lobby
	
	var lobbyList = get_node("LobbyList")
	lobbyList.clear()
	if userList.find("~") == -1:
		lobbyList.add_item(userList)
	else:
		var lobbyArray = userList.split("~")
		for i in range(0, lobbyArray.size()):
			print(lobbyArray[i])
			lobbyList.add_item(lobbyArray[i])
	


func _on_BackButton_pressed():
	get_node("..").send_packet(5,lobbyID)
	get_node("../Gameselect").show();
	get_node("../Lobby").hide()
