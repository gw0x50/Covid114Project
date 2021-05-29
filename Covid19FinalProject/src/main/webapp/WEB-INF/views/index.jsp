<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최종 프로젝트</title>
<!-- js inport -->
<jsp:include page="/WEB-INF/views/scripts.jsp"/>
<!-- css import -->
<link rel="stylesheet" href="/resources/css/index.css" type="text/css">
</head>
<body>
	<div class="wrap">
		<div class="logo_container">
			<img src="/resources/images/logo.png">		
		</div>
		<div class="container">
			<jsp:include page="/WEB-INF/views/section/board.jsp"/>
			<jsp:include page="/WEB-INF/views/section/chart.jsp"/>
			<jsp:include page="/WEB-INF/views/section/map.jsp"/>
			<jsp:include page="/WEB-INF/views/section/footer.jsp"/>
		</div>
	</div>	
	<jsp:include page="/WEB-INF/views/section/chatbot.jsp"/>
</body>
</html>