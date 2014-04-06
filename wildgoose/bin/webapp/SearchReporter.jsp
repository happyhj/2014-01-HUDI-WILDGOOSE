<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Reporter</title>
</head>
<body>
<!--  
<form action="/HelloServlet" method="get">
<input type="search" id="query" name="query" value="${ requestScope.search_query }"/>
<input type="submit" value="SEARCH" onclick="test()")/>
</form>
-->
<input type="search" id="query" value="<%= request.getAttribute("search_query") %>"/>
<a href="/" id="action"><button>SEARCH</button></a>
	<div class="search_result">
	<ul>
		<c:forEach var="reporter" items="${ requestScope.reporters }">
			<li>
				<div>
					${ reporter.email }
					${ reporter.author_info }
					${ reporter.press_name }
					${ reporter.article_title }
					${ reporter.article_URL }
				</div>
			</li>
		</c:forEach>
	</ul>
	</div>
<script>
var inputEl = document.getElementById("query");
var linkEl = document.getElementById("action");
var last_value;
inputEl.focus();
inputEl.addEventListener("keyup",function(e) {	
	if(document.activeElement === this) {
		if(last_value !== this.value) {
			console.log(this.value);
			if(this.value.length >= 1) {
				linkEl.setAttribute("href","/SearchReporter?q="+ encodeURIComponent(this.value));
			}
			else 
				linkEl.setAttribute("href","/");
			last_value = this.value;
		}	
		if (e.keyCode === 13) {
		    linkEl.click();
		}
	}
},false);

</script>
</body>
</html>