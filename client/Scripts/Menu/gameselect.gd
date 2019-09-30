extends Node

# Declare member variables here. Examples:
# var a = 2
# var b = "text"
var gameMode;
# Called when the node enters the scene tree for the first time.
func _process(delta):
	if get_node(".").is_visible():
		get_node("gametype").set_bbcode("[center]" + gameMode + "[/center]")

# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass
func set_game_mode(gamemode):
	gameMode = gamemode