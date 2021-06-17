# 공공데이터활용지원센터_코로나19 예방접종센터 현황
# https://www.data.go.kr/tcs/dss/selectFileDataDetailView.do?publicDataPk=15077603
# XML 파일의 태그들이 불편하게 되어있고 태그 인식 오류가 발생하여 bs 대신 et를 사용함

import requests
import xml.etree.ElementTree as et
import mysql.connector

url = 'https://api.odcloud.kr/api/15077586/v1/centers' # 서비스 URL
apiKey = 'serviceKey=' + 'API KEY' # 인증키
page = 'page=' + str(1) # 페이지 번호
perPage = 'perPage=' + str(500) # 한 페이지 결과 수
returnType = 'returnType=' + 'XML' # JSON, XML 사용 가능, 대소문자 구분 해야 함
params = page + '&' + perPage + '&' + returnType # 파라메터 합치는 과정
open_url = url + '?' + apiKey + '&' + params # 요청 URL 완성

print(open_url + '\n') # 요청한 링크, 링크 누르면 데이터 확인 가능

res = requests.get(open_url) # 응답받은 데이터를 res에 저장
tree = et.fromstring(res.content) # et에 알맞게 변환

# col 태그 안의 내용들이 순서가 일정하지 않기 때문에 dictionary로 받아온 후 list에 재정렬
dictionaryList = [] # item 내부에 있는 dictionary를 넣을 list 생성

for item in tree.iter('item'): # 데이터에서 item 태그들을 찾고
    dic = {} # dictionary 생성
    for col in item.iter('col'): # item 태그안의 col 태그를 찾아 하나씩 반복
        dic[col.get('name')] = col.text # 찾은 정보를 dictionary에 key와 value를 알맞게 추가
        #print(col.get('name') ,":", col.text)
    #print(dic)
    dictionaryList.append(dic) # list에 추가
    #print()

result = [] # 쿼리를 편하게 사용하기 위해 위의 list 데이터를 정리하기 위한 list 생성
for item in dictionaryList:
    # list안의 dictionary 데이터를 쿼리에 알맞게 정렬
    data = (item['centerName'], item['facilityName'], item['phoneNumber'], item['sido'], item['address'], item['zipCode'], item['lat'], item['lng'])
    print(data)
    result.append(data) # list에 추가
    
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

sql = "INSERT IGNORE INTO covid_vaccine_center VALUES(%s, %s, %s, %s, %s, %s, %s, %s)" # 쿼리 생성

cur.executemany(sql, result) # 쿼리 실행

conn.commit() # 커밋 

# close 작업 실행
cur.close()
conn.close()