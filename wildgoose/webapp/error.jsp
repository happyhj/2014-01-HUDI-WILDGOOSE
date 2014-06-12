<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="http://localhost:8080/CAGE/src/CAGE.ui.parallax.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/error.css" />
<title>Wildgoose</title>
<div class="wrap">
	<header class="header">
		<%@ include file ="jsp_templates/header.jsp" %>
	</header>
	<div class="container">
	<div class="parallax-wrapper">
		<div class="parallax-bg">
			<img src="http://localhost:8080/image/error404-bg.png" data-rangex="5" data-rangey="5" class="bg parallax-item"/>
		</div>
		<div class="parallax-stage">
			<img src="http://localhost:8080/image/error404-logo.png"style="opacity:.4" data-rangex="7" data-rangey="4" data-reverse class="logo parallax-item"/>
			<img src="http://localhost:8080/image/error404-baked.png" data-rangex="12" data-rangey="3" class="primary parallax-item"/>
			<img src="http://localhost:8080/image/error404-sweatDrop.png"  data-rangex="14" data-rangey="8" class="sweat-drop parallax-item"/>
			<img src="http://localhost:8080/image/error404-404msg.png"  data-rangex="40" data-rangey="18" class="msg parallax-item"/>
		</div>
	</div>
	<div class="search-container">
		<form class="input-query-entry" action="/">
		<label>WILDGOOSE에서 이름이나 이메일, 기사의 URL로 기자를 검색하세요</label><br/>
		<input class="search-input" type="text" name="q" placeholder="Search"/>
		<input type="submit" value="검색"/>
		</form>
	</div>
	</div>
	<footer class="footer"></footer>
</div>
<script src="http://localhost:8080/CAGE/src/CAGE.ui.parallax.js"></script>
<script>
var parallax = new CAGE.ui.parallax();
</script>