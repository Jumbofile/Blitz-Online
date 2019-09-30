extends Node

func _on_pvp_pressed():
	get_node(".").hide()
	var nextScene = get_node("../Gameselect")
	nextScene.set_game_mode("PVP");
	nextScene.show();

func _on_endless_pressed():
	pass # Replace with function body.


func _on_Campaign_pressed():
	pass # Replace with function body.
