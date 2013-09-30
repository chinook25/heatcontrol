// mainpage.js is used to load from the database all room instances 
// and display the internal temperature in a graph.
$(document).ready(function() {
	// Get info on different rooms from DB
	var rooms.load()
	
	// For each room get the information on the internal temperature,
	// create a new <div> table with the room name and a graph,
	// and append this to the roomTable id part of the main page
	var $rooms = ["room1","room2"];
	var $elements = $('<div />');
	for (var i = 0; i < $rooms.length; i++) {
		var $room = $rooms[i];
		//$('#roomTable').append('<div class="col-6 col-sm-6 col-lg-4"><h2>'+room+'</h2><p>'+graph+'</p><a class="btn btn-default" href="roomInformation.html?id='+room+''">View details &raquo;</a></p></div>')
		$elements.append('<div class="col-6 col-sm-6 col-lg-4"><h2>'+$room+'</h2><p>graph</p><a class="btn btn-default" href="roomInformation.html?id='+$room+'">View details &raquo;</a></p></div>');
	}
	$('#roomTable').html($elements);
});