extends Node

var connection = null
var lobbies
var wrapped_client

func _ready():
		print("Start client TCP")
		# Connect
		connection = StreamPeerTCP.new()
		
		connection.connect_to_host("127.0.0.1", 6666)

func _process(delta):
	
	if connection.is_connected_to_host():
		wrapped_client = PacketPeerStream.new()
		wrapped_client.set_stream_peer(connection)
	#looks for a packer
	var bytes = connection.get_available_bytes()
	
	#if a packet exist lets figure out what type it is
	if bytes > 0:
		#split packet into manageable info
		var inComingData = connection.get_string(bytes);
		#var inComingData = connection.get_peer(1).get_packet()
		print(inComingData)
		var data = inComingData.split(",")
		print(data)
		
		#Parse the packet
		recv_packet(data)

#
# RECIEVE PACKETS
#
func recv_packet(data):
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
				print(data)
				lobbies = "";
				get_node("Gameselect").populate_list(data)

#
# SEND PACKETS
#
func send_packet(packetType, data):
	#is the client connnected
	if connection.get_status() == connection.STATUS_CONNECTED:
		#Login packet
		var dataPacket = ""
		
		#packet switch statement
		match packetType:
			0:#disconnect (0)
				dataPacket = (str(packetType))
			1:#login.... (1, username, password)
				dataPacket = (str(packetType) +","+data[0]+","+data[1])
			2:#new lobby.... (2, lobby id, name, gamemode, owner)
				dataPacket = (str(packetType)+","+data[0]+","+str(data[1])+","+data[2])
			3:#join lobby.... (3, lobby id, player name)
				dataPacket = (str(packetType)+","+data[0])
		connection.put_data((dataPacket + "\n").to_ascii())
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
