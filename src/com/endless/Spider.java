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
		System.out.println("������Ҫ�鿴��С˵��");
		String name = new Scanner(System.in).nextLine();
		System.out.println("������Ҫ�鿴���½���");
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
				System.err.println("�Ķ����½���Ҫ��¼");
			}
			System.err.println("�ҵ��½�Ŀ¼\n\t\t" + book.get(x - 1).getName());
			System.out.println(books);
		} catch (Exception e) {

		}
	}

	public static String getBookUrl(String urllist, String host) {
		String x = "";
		if (host.equals("hongxiu") || host.equals("qidian")) {
			// ��������
			System.err.println("��������/�������" + urllist);
			String html = gethtml(urllist);
			Pattern p = Pattern.compile("<h4>.+?</cite>");
			Matcher m = p.matcher(html);
			if (m.find()) {
				x = m.group();
				x = "https://" + x.substring(15, x.indexOf("tar") - 2) + "#Catalog";
				System.err.println("�ҵ�Ŀ¼��ַ" + x);
			}
		} else if (urllist.startsWith("http://zhannei.baidu.com/cse/search?s=14402670595036768243")) {
			// ��Ȥ��
			System.out.println("��������");
			String html = gethtml(urllist);
			// System.out.println(html);
			Pattern p = Pattern.compile("<a cpos=\"title\".+?/\" ");
			Matcher m = p.matcher(html);
			if (m.find()) {
				x = m.group();
				x = x.substring(22, x.length() - 2);
				System.err.println("�ҵ�Ŀ¼��ַ" + x);
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
		System.err.println("����С˵" + bookname);
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
		System.err.println("���ڽ���\t<<" + bookname + ">>\t" + listurl);
		if (listurl.startsWith("https://www.hongxiu.com") || listurl.startsWith("https://book.qidian.com")) {
			System.err.println("����\t�������/�����������ģ��");
			String dest = gethtml(listurl);
			// Pattern p = Pattern.compile("<a[\\s\\S]+?</a>");
			Pattern p = Pattern.compile("cid=\"//.+?<");
			Matcher m = p.matcher(dest);
			while (m.find()) {
				String fin = m.group().substring(7, m.group().length() - 1);
				String url = "https://" + fin.substring(0, fin.indexOf("\""));
				String name = fin.substring(fin.indexOf(">") + 1, fin.length());
				System.err.println("��������\t" + name + "....");
				chapter cha = new chapter();
				cha.setName(name);
				cha.setUrl(url);
				book.add(cha);
			}

		} else if (listurl.startsWith("http://www.ybdu.com")) {
			System.out.println("����ִ��\t��Ȥ��ѧ����ģ��");
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
				System.err.println("��������\t" + name + "\t" + url + "....");
				chapter cha = new chapter();
				cha.setName(name);
				cha.setUrl(url);
				book.add(cha);
			}
		}
		System.err.println("������ɣ�\t<<" + bookname + ">>��" + book.size() + "��");
		return book;
	}

	public static String gethtml(String urlstr) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			URL url = new URL(urlstr);
			System.out.println("�����ҳ��" + urlstr);
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
			System.err.println("�Ҳ�����");
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}
