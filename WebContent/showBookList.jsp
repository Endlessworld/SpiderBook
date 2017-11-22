<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.endless.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>章节列表</title>
<style type="text/css">
body{
background-image: url(https://qidian.gtimg.com/qd/images/read.qidian.com/theme/body_theme1_bg.9987a.png);
}
table {
/* 	//border: 1px rgb(81, 130, 187) solid; */
	position: relative;
	width:90%;
	border-left-style:0px hidden;
border-right-style:0px hidden;
}
td{
text-align:left;
padding-left:2%;
height:35px;
border-bottom: 1px red solid;

}
a{
text-decoration : none;
} 
a:HOVER {
color:red;
}
 
</style>

</head>
<body>
<% 
request.setCharacterEncoding("utf-8");
response.setContentType("text/html;charset=utf-8");
List<chapter> book =(List)request.getAttribute("booklist");
String bookname=(String)request.getAttribute("bookname");
%>
<h1 align="center">${bookname}</h1>
<table cellspacing="0" align="center" >
<%
Iterator<chapter> it = book.iterator();
int i=0;
while(it.hasNext()){
	try{
		i=i==3?0:i==2?3:i==1?2:1;
		chapter list =it.next();
		String trline="<tr><td><a href=showBook.jsp?bookurl="+list.getUrl()+"&bookname="+list.getName()+">"+list.getName()+"</a></td>"; 
		String inline="<td><a href=showBook.jsp?bookurl="+list.getUrl()+"&bookname="+list.getName()+">"+list.getName()+"</a></td>";
		String tdline=" <td><a href=showBook.jsp?bookurl="+list.getUrl()+"&bookname="+list.getName()+">"+list.getName()+"</a></td></tr>";
		out.println(i==0?trline:i==1||i==2?inline:tdline);
	}catch(Exception e){	
	}
} %>
 
</table>
</body>
</html>