<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="http://localhost:8080/CAGE/src/CAGE.ui.parallax.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/card.css" />
<link type="text/css" rel="stylesheet" href="http://localhost:8080/stylesheet/account.css" />

<title>Wildgoose</title>
<style>

/* Layout rules */
html, 
body {
	height: 100%;
}

.wrap {
	position: relative;
	width: 100%;
	min-height: 100%;	
}

.header {
	width: 100%;
	1height: 50px;
}
.topbar {
	height: 50px;
}
</style>
<style>
.container {
	width: 100%;
	margin: 0;
}
.bg {
	1top: 50%;
	left: 50%;
}
.logo {
	top: 70px;
	left: 50%;
	margin-left: 257px;
}
.primary {
	top: 80px;
	left: 50%;
	margin-left: -77px;
}
.sweat-drop {
	top: 80px;
	left: 50%;
	margin-left: 180px;
}
.msg {
	top: 60px;
	left: 50%;
	margin-left: -350px;
}
.search-container{
	position: relative;
	max-width: 800px;
	1background: blue;
	margin: 0 auto;
	padding-top: 50px;
}
.search-input {
display: inline-block;
width: 400px;
height: 40px;
padding: 5px 27px 6px 12px;
font-size: 28px;
line-height: 1;
color: #ffffff;
background-color: #6b2e7a;
border: 0;
border-radius: 8px;
transition: all .2s ease-in-out;
}
.search-wrapper {
  position: relative;
}
.input-query-entry input[type='submit'] {
display: inline-block;
text-align: center;
width: 80px;
height: 40px;
padding: 5px 27px 6px 12px;
font-size: 28px;
line-height: 1;
color: #ffffff;
background-color: #6b2e7a;
border: 0;
border-radius: 8px;
transition: all .2s ease-in-out;
}
</style>
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
		<label>WILDGOOSE에서 이름이나 이메일, 기사의 URL로 기자를 검색하세요</label>
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