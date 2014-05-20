package next.wildgoose.utility;

public class Constants {
	public static final String HEADER_CON_TYPE_JSON = "application/json; charset=UTF-8";
	public static final String HEADER_CON_TYPE_HTML = "text/html";
	public static final String HEADER_CON_TYPE_PLAIN_TEXT = "text/plain";
	
	public static final String PAGE_ERROR = "error.jsp";
	public static final String PAGE_ERROR_SEARCH_REPORTER = "SearchReporter.jsp";
	public static final String PAGE_SEARCH_REPORTER = "SearchReporter.jsp";
	public static final String PAGE_SHOW_REPORTER = "ShowReporter.jsp";
	public static final String PAGE_STATIC_ACCOUNT = "account.html";
	public static final String PAGE_STATIC_LOGIN = "login.html";
	public static final String PAGE_STATIC_REPORTER_CARD = "reporterCard.html";
	
	public static final String RESOURCE_ROOT = "/";
	public static final String RESOURCE_INDEX = "";
	public static final String RESOURCE_REPORTERS = "reporters";
	public static final String RESOURCE_SEARCH = "search";
	public static final String RESOURCE_ERROR = "error";
	public static final String RESOURCE_HTML = "subhtml";
	public static final String RESOURCE_CHECK = "check";
	public static final String RESOURCE_SIGN_PW = "pw";
	public static final String RESOURCE_SIGN_NAME = "name";
	public static final String RESOURCE_ACCOUNT = "accounts";
	public static final String RESOURCE_SESSION = "session";
	public static final String RESOURCE_NEW = "new";
	public static final String RESOURCE_MORE_RPT_CARD = "more_reporter_card";
	public static final String RESOURCE_MOST_SMR_NAME = "most_similar_names";
	
	
	public static final String ABSOLUTE_RESOURECE_TEMPLATE = "/template/";
	
	
	public static final String MSG_WENT_WRONG = "잘못된 접근";
	public static final String MSG_ERROR = "예상치 못한 문제 발생";
	
	public static final String KEYWORD_MSG = "message";
	
	public static final int NUM_OF_CARDS = 24;
	public static final int SESSION_EXPIRING_TIME = 3 * 24 * 60 * 60;
}
