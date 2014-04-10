#!/usr/bin/python
# -*- coding: utf-8 -*-
# FILE_NAME: extract_hook_word.py

import re

def get_hook_word_list() :
	f = open("hook_word_list.txt")
	line = f.readline()
	words = line.split(' ')
	f.close()
	return words

def extract_hook_word(content) :
	result = {}
	hook_word_list = get_hook_word_list()
	for word in hook_word_list :
		pattern = re.compile(word)
		how_many = len(pattern.findall(content))
		if how_many == 0 :
			continue
		result[word] = how_many
	return result

test = "숨막히는 미모의 그녀가 최근 한 온라인 커뮤니티에서 거론된 것은 충격이었다 헉헉"

kws = extract_hook_word(test)
for key in kws :
 	print key + ', ' + str(kws[key])