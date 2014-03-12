import urllib
from bs4 import BeautifulSoup

hankyung_latest_list_url = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1&sdate=&page="

def _get_html_doc (url) :
	u = urllib.urlopen(url)
	html_doc = u.read()
	u.close()
	return html_doc


def get_article_urls_with_pagenum (page) :
	hankyung_latest_list_url_with_page = hankyung_latest_list_url + str(page)
	html_doc = _get_html_doc(hankyung_latest_list_url_with_page)
	soup = BeautifulSoup(html_doc)

	article_list = soup.find(id="newslist_ty1")
	tags = article_list.findAll("h3")
	urls = []
	for tag in tags :
		urls.append ( tag.a["href"] )
	return urls


def parse_article_with_url (url) :
	try :
		html_doc = _get_html_doc(url)

		article = {}
		article["section"] = extract_section(html_doc)
		article["title"] = extract_title(html_doc)
		article["datetime"] = extract_datetime(html_doc)
		article["contents"] = extract_contetns(html_doc)
		article["author"] = extract_author(html_doc)

		return article

	except :
		return False
	

def extract_section (doc) :
	return ""

def extract_title (doc) :
	return ""

def extract_datetime (doc) :
	return ""

def extract_contetns (doc) :
	return ""

def extract_author (doc) :
	return ""


# main
articles = List()
for page in range(1,10) :
	url_list = get_article_urls_with_pagenum(page)

for url in url_list :
	articles.append(parse_article_with_url(url))

return articles
