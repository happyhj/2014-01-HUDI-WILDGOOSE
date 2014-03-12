import urllib
from bs4 import BeautifulSoup

_hankyung_latest_list_url = "http://www.hankyung.com/news/app/newslist_all.php?tab=&iscts=&popup=1&sdate=&page="
_date_len = 19
_paragraph_list_of_article = []


private = [ u"기자", u"특파원", u"통신원", u"위원" ]
special = [ u"원장", u"회장", u"위원" ]

publicKeyWord = [ u"한경닷컴", u"뉴스팀", u"제보", u"뉴스", u"연구소" ]
publicEmails = [ u"newsinfo@hankyung.com", u"open@hankyung.com" ]
public = { "keyWord" : publicKeyWord, "email" : publicEmails  }

_key_word = { "private" : private, "public" : public, "special" : special }
_min_len = 60

_reporter_pattern_in_first_line = u"[가-힝|\s|\w]*"
_email_pattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
_comment_pattern = "<!--[\w|\W]*-->"


def _get_html_doc (url) :
	u = urllib.urlopen(url)
	html_doc = u.read()
	u.close()

	return html_doc


def _get_soup_in_container (html_doc, container) :
	soup = BeautifulSoup(html_doc)
	soup_in_container = soup.find(id=container)

	return soup_in_container


## 라인의 앞뒤 공백을 제거 후 마지막에 마침표가 있으면 문단이라고 판단
## 특히 첫번째 문단 마지막에 마침표가 있는 경우 기자정보가 없다고 판단하고 종료
def _has_period(paragraph):
	paragraph = paragraph.strip ( )
	if paragraph[len(paragraph)-1 ] == "." :
		return True
	return False


def _categorize_by_author_code(paragraph, private_flag) :
	# email 보유여부 확인
	emails = re.findall(_email_pattern, line)
	return_code_list = [False, "public", "private"]
	return_code = False

	if emails :
		for email in emails :
			if email in _key_word["public"]["email"] :
				return_code = 1
			else :
				private_flag["email"] = True
				return_code = 2

	# keyWord 보유여부 확인
	## public keyWord
	if not private_flag["email"] and not private_flag["keyWord"] :
		for word in _key_word["public"]["keyWord"] :
			index = line.find(word)
			if index != -1 :
				if not return_code :
					return_code = 1
				break

	## private keyWord
	for word in _key_word["private"] :
		index = line.find(word)
		if index != -1 :
			if not return_code :
				private_flag["keyWord"] = True
				return_code = 2
			break


	if return_code != False :
		return return_code_list[return_code]

	return -1




# 특정 단어가 있는 경우 그 내용을 뽑아서 리스트로 출력해주는 로직
def _pick_up_line (contents) :

	# authorCode에 따라서 분류
	line_list = {"public":[], "private":[], "special":""}
	private_flag = {"email":False, "keyWord":False }
	contents_len = len(contents)
	special_line_num = contents_len - 1
	author = ""

	line_num = 0
	exit_line_num = 4
	for line in reversed(contents) :
		
		# 종료조건
		line_num += 1
		if line_num > exit_line_num :
			break;

		## 라인의 앞뒤 공백을 제거 후 마지막에 마침표가 있으면 문단이라고 판단
		## 특히 첫번째 문단 마지막에 마침표가 있는 경우 기자정보가 없다고 판단하고 종료
		period = _has_period(line)
		if period :
			if line_num == 0 :
				break
			else :
				continue

		## 현재의 라인넘버 출력 
		# print "lineNum: " + str ( lineNum )
		code = _categorize_by_author_code(line, private_flag)

		## author code를 발견시
		if code != -1 :
			line_list[code].append(line)

			## private code 발견시, specialLine을 다시 찾을 필요가 없으므로 제거
			if code == "private" :
				special_line_num = False
			## public code 발견시, specialLine의 위치를 public라인 바로 위로 전달
			elif code == "public" :
				special_line_num = contents_len - 1 - line_num
				## 그 위치가 0이하인 경우 specialLine을 다시 확인하지 않으므로 제거
				if special_line_num <= 0 :
					special_line_num = False

	######
	# linelist [ "private" ]이 존재하지 않는 경우 special여부 확인
	if not line_list["private"] :
		if special_line_num != False :
			special_line = contents[special_line_num]
			period = _has_period(special_line)

			if not period and len(special_line) < _min_len :
				for word in key_word["special"] :
					index = special_line.find ( word )
					if index != -1 :
						line_list["special"] = special_line


	# lineList [ "private" ] 이 존재하나 불완전한 경우
	else :
		line = line_list["private"][0]
		line_len = len(line)

		## 문단의 길이가 필요이상으로 긴 경우
		if line_len > _min_len :
			email_len = 0
			## email이 존재하는 경우 그 길이만큼 제거 
			if private_flag["email"] :
				email = re.findall(_email_pattern, line)
				email_len = len(email[0])

			index = line[0:line_len - email_len ].rfind(".")
			line_list["private"][0] = line[index + 1:line_len].strip()

	return lineList



