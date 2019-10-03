extends Node

var connection = null
var lobbies

func _ready():
		print("Start client TCP")
		# Connect
		connection = StreamPeerTCP.new()
		
		connection.connect_to_host("127.0.0.1", 6666)

func _process(delta):
	#looks for a packer
	var bytes = connection.get_available_bytes()
	
	#if a packet exist lets figure out what type it is
	if bytes > 0:
		#split packet into manageable info
		var data = connection.get_string(bytes).split(",")
		print(data)
		#Header is the packet id number
		var header = int(data[0])
		print(header)
		match header:
			1:#login return packet!
				if data[1] == "true":
					get_node("Login").hide()
					get_node("Menu").show()
				else:
					handle_error(2)
			2:#lobby create packet
				print("fuck")
			3:#update list packet
				print(" yeah")
				for i in range(1, data.size()):
					if lobbies.has(data[i]) == false:
						var temp = [data[i]]
						lobbies = lobbies + temp
						get_node("Gameselect/lobbies").add_item(data[i])
				for i in lobbies:
					print(i)

func send_packet(packetType, data):
	if connection.get_status() == connection.STATUS_CONNECTED:
		#Login packet
		match packetType:
			1:
				var dataPacket = (str(packetType) +","+data[0]+","+data[1]+"\n").to_ascii()
				connection.put_data(dataPacket)
			2:
				var dataPacket = (str(packetType)+","+data[0]+","+str(data[1])+","+data[2]+"\n").to_ascii()
				connection.put_data(dataPacket)
	else:
		handle_error(1)

#Generic client error fucntion
func handle_error(errorId):
	match errorId:
		1:
			var errorText = "Server not responding"
			get_node("Login").set_error(errorText)
		2:
			var errorText = "Username or password is incorrect"
			get_node("Login").set_error(errorText)