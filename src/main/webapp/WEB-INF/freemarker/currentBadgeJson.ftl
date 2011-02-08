{"id" : "${badge.id}",
<#if user??>
"userKnown" : true,
"userName" : "${user.name}",
"userEmail" : "${user.emailAddress}"
<#else>
"userKnown": false
</#if>
}