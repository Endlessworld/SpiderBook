package com.endless;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SearchSer")
public class SearchSer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("This's Ser!");
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		String bookname = req.getParameter("bookname");
		String host = req.getParameter("Searchost");
		List<chapter> booklist = Spider.getBookList(bookname, host);
		req.setAttribute("bookname", bookname);
		req.setAttribute("booklist", booklist);
		req.getRequestDispatcher("showBookList.jsp").forward(req, resp);
	}
}
