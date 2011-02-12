<ul>
    <li>Badge ID: ${user.id}</li>
    <#assign hue = ((user.id!0) / 255 * 360) >
    <li style="background-color: hsl(${hue}, 100%, 50%)"></li>
    <li>Registered to: ${user.name!"Unknown"} (${user.emailAddress!"Unknown"})</li>
</ul>