def get_article_urls_with_pagenum (page) :
	hankyung_latest_list_url_with_page = hankyung_latest_list_url+str(page)
	html_doc = _get_html_doc(hankyung_latest_list_url_with_page)

	article_list = _get_soup_in_container(html_doc, "newslist_ty1")
	tags = article_list.findAll("h3")
	urls = []
	for tag in tags :
		urls.append(tag.a["href"])
	return urls


def parse_article_with_url (url) :
	try :
		html_doc = _get_html_doc(url)

		article = {}
		article["url"] = url
		article["section"] = extract_section(html_doc)
		article["title"] = extract_title(html_doc)
		article["datetime"] = extract_datetime(html_doc)
		article["contents"] = extract_contetns(html_doc)
		# article["author"] = extract_author(html_doc)

		return article

	except :
		return False
	

def extract_section (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")
	section = contents.find("span",{"class":"cate"} ).a.string.strip()

	return section

def extract_title (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")

	title = contents.h1.find (text=True, recursive=False)
	status = contents.h1.span
	if status :
		status = status.string
	title = status.strip()+title.strip()

	return title


def extract_datetime (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")

	date_list = []
	date_list = contents.find("dl",{"class":"modify_date"}).findAll("dd")
	## 입력과 수정의 날짜,시간이 같은 경우 수정부분 입력 안함
	for date in date_list :
		if len(date.string) == date_len :
			if len(date_list) == 1 and date_list[0] == date.string :
				continue
			date_list.append(date.string)
	#입력된 시간의 date_list만 반환
	return date_list[0]


def extract_contents (html_doc) :
	contents = _get_soup_in_container(html_doc, "contents")

	news_view = contents.find(id="newsView")
	for dom in news_view.contents :
		#exit condition
		if dom.name == "div" :
			# sns구조 전까지 검색 후 종료
			if dom["class"][0] == "sns_article" : break
			else : continue

			#advertisement
			if dom["class"][0] == "articleAD_L" :
				continue

		#ignore
		if dom.name == "br" or dom.name == "iframe" :
			continue
		if dom.string is None or dom.string == "\n" or dom.string == " " :
			continue
		if dom.string.find ( u"서브 제목" ) != -1 or dom.string.find ( u"//서브 제목" ) != -1 :
			continue
		
		_paragraph_list_of_article.append(dom.string)
		contents_of_article += dom.string

	return contents_of_article


def extract_author (html_doc) :
	##### 기존 로직에서 변경
	##### 이메일, 관련단어를 찾으면 그 내용을 찾아주는 로직
	line_list = pick_up_line(contents)
	author = ""

	if line_list["private"] :
		for line in line_list["private"] :
			author = line + author
		return author.strip()

	if line_list["special"] :
		return line_list["special"].strip()

	if line_list["public"] :
		for line in line_list["public"] :
			author = line + author
		return author.strip()

	return "anonymous"



# main
articles = []
for page in range(1,2) :
	url_list = get_article_urls_with_pagenum(page)

for url in url_list :
	articles.append(parse_article_with_url(url))

#print
for article in articles :
	print "url: " + article["url"]
	print "title: " + article["title"]
	print "------------------------\n"




