<#assign bodyId = 'graphPage' />
<#include "common/header.ftl" />
<script>
var graph = new Graph();
var nodes = new Array();
<#assign uids = users?keys>
<#list uids as user>
nodes[${users[user].id}] = graph.newNode({label: '${users[user].name!user}'});
</#list>

<#list uids as user>
    <#if users[user].colours??>
        <#list users[user].colours?keys as colour>
            <#if uids?seq_contains(colour?string) && colour?string != user>
            graph.newEdge(nodes[${users[user].id}], nodes[${colour}], {color: 'hsl(${users[user].hue()}, 100%, 50%)'});
            </#if>
        </#list>
    </#if>
</#list>

jQuery(document).ready(function(){
    jQuery('#graph').springy(graph);
});
</script>
<canvas id="graph" width="960" height="480"/>
<#include "common/footer.ftl" />