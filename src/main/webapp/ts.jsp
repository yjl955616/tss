<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>

<h1>请输入诗歌的内容哦！！！<h1>
<form action="${pageContext.request.contextPath}/ts/search" method="post">
    <input type="text" name="aa" />
    <input type="submit" value="搜索">
</form>
</body>
</html>
