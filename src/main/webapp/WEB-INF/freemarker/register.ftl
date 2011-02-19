<#assign bodyId = 'register' />
<#include "common/header.ftl" />
<h2>
<#if user.id??>
Editing user (id ${user.id})
<#else>
Register your badge
</#if>
</h2>
<form class="listed" action="saveUser.html" method="post">
<input type="hidden" name="id" value="${user.id!badge.id}" />
<ul>
<li>
<label>Name <input type="text" name="name" value="${user.name!""}"/></label>
</li>
<li>
<label>Email <input type="text" name="emailAddress" value="${user.emailAddress!""}"/></label>
</li>
<li>
<label>Twitter ID <input type="text" name="twitterId" value="${user.twitterId!""}"/></label>
<li class="checkbox">
<label><input type="checkbox" name="announcements" <#if user.announcements!false >checked</#if>> Add me to the announcements list</label>
</li>
<li class="submit">
<input type="submit" name="submit" value="<#if user.id??>Save<#else>Register</#if>" />
</li>
</form>
<#include "common/footer.ftl" />