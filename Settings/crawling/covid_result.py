# 공공데이터활용지원센터_보건복지부 코로나19 시·도발생 현황
# https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043378

import requests
from bs4 import BeautifulSoup
import mysql.connector
import datetime as dt

date = dt.datetime.now() # 20210514일 경우 20210513의 확진자 집계 정보가 나옴
date = date.strftime('%Y%m%d')
print(date)

url = 'http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19SidoInfStateJson' # 서비스 URL
apiKey = 'serviceKey=' + 'API KEY' # 인증키
pageNo = 'pageNo=' + str(1) # 페이지 번호
numOfRows = 'numOfRows=' + str(200) # 한 페이지 결과 수
startCreateDt = 'startCreateDt=' + str(date) # 데이터 생성일 시작 범위
endCreateDt = 'endCreateDt=' + str(date) # 데이터 생성일 종료 범위
params = pageNo + '&' + numOfRows  + '&' + startCreateDt  + '&' + endCreateDt # 파라메터 합치는 과정
open_url = url + '?' + apiKey + '&' + params # 요청 URL 완성

print(open_url + '\n') # 요청한 링크, 링크 누르면 데이터 확인 가능

res = requests.get(open_url) # 응답받은 데이터를 res에 저장
soup = BeautifulSoup(res.content, 'html.parser') # bs에 알맞게 변환

items = soup.findAll('item') # 데이터에서 item 태그들을 찾아 ResultSet으로 반환

result = [] # item 데이터를 넣을 tuple 생성

for one in items:
    print('sequence : ' + one.find('seq').string)
    print('수정 시간 : ' + one.find('createdt').string) # 나중에는 수정 시간 하나만 출력해도 될 거 같음
    print('지역 : ' + one.find('gubun').string)
    print('일일 확진자 수 : ' + one.find('incdec').string)
    print('누적 확진자 수 : ' + one.find('defcnt').string)
    print('누적 완치자 수 : ' + one.find('isolclearcnt').string)
    print('누적 사망자 수 : ' + one.find('deathcnt').string)
    sequence = str(one.find('seq').string)
    createDt = one.find('createdt').string[:10] # ex) 2021-05-18
    splitDate = createDt.split('-')
    #실제로는 하루 전 날의 통계 데이터이기 때문에 str을 datetime로 변환 후 -1일 연산
    date = dt.date(int(splitDate[0]), int(splitDate[1]), int(splitDate[2])) - dt.timedelta(days=1)
    result_date = date.strftime('%Y-%m-%d') # 양식에 맞게 str로 변환
    location = str(one.find('gubun').string)
    increment_count = str(one.find('incdec').string)
    total_count = str(one.find('defcnt').string)
    clear_count = str(one.find('isolclearcnt').string)
    death_count = str(one.find('deathcnt').string)
    
    data = (sequence, result_date, location, increment_count, total_count, clear_count, death_count) # tuple 생성
    result.append(data) # list에 추가
    print()

# MariaDB 커넥션 생성

try:
    conn = mysql.connector.connect(
        user="DB USER",
        password="DB PASSWORD",
        host="localhost",
        port=3306,
        database="DB NAME"
    )
    print(conn)
except mysql.connector.Error as e:
    print(e)


cur = conn.cursor() # 커서 생성

sql = "INSERT IGNORE INTO covid_result VALUES(%s, %s, %s, %s, %s, %s, %s)" # 쿼리 생성

cur.executemany(sql, result) # result 만큼의 쿼리 실행


conn.commit() # 커밋

# close 작업 실행
cur.close() 
conn.close()