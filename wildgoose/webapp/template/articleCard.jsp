<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="card-section card-section-article">
	<a href="${ articleCard.url }"><span></span></a>
	<div class="article-title">
		<h1>${ articleCard.title }</h1>
	</div>
	<div class="article-preview">
		<p>${ articleCard.content }</p>
	</div>
</div>
<div class="card-section card-section-footer">
	<a href="reporters/${ articleCard.authorId }"><span></span></a>
	<c:set var="date" value="${fn:split(articleCard.datetime, ' ')}" />
	${ articleCard.name } 기자의 ${ date[0] }일자 기사입니다
</div>
