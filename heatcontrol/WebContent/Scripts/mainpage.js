// mainpage.js is used to load from the database all room instances 
// and display the internal temperature in a graph.
$(document).ready(function() {
	// Get info on different rooms from DB
	
	// For each room get the information on the internal temperature,
	// create a new <div> table with the room name and a graph,
	// and append this to the roomTable id part of the main page
	var $rooms = ["5161.0222","5161.0267","5161.0293"];
	var $elements = $('#roomTable');
	for (var i = 0; i < $rooms.length; i++) {
		var $room = $rooms[i];
		//$('#roomTable').append('<div class="col-6 col-sm-6 col-lg-4"><h2>'+room+'</h2><p>'+graph+'</p><a class="btn btn-default" href="roomInformation.html?id='+room+''">View details &raquo;</a></p></div>')
		$elements.append('<div class="col-6 col-sm-6 col-lg-4" id="'+ $room + '"><h2>'+$room+'</h2><p><div id="graph'+$room+'"></div></p><a class="btn btn-default" href="roomInformation.html?room='+$room+'">View details &raquo;</a></p></div>');
		graph("graph"+$room);
	}
	
	$('#add').click(function() {
		var newRoom = prompt("Please enter the name for the new room", "");
		createRoom(newRoom);
	});
	
	$('#remove').click(function() {
		//var $choice = document.getElementById(getRemovalChoice());
		var $toRemove = document.getElementById('5161.0222');
		$($toRemove).remove();
		$rooms.splice($rooms.indexOf($toRemove)+1,1);
	});
	
	function createRoom($room) {
		$rooms.push($room);
		$('#roomTable').append('<div class="col-6 col-sm-6 col-lg-4" id="'+ $room + '"><h2>'+$room+'</h2><p><div id="graph'+$room+'"></div></p><a class="btn btn-default" href="roomInformation.html?room='+$room+'">View details &raquo;</a></p></div>');
		graph("graph"+$room);
	}
	
	function getRemovalChoice() {
		var window = $('<div>Please choose which room you want to remove</div>')
		for (var i=0;i<$rooms.length;i++) {
			window.append('<br><input type="radio" name="roomChoice">'+$rooms[i]);
		}
		$(window).modal();
	}

});