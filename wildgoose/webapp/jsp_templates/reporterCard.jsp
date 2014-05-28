<div class="card-section card-section-identity">
	<h3 class="name">
		<a href="/reporters/${ reporter.id }">${ reporter.name }</a>
	</h3>
	<p class="email">${ reporter.email }</p>
	<c:if test="${ not empty sessionScope.userId }">
		<div class="favorite"><div class="star"></div></div>
	</c:if>
	<div class="${ reporter.pressName } press-tag"></div>
</div>
<div class="card-section card-section-headline">
	<h4 class="headline">${ reporter.articleTitle }</h4>
</div>