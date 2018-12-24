<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>



<p>总记录数：${requestScope.counts }</p>


    <c:forEach items="${requestScope.list}" var="list">

        作者: ${list.poet.name}</br>
        诗名: ${list.title}</br>
        内容: ${list.content}</br>
            <hr>
    </c:forEach>



        <c:if test="${requestScope.nowPage != 1}">
            <td>
                <a href="${pageContext.request.contextPath}/ts/search?nowPage=${requestScope.nowPage - 1}&aa=${requestScope.aa}">上一页</a>
            </td>
        </c:if>
        <c:if test="${requestScope.nowPage == 1}">
            <td>
                上一页
            </td>
        </c:if>

        <c:if test="${requestScope.nowPage < requestScope.pageCounts}">
            <td colspan="4">
                <a href="${pageContext.request.contextPath}/ts/search?nowPage=${requestScope.nowPage + 1}&aa=${requestScope.aa}">下一页</a>
            </td>
        </c:if>
        <c:if test="${requestScope.nowPage == requestScope.pageCounts}">
            <td colspan="4">
                下一页
            </td>
        </c:if>

        <td>
            <input type="button" class="button" value="返回重新查询"onclick="location='${pageContext.request.contextPath}/ts.jsp'" />
        </td>


</body>
</html>