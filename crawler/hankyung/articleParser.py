#!/usr/bin/pythond
# -*- coding: utf-8 -*-

import urllib
import threading
import datetime
import re
from bs4 import BeautifulSoup
from time import sleep

class ArticleParser :

	def __init__ ( self ) :

		private = [ u"기자", u"특파원", u"통신원", u"위원" ]
		special = [ u"원장", u"회장", u"위원" ]
		
		publicKeyWord = [ u"한경닷컴", u"뉴스팀", u"제보", u"뉴스", u"연구소" ]
		publicEmails = [ u"newsinfo@hankyung.com", u"open@hankyung.com" ]
		public = { "keyWord" : publicKeyWord, "email" : publicEmails  }

		self.keyWord = { "private" : private, "public" : public, "special" : special }
		self.minLen = 60

		self.reporterPatternInFirstLine = u"[가-힝|\s|\w]*"
		self.emailPattern = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}"
		self.commentPattern = "<!--[\w|\W]*-->"

	def init ( self ) :
		self.title = ""
		self.section = ""
		self.subTitle = ""
		self.author = ""
		self.dateArr = []
		self.article = ""


	def getHtmlDoc ( self, url ) :
		self.url = url

		u = urllib.urlopen ( url )
		htmlDoc = u.read ( )
		u.close ( )		

		return htmlDoc

	# main
	def get ( self, url ) :
		try :
			# initialize variable
			self.init ( );

			# parse html doc
			htmlDoc = self.getHtmlDoc ( url )

			# search structure
			self.searchUrl ( htmlDoc )
			self.article = self.article.strip ( )

			# # print structure
			# self.printInfo ( )
			return self.makeDict ()

		except TypeError :
			print "TypeError"

	def makeDict ( self ) :
		newDict = {}
		newDict [ "url" ] = self.url.decode ( "utf-8" )
		newDict [ "title" ] = unicode ( self.title )
		newDict [ "section" ] = unicode ( self.section )
		newDict [ "content" ] = self.article + u" "
		newDict [ "author" ] = self.author.decode ( "utf-8" )
		newDict [ "datetime" ] = unicode ( self.dateArr[0] )
		

		return newDict

	def printInfo ( self ) :
		print "        title : " + self.title
		print "      section : " + self.section

		if self.subTitle :
			print "     subTitle : " + self.subTitle

		print "      article : " + self.article

		print "\n"
		print "       author : " + self.author

		print "    published : " + self.dateArr [ 0 ]
		if len ( self.dateArr ) > 1 :
			print "      updated : " + self.dateArr [ 1 ]

		print "-------------------------------------\n\n"


	def searchUrl ( self, htmlDoc ) :
		soup = BeautifulSoup ( htmlDoc )
		
		#contents
		contents = soup.find ( id = "contents" )

		##title
		title = contents.h1.find ( text=True, recursive=False )

		##status
		articleStatus = contents.h1.span
		if articleStatus :
			articleStatus = articleStatus.string

		### self.title
		self.title = articleStatus.strip ( ) + title.strip ( )

		### self.section
		self.section = contents.find ( "span", {"class":"cate"} ).a.string.strip ( )

		### self.dateArr
		## 입력: dateArr[0], 수정: dateArr[1]
		dateTemp = contents.find ("dl", {"class":"modify_date"} ).findAll ( "dd" )
		## 입력과 수정의 날짜,시간이 같은 경우 수정부분 입력 안함
		for date in dateTemp :
			if len ( date.string ) == 19 :
				if len ( self.dateArr ) == 1 and self.dateArr[0] == date.string :
					continue
				self.dateArr.append ( date.string )
		
		
		## find #newsView
		tag_a = []
		tempContents = []
		newsView = contents.find ( id = "newsView" )
		for i in newsView.contents :

			#exit condition
			if i.name == "div" :
				# exit condition / sns구조 전까지
				# 종료시 마지막 문단의 기자 정보를 검색
				if i["class"][0] == "sns_article" :
					self.author = self.findAuthor ( tempContents )
					break
				#아닐 경우 pass
				else :
					continue

				#advertisement
				if i["class"][0] == "articleAD_L" :
					continue

			#ignore
			if i.name == "br" or i.name == "iframe" :
				continue

			if i.string is None or i.string == "\n" or i.string == " " :
				continue

			if i.string.find ( u"서브 제목" ) != -1 or i.string.find ( u"//서브 제목" ) != -1 :
				continue
			
			tempContents.append ( i.string )
			self.article += i.string


	def findAuthor ( self, contents ) :
		##### 기존 로직에서 변경
		##### 이메일, 관련단어를 찾으면 그 내용을 찾아주는 로직
		lineList = self.pickUpLine ( contents )
		author = ""

		if lineList [ "private" ] :
			for line in lineList [ "private" ] :
				author = line + author
			return author.encode ( "utf-8" ).strip ( )

		if lineList [ "special" ] :
			return lineList [ "special" ].encode ( "utf-8" ).strip ( )

		if lineList [ "public" ] :
			for line in lineList [ "public" ] :
				author = line + author
			return author.encode ( "utf-8" ).strip ( )

		return "anonymous"



	def categorizeByAuthorCode ( self, line, privateFlag ) :

		# email 보유여부 확인
		emails = re.findall ( self.emailPattern, line )
		returnCodeList = [ False, "public", "private" ]
		returnCode = False

		if emails :
			for email in emails :
				if email in self.keyWord [ "public" ] [ "email" ] :
					returnCode = 1
				else :
					privateFlag [ "email" ] = True
					returnCode = 2

					# print line
					# print len ( line )

		# keyWord 보유여부 확인
		## public keyWord
		if not privateFlag [ "email" ] and not privateFlag [ "keyWord" ] :
			for word in self.keyWord [ "public" ] [ "keyWord" ] :
				index = line.find ( word )
				if index != -1 :
					if not returnCode :
						returnCode = 1
					break
		## private keyWord
		for word in self.keyWord [ "private" ] :
			# print " in privatWord for loop"
			index = line.find ( word )
			if index != -1 :
				if not returnCode :
					privateFlag [ "keyWord" ] = True
					returnCode = 2
				break


		if returnCode != False :
			return returnCodeList [ returnCode ]

		return -1

	## 라인의 앞뒤 공백을 제거 후 마지막에 마침표가 있으면 문단이라고 판단
	## 특히 첫번째 문단 마지막에 마침표가 있는 경우 기자정보가 없다고 판단하고 종료
	def hasPeriod ( self, line ):
		line = line.strip ( )
		if line [ len ( line ) - 1 ] == "." :
			return True
		return False


	# 특정 단어가 있는 경우 그 내용을 뽑아서 리스트로 출력해주는 로직
	def pickUpLine ( self, contents ) :

		# authorCode에 따라서 분류
		lineList = { "public" : [ ], "private" : [ ], "special" : "" }
		privateFlag = { "email" : False, "keyWord" : False }
		contentsLen = len ( contents )
		specialLineNum = contentsLen - 1
		author = ""

		lineNum = 0
		exitLineNum = 4
		for line in reversed ( contents ) :
			
			# 종료조건
			lineNum += 1
			if lineNum > exitLineNum :
				break;

			# print "lineNum: " + str ( lineNum )

			## 라인의 앞뒤 공백을 제거 후 마지막에 마침표가 있으면 문단이라고 판단
			## 특히 첫번째 문단 마지막에 마침표가 있는 경우 기자정보가 없다고 판단하고 종료
			period = self.hasPeriod ( line )
			if period :
				if lineNum == 0 :
					break
				else :
					continue

			## 현재의 라인넘버 출력 
			# print "lineNum: " + str ( lineNum )
			code = self.categorizeByAuthorCode ( line, privateFlag )

			## author code를 발견시
			if code != -1 :
				lineList [ code ].append ( line )

				## private code 발견시, specialLine을 다시 찾을 필요가 없으므로 제거
				if code == "private" :
					specialLineNum = False
				## public code 발견시, specialLine의 위치를 public라인 바로 위로 전달
				elif code == "public" :
					specialLineNum = contentsLen - 1 - lineNum
					## 그 위치가 0이하인 경우 specialLine을 다시 확인하지 않으므로 제거
					if specialLineNum <= 0 :
						specialLineNum = False

		######
		# linelist [ "private" ]이 존재하지 않는 경우 special여부 확인
		if not lineList [ "private" ] :
			if specialLineNum != False :
				specialLine = contents [ specialLineNum ]
				period = self.hasPeriod ( specialLine )

				if not period and len ( specialLine ) < self.minLen :
					for word in self.keyWord [ "special" ] :
						index = specialLine.find ( word )
						if index != -1 :
							lineList [ "special" ] = specialLine


		# lineList [ "private" ] 이 존재하나 불완전한 경우
		else :
			line = lineList [ "private" ] [ 0 ]
			lineLen = len ( line )

			## 문단의 길이가 필요이상으로 긴 경우
			if lineLen > self.minLen :
				emailLen = 0
				## email이 존재하는 경우 그 길이만큼 제거 
				if privateFlag [ "email" ] :
					email = re.findall ( self.emailPattern, line )
					emailLen = len ( email[0] )

				index = line [ 0 : lineLen - emailLen ].rfind ( "." )
				lineList [ "private" ] [ 0 ] = line [ index + 1 : lineLen ].strip ( )


		# print "-----------------"
		# if lineList [ "private"] :
		# 	for line in lineList [ "private"] :
		# 		print line
		# print "-----------------\n"

	
		return lineList


#main

# print "Start\n\n"

# ap = ArticleParser ( )
# ap.get ( "http://www.hankyung.com/news/app/newsview.php?aid=201403095513g" )

# 죽음

# 에러
# 텐아시아
# http://www.hankyung.com/news/app/newsview.php?aid=2014030733031
# 서브제목
# http://www.hankyung.com/news/app/newsview.php?aid=2014030733931
#http://www.hankyung.com/news/app/newsview.php?aid=2014030733131


# print "\n\nEnd"






