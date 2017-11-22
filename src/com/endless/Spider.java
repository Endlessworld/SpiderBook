package com.endless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spider {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws URISyntaxException, UnsupportedEncodingException {
		System.out.println("请输入要查看的小说名");
		String name = new Scanner(System.in).nextLine();
		System.out.println("请输入要查看的章节数");
		int index = new Scanner(System.in).nextInt();
		showbook(name, index);
	}

	public static void showbook(String name, int x) {
		List<chapter> book = getBookList(name, "qidian");
		String str = "";
		try {
			str = gethtml(book.get(x - 1).getUrl());
			int start = str.indexOf("Content");
			int end = str.indexOf("</div", start);
			String books = str.substring(start, end).replaceAll("Content\">|<p>", "\r\n");
			if (books.length() < 500) {
				System.err.println("阅读该章节需要登录");
			}
			System.err.println("找到章节目录\n\t\t" + book.get(x - 1).getName());
			System.out.println(books);
		} catch (Exception e) {

		}
	}

	public static String getBookUrl(String urllist, String host) {
		String x = "";
		if (host.equals("hongxiu") || host.equals("qidian")) {
			// 红袖添香
			System.err.println("红袖添香/起点中文" + urllist);
			String html = gethtml(urllist);
			Pattern p = Pattern.compile("<h4>.+?</cite>");
			Matcher m = p.matcher(html);
			if (m.find()) {
				x = m.group();
				x = "https://" + x.substring(15, x.indexOf("tar") - 2) + "#Catalog";
				System.err.println("找到目录地址" + x);
			}
		} else if (urllist.startsWith("http://zhannei.baidu.com/cse/search?s=14402670595036768243")) {
			// 笔趣阁
			System.out.println("红袖添香");
			String html = gethtml(urllist);
			// System.out.println(html);
			Pattern p = Pattern.compile("<a cpos=\"title\".+?/\" ");
			Matcher m = p.matcher(html);
			if (m.find()) {
				x = m.group();
				x = x.substring(22, x.length() - 2);
				System.err.println("找到目录地址" + x);
			}
		}

		return x;
	}

	public static String Search(String bookname, String host) {
		String urlstr = null, enurl = "";
		try {
			enurl = URLEncoder.encode(bookname, "utf-8");
			// urlstr = "https://www.qidian.com/search?kw=" + enurl;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.err.println("搜索小说" + bookname);
		if (host.equals("hongxiu")) {
			urlstr = "https://www.hongxiu.com/search?kw=" + enurl;
		} else if (host.equals("qidian")) {
			urlstr = "https://www.qidian.com/search?kw=" + enurl;
		} else if (host.equals("ybdu")) {
			urlstr = "http://zhannei.baidu.com/cse/search?s=14402670595036768243&q=" + enurl;
		}
		System.out.println(urlstr);
		return getBookUrl(urlstr, host);
	}

	public static List<chapter> getBookList(String bookname, String host) {
		List<chapter> book = new ArrayList<>();
		String listurl = Search(bookname, host);
		System.err.println("正在解析\t<<" + bookname + ">>\t" + listurl);
		if (listurl.startsWith("https://www.hongxiu.com") || listurl.startsWith("https://book.qidian.com")) {
			System.err.println("启动\t起点中文/红袖添香解析模块");
			String dest = gethtml(listurl);
			// Pattern p = Pattern.compile("<a[\\s\\S]+?</a>");
			Pattern p = Pattern.compile("cid=\"//.+?<");
			Matcher m = p.matcher(dest);
			while (m.find()) {
				String fin = m.group().substring(7, m.group().length() - 1);
				String url = "https://" + fin.substring(0, fin.indexOf("\""));
				String name = fin.substring(fin.indexOf(">") + 1, fin.length());
				System.err.println("正在整理\t" + name + "....");
				chapter cha = new chapter();
				cha.setName(name);
				cha.setUrl(url);
				book.add(cha);
			}

		} else if (listurl.startsWith("http://www.ybdu.com")) {
			System.out.println("正在执行\t笔趣文学解析模块");
			listurl = listurl.replace("http", "https");
			System.out.println(listurl);
			String dest = null;
			dest = gethtml(listurl);
			Pattern p = Pattern.compile("<li><a.+?<");
			Matcher m = p.matcher(dest);
			while (m.find()) {
				String fin = m.group();
				String url = listurl + fin.substring(13, fin.indexOf("\">"));
				String name = fin.substring(fin.indexOf("\">") + 2, fin.length() - 1);
				System.err.println("正在整理\t" + name + "\t" + url + "....");
				chapter cha = new chapter();
				cha.setName(name);
				cha.setUrl(url);
				book.add(cha);
			}
		}
		System.err.println("解析完成！\t<<" + bookname + ">>共" + book.size() + "章");
		return book;
	}

	public static String gethtml(String urlstr) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			URL url = new URL(urlstr);
			System.out.println("请求的页面" + urlstr);
			InputStreamReader in;
			if (urlstr.startsWith("http://zhannei.baidu.com") || urlstr.startsWith("https://www.ybdu.com")) {
				in = new InputStreamReader(url.openStream());
				// System.out.println("charset=GBK");
			} else {
				in = new InputStreamReader(url.openStream(), "utf-8");
				// System.out.println("charset=UTF-8");
			}
			BufferedReader reader = new BufferedReader(in);
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				stringBuffer.append(temp).append("\r\n<br/>");
			}

		} catch (IOException e) {
			System.err.println("找不到！");
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}
