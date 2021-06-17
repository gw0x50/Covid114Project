# 행정안전부_재난문자방송 발령현황
# https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=3058822
# 실시간 확진자 수 파악에 사용 예정

import requests
from bs4 import BeautifulSoup
import mysql.connector
import datetime as dt
import re
import time as t

# 프로그램 시작 후 현재 시간을 갱신 시간으로 입력, 이후에 데이터 갱신 시간을 비교하여 원하는 데이터 입력
update_date = dt.datetime.now()
# update_date = dt.datetime(2021, 5, 18, 11, 0, 0) # 임의의 갱신 시간
print('Program Start! Time : ' + update_date.strftime('%Y-%m-%d %H:%M:%S'))
update_date = update_date - dt.timedelta(minutes=30) # 프로그램 시작 30분 전 데이터부터 집계 시작

# API에서 사용되는 지역명과 DB에서 사용되는 지역명을 일치 시키기 위한 list
location_list = [
    '서울특별시', '인천광역시', '광주광역시', '대전광역시', '대구광역시', '부산광역시', '울산광역시', '세종특별자치시', '경기도', '강원도',
    '충청북도', '충청남도', '전라북도', '전라남도', '경상북도', '경상남도', '제주특별자치도'
]
db_location_list = [
    'seoul', 'incheon', 'gwangju', 'daejeon', 'daegu', 'busan', 'ulsan', 'sejong', 'gyeonggi', 'gangwon',
    'chungbuk', 'chungnam', 'jeonbuk', 'jeonnam', 'gyeongbuk', 'gyeongnam', 'jeju'
]

url = 'http://apis.data.go.kr/1741000/DisasterMsg3/getDisasterMsg1List' # 서비스 URL
apiKey = 'serviceKey=' + 'API KEY' # 인증키
pageNo = 'pageNo=' + str(1) # 페이지 번호
numOfRows = 'numOfRows=' + str(240) # 한 페이지 결과 수
returnType = 'type=' + 'xml' # json, xml 사용 가능, type는 python 기본 함수라서 returnType으로 변수명 변경
params = pageNo + '&' + numOfRows + '&' + returnType # 파라메터 합치는 과정
open_url = url + '?' + apiKey + '&' + params # 요청 URL 완성

print(open_url + '\n') # 요청한 링크, 링크 누르면 데이터 확인 가능

