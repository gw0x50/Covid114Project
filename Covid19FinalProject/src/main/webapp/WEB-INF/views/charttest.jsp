<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>최종 프로젝트</title>
<!-- jquery  --> 
<script type="text/javascript" src="/resources/js/jquery-3.2.1.min.js"></script>
<!-- chart.js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.2.1/chart.min.js"></script>
<!-- index 관련 script -->
<script src="/resources/js/index.js"></script>
<!-- chart 관련 script -->
<script src="/resources/js/chart.js"></script>
<!-- map 관련 script -->
<script src="/resources/js/map.js"></script>
<!-- index 관련 css -->
<link rel="stylesheet" href="/resources/css/index.css" type="text/css">
<!-- chart 관련 css -->
<link rel="stylesheet" href="/resources/css/chart.css" type="text/css">
<!-- map 관련 css -->
<link rel="stylesheet" href="/resources/css/map.css" type="text/css">
<script>
$(document).ready(function(){
	
});
</script>

</head>
<body>
${sevenDays[0][1].result_date}
	<div class="wrap">
		<div class="container">
			<img src="/resources/images/logo.png">
			<jsp:include page="/WEB-INF/views/section/chart.jsp"/>
			<jsp:include page="/WEB-INF/views/section/map.jsp"/>
		</div>
	</div>
</body>
</html>