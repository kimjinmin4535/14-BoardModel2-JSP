package kr.co.ezenac.model2.board;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import kr.co.ezenac.model2.util.FileUtil;
import kr.co.ezenac.model2.util.JsFunction;

@WebServlet("/board/edit.do")
public class EditController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String idx = request.getParameter("idx");	//수정할 게시물의 일련번호 받음
		
		BoardDAO dao = new BoardDAO();
		BoardDTO dto = dao.selectView(idx);			//기존 게시물을 내용을 담은 DTO 객체 얻음
		request.setAttribute("dto", dto);  			//request 영역에 저장(바인딩)
		
		request.getRequestDispatcher("/board/edit.jsp").forward(request, response);  // 포워딩
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 파일 업로드 처리 -------------
		//업로드 시 디렉토리의 물리적 경로 확인
		String saveDirectory = request.getServletContext().getRealPath("/Uploads");
		
		//초기화 매개변수로 설정한 첨부 파일 최대 용량 확인 
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		
		//파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(request, saveDirectory, maxPostSize);
		if(mr == null) {
			//파일 업로드 실패 - 경고창 띄워주고 작성 페이지로 다시 이동
			JsFunction.alertBack(response, "첨부 파일이 제한 용량을 초과합니다.");
			return;
		}
		
		//2.파일 업로드 외 처리
		//수정 내용을 매개변수로 얻어옴 
		String idx = mr.getParameter("idx");
		String prevOfile = mr.getParameter("prevOfile");
		String prevSfile = mr.getParameter("prevSfile");
		
		String name = mr.getParameter("name");
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		
		HttpSession session =  request.getSession();
		String pass = (String) session.getAttribute("pass");
		
		BoardDTO dto = new BoardDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		
		// 원본 파일명과 저장된 파일이름 설정
		String fileName = mr.getFilesystemName("ofile");
		
		if (fileName != null) {
			//첨부파일이 있을 경우 파일명 변경, 새로운 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));		//파일 확장자
			String newFileName = now + ext;		// 새로운 파일 이름 ("업로드일시.확장자")
			
			//파일명 변경
			// File.separator : 경로 구분하는 특수 기호를 뜻함. OS에 따라 경로 표현 방법이 다름. 환경에 상관없이 코드 동작하게함.
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);	
			oldFile.renameTo(newFile);		//파일이름 변경
			
			//dto에 저장
			dto.setOfile(fileName);			//원래 파일 이름
			dto.setSfile(newFileName);		//서버에 저장된 파일 이름			
			
			//기존파일 삭제
			FileUtil.deleteFile(request, "/Uploads", prevSfile);
		}
		else {
			//첨부 파일이 없는 경우 - 기존 이름 유지
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		//DB에 수정 내용 반영
		BoardDAO dao = new BoardDAO();
		int result = dao.updatePost(dto);
		
		//성공 or 실패
		if (result == 1) { 		//수정 성공
			session.removeAttribute("pass");
			response.sendRedirect("../board/view.do?idx=" + idx);
		}
		else {					//수정 실패
			JsFunction.alertLocation(response, "비밀번호 검증을 다시 진행해 주세요.", 
									"../board/view.do?idx=" + idx);
		}
		
		
		
		
	}
}





















