<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-zoom=1, user-scalable=no">
<link type="text/css" rel="stylesheet" href="/stylesheet/base.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/basic_layout.css" />
<link type="text/css" rel="stylesheet" href="/stylesheet/card.css" />
<title>Wildgoose</title>

<div class="wrap">
	<header class="header"></header>
	<div class="container">
		login
		<div class="card-section card-section-identity"
			data-reporter_id="${reporter.id}">
			<h3 class="name">
				<a href="/reporters/${ reporter.id }">${ reporter.name }</a>
			</h3>
			<p class="email">${ reporter.email }</p>
			<div class="favorite">
				<div
					class="star<c:if test="${ empty sessionScope.userId }"> invisible</c:if>"></div>
			</div>

			<div class="${ reporter.pressName } press-tag"></div>
		</div>
		<div class="card-section card-section-headline">
			<h4 class="headline">${ reporter.articleTitle }</h4>
		</div>

	</div>
	<footer class="footer"></footer>
</div>