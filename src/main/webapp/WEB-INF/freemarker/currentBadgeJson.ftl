{"id" : "${badge.id}",
"timestamp" : "${timestamp}",
"coloursRecieved" : ${badge.coloursReceived?string},
<#if user?? && user.name??>
"userKnown" : true,
"userName" : "${user.name}",
"userEmail" : "${user.emailAddress}"
<#else>
"userKnown": false
</#if>
}