# 1분 마다 while 반복, update_date 이후의 데이터를 입력.
# 입력 된 데이터가 있을 시 update_date 수정,
# 아닐 경우 update_date를 유지하고 코드 실행 반복
while(True):
    print('Start!')
    res = requests.get(open_url) # 응답받은 데이터를 res에 저장
    soup = BeautifulSoup(res.content, 'html.parser') # bs에 알맞게 변환

    items = soup.findAll('row') # 데이터에서 row 태그들을 찾아 ResultSet으로 반환

    result = [] # row 태그 안의 내용을 알맞게 넣을 list 생성

    for one in items:
        # 재난 문자 입력 시간들을 추출하기 위한 코드
        split_create_date = str(one.find('create_date').string).split(' ') # ex) 2021/05/18 19:01:01
        date = split_create_date[0] # ex) 2021/05/18
        time = split_create_date[1] # ex) 19:01:01
        split_date = date.split('/') 
        split_time = time.split(':')
        year = int(split_date[0]) # 년
        month = int(split_date[1]) # 월
        day = int(split_date[2]) # 일
        hour = int(split_time[0]) # 시
        minute = int(split_time[1]) # 분
        second = int(split_time[2]) # 초
        datetime = dt.datetime(year, month, day, hour, minute, second) # 추출한 시간들로 datetime 생성

        split_location = str(one.find('location_name').string).split(' ') # ex) 경기도 ㅇㅇ시
        location = split_location[0] # ex) 경기도

        msg = str(one.find('msg').string) # 재난 문자 내용
        
        # 출력 태그들
        #print('문자 발송 일시 : ' + datetime.strftime('%Y-%m-%d %H:%M:%S'))
        #print('문자 발송 날짜 : ' + date)
        #print('문자 발송 시간 : ' + time)
        #print('도, 광역, 특별시 : ' + location)
        #print('메시지 내용 : ' + msg)   
        #print()
        
        # 0시 또는 00시 기준 데이터를 생략하기위한  코드
        infoIndex = msg.find('0시')
        if msg[infoIndex - 1:infoIndex] == ' ':
            print('continue')
            continue
        elif msg[infoIndex - 1:infoIndex] == '0':
            print('continue')
            continue
        
        # 재난 문자 내용 중 '확진자'라는 단어가 시작하는 위치와 '명'으로 끝나는 위치의 index 추출
        startIndex = msg.find('확진자')
        endIndex = msg.find('명')
        
        # 확진자 수를 저장할 int 데이터 초기화
        count = 0
        
        # 두 index가 제대로 추출 되었으면
        if startIndex != -1 and endIndex != -1 and startIndex < endIndex:
            numMsg = msg[startIndex : endIndex + 1] # ex) 확진자 18명
            #print(numMsg)
            num = re.findall("\d+", numMsg) # numMsg 문자열에서 숫자만 추출 
            if(len(num) == 1): # 추출한 값 중 앞의 값을 우선으로
                count = int(num[0]) # 확진자 수 입력
                #print(count)
        #print()    
        
        # 확진자 수가 0명 초과하고 갱신되지 않은 데이터라면 
        if count > 0 and datetime > update_date:
            # 위에서 추출한 location 정보를 DB에 맞는 location 값으로 변경
            location = db_location_list[location_list.index(location)]
            data = [count, date, location] # 필요한 데이터를 list에 저장
            #print(data)
            result.append(data) # list에 추가
    
    # 삽입 될 값들 출력
    print('Result : ')
    print(result)
    
    # MariaDB 커넥션 생성
    try:
        conn = mysql.connector.connect(
            user="DB USER",
            password="DB PASSWORD",
            host="localhost",
            port=3306,
            database="DB NAME"
        )
        #print(conn)
    except mysql.connector.Error as e:
        print(e)
    
    cur = conn.cursor() # 커서 생성
    now = dt.datetime.now() # 현재 시간 받아와서
    now_str = update_date.strftime('%Y-%m-%d') # 연월일 str 데이터로 변경
    
    # covid_live table에 현재 날짜 데이터가 있는지 확인
    sql = "SELECT * FROM covid_live WHERE live_date = '" + now_str + "'" # 쿼리 생성
    cur.execute(sql) # 쿼리 실행
    sql_result = cur.fetchone() # 결과 값 입력

    # 결과값이 없다면 = 현재 날짜 데이터가 없음
    if sql_result is None: 
        print('Insert New Date! Time : ' + now_str)
        # 현재 날짜 데이터를 입력 할 쿼리 생성
        sql = "INSERT INTO covid_live VALUES ( '"+ now_str +"', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)" 
        cur.execute(sql) # 쿼리 실행
    
    for one in result:
        # 결과값을 입력할 쿼리 생성
        sql = "UPDATE covid_live SET " + one[2] + " = " + one[2] + " + %s WHERE live_date = %s"
        del one[2] # list에서 필요 없어진 데이터를 삭제
        cur.execute(sql, one) # 쿼리 실행
    
    conn.commit() # 커밋
    
    # close 문구 실행
    cur.close()
    conn.close()
    
    # 결과값이 있다면 = 데이터가 입력 되었으면
    if result:
        print('Updated Datetime!')
        update_date = dt.datetime.now() # 최근 데이터 갱신 시간을 현재 시간으로 변경
    else:
        now = dt.datetime.now() # 코드 실행 시간 출력
        print('No Update! Time : ' + now.strftime('%Y-%m-%d %H:%M:%S'))
        
    print('Recent Update Time : ' + update_date.strftime('%Y-%m-%d %H:%M:%S'))
    print('Sleep 60 Sec...')
    print()
    t.sleep(60) # 60초 뒤 코드 재실행