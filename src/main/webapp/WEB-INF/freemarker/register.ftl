<#assign bodyId = 'register' />
<#include "common/header.ftl" />
<form action="saveUser.html" method="post">
<input type="hidden" name="id" value="${badge.id}" />
<label>Name <input type="text" name="name" value="${user.name!""}"/></label>
<label>Email <input type="text" name="emailAddress" value="${user.emailAddress!""}"/></label>
<input type="submit" name="submit" value="Register" />
</form>
<#include "common/footer.ftl" />