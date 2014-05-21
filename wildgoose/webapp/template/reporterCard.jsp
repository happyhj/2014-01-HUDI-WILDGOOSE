<div class="card-section card-section-identity">
	<h3 class="name">
		<a href="/reporters/${ reporterCard.id }">${ reporterCard.name }</a>
	</h3>
	<p class="email">${ reporterCard.email }</p>
	<c:if test="${ not empty sessionScope.userId }">
		<h4 class="favorite">&#x2605;</h4>
	</c:if>
	<div class="${ reporterCard.pressName } press-tag"></div>
</div>
<div class="card-section card-section-headline">
	<h4 class="headline">${ reporterCard.articleTitle }</h4>
</div>