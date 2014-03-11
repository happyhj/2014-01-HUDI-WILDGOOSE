import pickle

def get_url(press_name) :
	FILE_NAME = 'article_list.pkl'
	article_list_f = open(FILE_NAME, 'rb')
	article_list = pickle.load(article_list_f)
	article_list_f.close()

	result = article_list[press_name]
	return result

def set_url(press_name, url) :
	FILE_NAME = 'article_list.pkl'

	article_list_f = open(FILE_NAME, 'rb')
	article_list = pickle.load(article_list_f)
	article_list_f.close()

	article_list[press_name] = url
	print article_list

	article_list_f = open(FILE_NAME, 'wb')
	pickle.dump(article_list, article_list_f)
	article_list_f.close()


"""
EXAMPLE CODE
get_url('joongang')
set_url('chosun', 'http://news.chosun.com/svc/list_in/list.html?source=1&pn=')
"""