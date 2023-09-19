
<%@ page contentType = "text/html; charset=UTF-8"%>
<%@ page import="java.io.*"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>

<%
	//http://211.55.75.242:9090/ems/resp/resp.jsp?rsID=4380&mID=438&rName=test&rMail=hun1110@enders.co.kr&rID=QSKY1&refMID=0&subID=0
	//파일경로
	String responseFolder = "E:\\TS4J\\TSWeb\\TS\\";

	//파일명
	String responseFileeName = "ResponseConfirmInfo.log";

	String rsID = request.getParameter("rsID");
	String mID = request.getParameter("mID");
	String subID = request.getParameter("subID");
	String refMID = request.getParameter("refMID");
	String rName = request.getParameter("rName");
    //String rName = new String(request.getParameter("rName").getBytes("8859_1"),"EUC-KR");
	String rID = request.getParameter("rID");
	String rMail = request.getParameter("rMail");


	Date now = new Date();
	SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
	String today = sf.format(now);
	sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	today = sf.format(now);

	String receiveUsrInfo = rsID+"``"+mID+"``"+subID+"``"+refMID+"``"+rName+"``"+rID+"``"+rMail+"``"+today+"\r\n";

	FileWriter fw = new FileWriter(responseFolder+File.separator+responseFileeName,true);
	fw.write(receiveUsrInfo);
	fw.close();

%>

 
