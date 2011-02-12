<ul>
    <li>Badge ID: <a href="${contextPath}/users/${user.id}.html">${user.id}</a></li>
    <li style="background-color: hsl(${user.hue}, 100%, 50%)"></li>
    <li>Registered to: ${user.name!"Unknown"} 
        <#if user.emailAddress??>email: ${user.emailAddress}</#if>
        <#if user.twitterId??>twitter: ${user.twitterId}</#if>
        <#if user.announcements??>add to announcments?: ${user.announcements}</#if>
        </li>
    <li>
        <form action="${contextPath}/users.html" method="post">
            <input type="hidden" name="action" value="delete" />
            <input type="hidden" name="userId" value="${user.id}" />
            <input type="submit" name="delete" value="Delete" />
        </form>
    </li>
    <li><a href="${contextPath}/register.html?userId=${user.id}">Edit</a></li>
</ul>