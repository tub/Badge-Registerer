<#include "common/header.ftl" />
<h2>All Users</h2>
<#if message??>
    <div class="message">${message}</div>
</#if>
<#list users?sort_by("id") as user>
<#include "common/user.ftl" />
</#list>
<#include "common/footer.ftl" />