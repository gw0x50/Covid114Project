<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<
<div class="chat">
	<div class="chat_header">
<!-- PC 카카오톡 버튼 -->
<a href="javascript:void kakaoChatStart()" class="kakaoChatPc hidden-md hidden-sm hidden-xs">
    <img src="<%=request.getContextPath()%>/resources/images/kakao_logo.png" width="72px" height="72px">
</a>

</div>
</div>


<!-- 카카오톡 채널 스크립트 -->
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<script type='text/javascript'>
    Kakao.init('62efded51f4c41f3602e72396ecafaad'); 
    function kakaoChatStart() {
        Kakao.Channel.chat({
            channelPublicId: '_pEfMs' 
        });
    }
</script>