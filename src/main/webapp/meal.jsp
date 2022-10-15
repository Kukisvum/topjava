<%--
  Created by IntelliJ IDEA.
  User: valer
  Date: 13.10.2022
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="HH" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add new meal</title>
</head>
<body>
<form method="POST" action="http://localhost:8080/topjava/meals">
    User ID : <input type="text" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />" /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    Date : <input
        type="datetime-local" name="dateTime" placeholder="yyyy-MM-dd hh:mm:ss"
        value= ${meal.dateTime}       /> <br />
<%--    value= ${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}       /> <br />--%>
     <br /> <input
        type="submit" value="Submit" />
</form>
</body>
</html>
