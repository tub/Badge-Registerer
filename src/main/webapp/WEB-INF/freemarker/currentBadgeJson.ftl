{"id" : "${badge.id}",
<#if user?? && user.name??>
"userKnown" : true,
"userName" : "${user.name}",
"userEmail" : "${user.emailAddress}"
<#else>
"userKnown": false
</#if>
}