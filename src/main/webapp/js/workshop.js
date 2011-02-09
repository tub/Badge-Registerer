var currentBadge = 0;

$(document).ready(function(){
	if($("body#home").size() > 0){
		$("#content").append("<div id='ajax'>Looking for badges</div>");
		//setInterval( "doBadgeAjax()", 1000 );
		
		$.comet.init() to setup the connection and get clientId.

		$.comet.publish to send messages,

		$.comet.subscribe to setup a subscription.

		$(’selector’).bind(’subscription’, function(event, data) {});
	}
});

function doBadgeAjax(){
	//Init block here
	$.ajax({
		  url: "currentBadge.json",
		  dataType: 'json',
		  success: function(data){parseBadgeData(data);},
		  error: function(jqXHR, textStatus, errorThrown){
			  var debug = textStatus;
		  }
		});
}
function parseBadgeData(data){
	//The badge hasn't changed, don't worry about it.
	if(data.id == currentBadge){
		return;
	}
	//Store the ID so we don't react to badges once in a row
	currentBadge = data.id;
	
	if(data.userKnown){
		$("#ajax").html("Welcome back "+data.userName + "<br/>Loading your details...");
		location.href="users/" + data.id + ".html";
	}else{
		$("#ajax").html("New badge found, loading registration form...");
		location.href="register.html";
	}
}