<%@page import="com.endless.Spider"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=request.getParameter("bookname")%></title>
<style type="text/css">
</style>
</head>
<body>
<%
request.setCharacterEncoding("gbk");
response.setContentType("text/html;charset=gbk");
String bookname = request.getParameter("bookname");
String bookurl = request.getParameter("bookurl");
String msg="";
String books="";
if (bookurl.startsWith("https://www.hongxiu.com") || bookurl.startsWith("https://book.qidian.com")) {
	String str = Spider.gethtml(bookurl);
	int start = str.indexOf("Content");
	int end = str.indexOf("</div", start);
	books = str.substring(start, end).replaceAll("Content\">","");
}else if(bookurl.startsWith("https://www.ybdu.com")){
	String str = Spider.gethtml(bookurl);
	System.out.println(request.getCharacterEncoding());
	int start = str.indexOf("contentbox")+12;
	int end = str.indexOf("ad00", start)-56;
	books = str.substring(start, end).replaceAll("contentbox","");
}
if (books.length() < 500) {
	msg="阅读该章节需要登录";
}
 request.setAttribute("books", books);
%>
<h1 ><%=msg%></h1>
	<div align="center" style="width: 30% ;top:50%">
		1><%=bookname %></h1>
	</div>
	<div style="width: 80%;margin-left: 10%;font:16px 黑体 ; background-image: url(https://qidian.gtimg.com/qd/images/read.qidian.com/theme/body_theme1_bg.9987a.png)">
		${books}
	</div>
</body>
</html>