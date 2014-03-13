newsSQL.py

oracle에서 제공하는 mysql.connector 라이브러리 이용

config = { "host":"host_IP", "user":"user_id", "password":"user_pw", "database":"db_name"}
sql_conn = NewsSQL(config)

DB연결정보를 가지는 config Dict를 NewsSQL의 인자로 전달해 객체 생성시
sql_conn은 db의 연결정보와 cursor를 보유


insert (query, dict)
string 타입의 query와, db에 전달한 정보를 담은 dict를 인자로 insert() 실행시 sql 수행

close ()
sql_conn에서 유지하던 db의 연결을 종료

