extends Node
var lobbyList
var selectedItem

#list updating
var time_passed = 0
var calls_per_sec = 1
# Called when the node enters the scene tree for the first time.
func _ready():
	get_node("gametype").set_bbcode("[center][b]Play Blitz-Online[/b][/center]")
	lobbyList = get_node("lobbies")

func _process(delta):
	if get_node("../Gameselect").visible == true:
		#update lobby packet
		time_passed += delta
		
		if time_passed >= 1:
			updateGameSelect()
			time_passed -= 1 

func updateGameSelect():
	get_node("..").send_packet(6, null)
	
func populate_list(data):
	lobbyList.clear()
	for i in range(1, data.size()):
		print(data[i])
		lobbyList.add_item(data[i])

#back button goes back to the play button menu
func _on_back_pressed():
	get_node("../Gameselect").hide()
	get_node("../Menu").show()

#this creates a new lobby and sends it the server
func _on_createLobby_pressed():
	var lobbyName = get_node("CreateDialog/LineEdit")
	if lobbyName.text != "":
		var data = [lobbyName.text, 1, "tempName"]
		get_node("..").send_packet(2,data)
		lobbyName.clear()
		get_node("CreateDialog").hide()
	

#this opens up the create dialog
func _on_create_pressed():
	get_node("CreateDialog").show()

func _on_cancelLobby_pressed():
	var lobbyName = get_node("CreateDialog/LineEdit")
	lobbyName.clear()
	get_node("CreateDialog").hide()
	
func goto_lobby(var lobbyString):
	if selectedItem != null:
		var data = [lobbyString]
		get_node("..").send_packet(3,data)

func _on_join_pressed():
	goto_lobby(selectedItem)

func _on_lobbies_item_selected(index):
	lobbyList.unselect_all()
	lobbyList.select(index)
	#print(index)
	selectedItem = lobbyList.get_item_text(index)
