<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>小说搜</title>
<style type="text/css">
body{
background-image: url(https://qidian.gtimg.com/qd/images/read.qidian.com/theme/body_theme1_bg.9987a.png);
}
.fo{
height: 100px;
width: 300px;
margin-left: 35%;
margin-top: 10%;
}
</style>
</head>
	<body>
		<form action="SearchSer" method="post">
			<fieldset class="fo" align="center" >
				<legend align='center'>小说搜索</legend>
				<input  type="text" name ="bookname">
					<select name="Searchost" >
						<option value="qidian">起点中文</option>
						<option value="hongxiu">红袖添香</option>
						<option value="ybdu" selected="selected">笔趣阁</option>
					</select>
				<br/><br/> 
				<input style="width:100px ;height:35px;" type="submit" value="搜索">
			</fieldset>
		</form>
	</body>
</html>