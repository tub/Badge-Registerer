<ul class="userInfo">
    <li>
        <form action="${contextPath}/users.html" method="post">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="userId" value="${user.id}" />
            <input type="submit" name="delete" value="Delete" />
        </form> <a href="${contextPath}/register.html?userId=${user.id}">Edit</a></li>
    <li>Badge ID: <a href="${contextPath}/users/${user.id}.html">${user.id}</a></li>
    <li class="colour" style="background-color: hsl(${user.hue()}, 100%, 50%)">&nbsp;</li>
    <li>Registered to: <ul><li>${user.name!"Unknown"} </li>
        <#if user.emailAddress??><li>email: ${user.emailAddress}</li></#if>
        <#if user.twitterId??><li>twitter: ${user.twitterId}</li></#if>
        <#if user.announcements??><li>add to announcments?: ${user.announcements?string}</li></#if>
        </ul>
        </li>
</ul>

<#if colours?? && colours?keys?size &gt; 0>
<h2>People I've seen</h2>
        <ul>
            <#list user.colours?keys?sort as colour>
                <li class="seenColour" style="background-color: hsl(${user.hue(colour?number)}, 100%, 50%)">seen badge ${colour} ${colours[colour?string]} times</li>
            </#list>
        </ul>
</#if>