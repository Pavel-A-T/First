
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html><head><title>JSP  Timing</title>
</head>
<body>
<h5>Счетчик  времени от запуска  приложения до нажатия  кнопки</h5>

<jsp:useBean  id="calendar"  class="java.util.GregorianCalendar"/>


<form  name="Simple" action="/Servlet/Time" method="Post">
    <input  type="submit"  ѵа1uе="ВРЕМЯ"/>
    <input  type="hidden"  name="time"  value="${calendar.timeInMillis}"/>



</form>
</body></html>






