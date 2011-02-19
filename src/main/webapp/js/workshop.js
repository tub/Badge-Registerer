var currentBadge = 0;

$(document).ready(function(){
	if($("body#home").size() > 0){
		$("#content").append("<div id='ajax'>Looking for badges</div>");
		
		setInterval( "doBadgeAjax()", 1000 );
		
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
	if(data.timestamp == localStorage.timestamp){
		return;
	}else{
		localStorage.timestamp = data.timestamp;
	}
	
	//Store the ID so we don't react to badges once in a row
	currentBadge = data.id;
	
	if(data.userKnown){
		if(data.coloursRecieved){
			$("#ajax").html("Welcome back "+data.userName + "<br/>Downloaded. Loading your details.");
			setTimeout(function(){ location.href="users/" + data.id + ".html"; }, 1000);
		}else{
			$("#ajax").html("Welcome back "+data.userName + "<br/>Downloading colours from your badge...");
		}
	}else{
		$("#ajax").html("New badge found, loading registration form...");
		setTimeout(function(){location.href="register.html"; }, 1000);
	}
}