<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
</head>
<body>
    hi
    
    <ul>
    <#list user.items as item>
        
    </#list>
    </ul>
    
    <#if user.isAdmin>
        <p>You have admin privileges.</p>
    <#else>
        <p>You do not have admin privileges.</p>
    </#if>
</body>
</html>
