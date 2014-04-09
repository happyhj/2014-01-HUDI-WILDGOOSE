<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${ requestScope.name } 기자</title>
</head>
<body>
	<div class="card" >
		<h2 class="reporter-name">${ requestScope.name }</h2>
		<h3 class="email">${ requestScope.email }</h3>
		<p class="sub-email">${ requestScope.info }</p>
		<h4 class="press-name">${ requestScope.pressName }</h4>
	</div>
</body>
</html>