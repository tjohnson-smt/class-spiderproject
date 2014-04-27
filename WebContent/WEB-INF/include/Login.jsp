<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SMT Site Spider</title>
</head>
<style type="text/css">
	p {margin:0 0 2em}
</style>
<body>

<h1 style="margin-bottom:0">Welcome to the SMT Site Spider</h1>

<p>Do not enter anything into the fields if you would like to spider the site
while not logged in. Otherwise, you can enter your <strong>SMT site login</strong>,
or grab a cookie from a logged in browser, sit back, and enjoy.</p>

<form action="SpiderServlet" method="post">
	User Name: <input type="text" name="userName" value=""><br />
	Password: <input type="password" name="password" value=""><br />
	<br />
	<strong>-OR-</strong><br />
	<br />
	Cookie Override: <input type="text" name="cookies" value=""><br />
	<br />
	<br />
	Please be patient... this will take a moment.<br />
	<input type="submit" name="logIn" value="Start Spidering" />
</form>

</body>
</html>