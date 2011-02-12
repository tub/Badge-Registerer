<#include "common/header.ftl" />
<h2>User Details</h2>
<#if user??>
<#include "common/user.ftl" />
<#else>
No such user registered yet.
</#if>
<#include "common/footer.ftl" />