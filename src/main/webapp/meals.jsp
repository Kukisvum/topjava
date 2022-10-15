<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>HELLO, MEALS</h1>
<p><a href="http://localhost:8080/topjava/meals?action=insert">Add User</a></p>
<table border="1">
    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>

    <c:forEach items="${mealsTo}" var="mealsTo">
        <tr>
            <td>${mealsTo.id}</td>
            <td style="color:${mealsTo.excess ? 'red':'green'}" >
                    ${mealsTo.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}
            </td>
            <td style="color:${mealsTo.excess ? 'red':'green'}">
                    ${mealsTo.description}</td>
            <td style="color:${mealsTo.excess ? 'red':'green'}">
                    ${mealsTo.calories}</td>
            <td><a href="http://localhost:8080/topjava/meals?action=edit&id=${mealsTo.id}">Update</a></td>
            <td><a href="http://localhost:8080/topjava/meals?action=delete&id=${mealsTo.id}">Delete</a></td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
