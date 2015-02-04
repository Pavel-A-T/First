
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
<title >mail</title>
</head>

<body>
<form action="/Servlet/Mail" method="POST">
<table>
<tr>
<td>Send  to:</td>
<td><input type="text" name="to"/></td>
</tr>
<tr>
<td>Subject:</td>
<td><input type="text" name="subject"/></td>
</tr>
</table>

<hr/>
<textarea  type= "text"  name="body"  rows="5"  cols="50"> Message  text
</textarea>
<br/><br/>
<input  type="submit"  value="Send  message!"/>

</form>
</body>
</html>
