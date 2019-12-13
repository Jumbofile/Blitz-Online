extends Node

func _on_pvp_pressed():
	get_node(".").hide()
	var nextScene = get_node("../Gameselect")
	nextScene.show();

func _on_logout_pressed():
	#send logout packet
	var client = get_node("..")
	client.send_packet(0, "")
	
	#close game
	get_tree().quit()
