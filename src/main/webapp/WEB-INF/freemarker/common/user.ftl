<ul>
    <li>Badge ID: <a href="${contextPath}/users/${user.id}.html">${user.id}</a></li>
    <li style="background-color: hsl(${user.hue()}, 100%, 50%)"></li>
    <li>Registered to: <ul><li>${user.name!"Unknown"} </li>
        <#if user.emailAddress??><li>email: ${user.emailAddress}</li></#if>
        <#if user.twitterId??><li>twitter: ${user.twitterId}</li></#if>
        <#if user.announcements??><li>add to announcments?: ${user.announcements?string}</li></#if>
        </ul>
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