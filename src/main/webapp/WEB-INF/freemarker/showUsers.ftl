<#include "common/header.ftl" />
<h2>Badge Details</h2>
<#list users?sort_by("id") as user>
<#include "common/user.ftl" />
</#list>
<#include "common/footer.ftl" />