<#include "common/header.ftl" />
<ul>
<li>Badge ID: ${badge.id}</li>
<li>Badge Mode: ${badge.mode ! "Unknown"}</li>
<li>Badge Value: ${badge.value ! "Unknown" }</li>
<#assign colours = badge.colours />
<#if (colours.size!0) &gt; 0>
<ul>
<#list colours?keys as k>
<li>${colours[k]}</li>
</#list>
</ul>
</#if>
</ul>
<#include "common/footer.ftl" />