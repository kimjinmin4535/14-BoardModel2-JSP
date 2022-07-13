<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>파일 첨부형 게시판 - 수정하기</title>
  <script type="text/javascript">
  	function validateForm(form) {		//폼내용 검증
		if (form.name.value == "") {
			alert("작성자를 입력하세요.")
			form.name.focus()
			return false
		}  		
		if (form.title.value == "") {
			alert("제목을 입력하세요.")
			form.title.focus()
			return false
		}
  		if (form.content.value == "") {
  			alert("내용을 입력하세요.")
  			form.content.focus()
  			return false
  		}	
	}
  	
  </script>    
</head>
<body>
	<h2> 파일 첨부형 게시판 - 수정하기</h2>
	<form action="../board/edit.do" name="writeFrm" method="post" enctype="multipart/form-data"
	      onsubmit="return validateForm(this)">
	    <!-- hidden 타입 입력상자로 일련번호, 서버에 저장된 파일명, 원본 파일명 전달함 -->  
	    <input type="hidden" name="idx" value="${dto.idx }" />
	    <input type="hidden" name="prevOfile" value="${dto.ofile}" />
	    <input type="hidden" name="prevSfile" value="${dto.sfile }" />
	      
		<table border="1" width="90%">
			<tr>
				<td>작성자</td>
				<td>
					<input type="text" name="name" style="width: 150px;" value="${dto.name }" />
				</td>
			</tr>		
			<tr>
				<td>제목</td>
				<td>
					<input type="text" name="title" style="width: 90%" value="${dto.title }"/>
				</td>
			</tr>
			<tr>
				<td>내용</td>
				<td>
					<textarea name="content" style="width: 90%; height: 100px;">${dto.content }</textarea>
				</td>
			</tr>	
			<tr>
				<td>첨부파일</td>
				<td>
					<input type="file" name="ofile" />
				</td>
			</tr>						
			<tr>
				<td colspan="2" align="center">
					<button type="submit">작성 완료</button>
					<button type="reset">RESET</button>
					<button type="button" onclick="location.href='../board/list.do'">목록 보기</button>
				</td>
			</tr>		
		</table>
	</form>
</body>
</html>