/*
* 클래스명: DBRecorder.java
* 버전정보: JDK 1.4.1
* 요약설명: 설정 파일
* 작성일자: 2003-04-04 하광범
 */

package com.response;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.text.*;

import com.config.Config_File_Receiver;
import com.log.LogWriter;
import com.custinfo.safedata.*;
import com.util.EncryptUtil;
import com.util.utilLogWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Response extends Thread
{
	private static final Logger LOGGER = LogManager.getLogger(Response.class.getName());
	
	//수신확인테이블 검색용쿼리
	public String RES_SEARCH_QUERY = "SELECT RSID FROM TS_RESPONSELOG WHERE RSID=?";
	//수신확인테이블 입력용쿼리
	public static String RES_INSERT_QUERY = "INSERT INTO TS_RESPONSELOG VALUES(?,?,?,?,?,?,?,?)";
	//수신확인통계테이블 검색용쿼리
	public static String RES_STATIC_SEARCH_QUERY = "SELECT COUNT(*) FROM TS_RESPONSELOG WHERE MID=?";
	//수신확인통계테이블 수정용쿼리
	public static String RES_STATIC_UPDATE_QUERY = "UPDATE TS_RESPONSE_RSINFO SET RSCOUNT = ? WHERE MID=?";
	
	
	
	Config_File_Receiver config_File_Receiver;
	LogWriter logWriter;
//	NeoQueue_DirManager neoQueue_DirManager;

	Vector vLogFileList;
	Vector vRollbackLogFileList;

	String startTime;

	public Response()
	{
		
		//config_File_Receiver = Config_File_Receiver.getInstance();
		Config_File_Receiver CFR = new Config_File_Receiver();
		config_File_Receiver = CFR.getInstance();
		
		logWriter = new LogWriter();

		vLogFileList = new Vector();
		vRollbackLogFileList = new Vector();
//		neoQueue_DirManager = new NeoQueue_DirManager();
//		neoQueue_DirManager.start();

		start();
		
		new DemonCheck_Response("Response").start();
	}

	public void run()
	{
		String RESPONSE_CONFIRM_FULL_PATH = "";
		String BACK_EXT = ".bak";
		
		while( true )
		{
			try
			{
				RESPONSE_CONFIRM_FULL_PATH = config_File_Receiver.RESPONSE_CONFIRM_FULL_PATH;
				File rcLog = new File(RESPONSE_CONFIRM_FULL_PATH);
				if(rcLog.exists()) {
					insertToDBFromResponseConfirmLog(rcLog, RESPONSE_CONFIRM_FULL_PATH, BACK_EXT);	
				}
				sleep(config_File_Receiver.RESPONSE_INSERT_PERIOD*1000*60);
				//sleep(1000*60*5);
			}
			catch(Exception e) {
				LOGGER.error(e);
				logWriter.logWrite("Response", "run()", e);
			}
		}
	}
	
	//수신로그파일의 내용을 DB에 넣어준다.
	public boolean insertToDBFromResponseConfirmLog(File rcLog, String RESPONSE_CONFIRM_FULL_PATH, String BACK_EXT)
	{
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		
		File reRcLog = new File(RESPONSE_CONFIRM_FULL_PATH+BACK_EXT);
		//수신로그파일의 이름을 바꿔준다.
		if(!(rcLog.renameTo(reRcLog)))
		{
			//그전에 남아있는 쓰레기가 있는 모양이니 지워버린다.
			if(reRcLog.exists()) {
				reRcLog.delete();
			}
			//LOGGER.info("전에있던 쓰레기 데이타를 지워버린다");
		}
		
		//수신로그의 파일을 읽어들인다.
		
		String tempStr= "";
		StringTokenizer st = null;
		
		String rsID = "";
		String mID = "";
		String subID = "";
		String refMID = "";
		String rName = "";
		String rID = "";
		String rMail = "";
		String rsDate = "";
		ArrayList mIDList = new ArrayList();
		
		Connection con_work = DBConnection.getConnection();
		ResultSet rs =null;
		PreparedStatement res_pstmt = null;
		PreparedStatement insert_pstmt = null;
		
		
		boolean return_value = false;
		
		BufferedReader br = null;
		
	      ///암호화/복호화
	      String ALGORITHM = "PBEWithMD5AndDES";
	      String KEYSTRING = "ENDERSUMS";
	      EncryptUtil enc =  new EncryptUtil();
	      
	      CustInfoSafeData safeDbEnc = new CustInfoSafeData();
	      
	      
		try
		{
			if(reRcLog.exists()) {
				br = new BufferedReader(new FileReader(reRcLog));
				
				res_pstmt = con_work.prepareStatement(RES_SEARCH_QUERY);
				insert_pstmt = con_work.prepareStatement(RES_INSERT_QUERY);
				
				while((tempStr = br.readLine())!=null)
				{
					
					if(tempStr.contains("``null") || tempStr.contains("null``")) {
						
					}else {
							st = new StringTokenizer(tempStr,"``");
							
							if(st.countTokens()==8) {
								while(st.hasMoreTokens())
								{
									rsID = st.nextToken();
									mID = st.nextToken();
									subID = st.nextToken();
									refMID = st.nextToken();
									rName = st.nextToken();
									rID = st.nextToken();
									rMail = st.nextToken();
									rsDate = st.nextToken();
			
									//암호화
									if("Y".equals(Config_File_Receiver.ENC_YN)) {
										rMail = enc.getJasyptEncryptedFixString(ALGORITHM, KEYSTRING, rMail);
										//rMail = safeDbEnc.getEncrypt(rMail, "NOT_RNNO");
									}
									
									if(rID.equals("null")) rID = null;
									if(rName.equals("null")) rName = null;
			
				/*					
									LOGGER.info("rsID:"+rsID);
									LOGGER.info("mID:"+mID);
									LOGGER.info("subID:"+subID);
									LOGGER.info("refMID:"+refMID);
									LOGGER.info("rName:"+rName);
									LOGGER.info("rID:"+rID);
									LOGGER.info("rMail:"+rMail);
									LOGGER.info("rsDate:"+rsDate);
				*/
									mIDList.add(mID);
								}
								
								//이미 있는 내용인지 체크한다.(있으면 넣지 말구 없으면 넣는다.)
								res_pstmt.clearParameters();
								res_pstmt.setInt(1, Integer.parseInt(rsID));
								rs = res_pstmt.executeQuery();
								if(rs.next())
								{
									//LOGGER.info("중복수신 : " +rsID + "  "+rMail);
									
								}else
								{
									
									//LOGGER.info("수신확인 로그를 디비에 입력합니다.");
									//디비에 내용을 넣는다.
								
									insert_pstmt.clearParameters();
									insert_pstmt.setString(1, rsID);
									insert_pstmt.setString(2, mID);
									insert_pstmt.setString(3, subID);
									insert_pstmt.setString(4, refMID );
									insert_pstmt.setString(5, rsDate);
									insert_pstmt.setString(6, rID);
									insert_pstmt.setString(7, rName);
									insert_pstmt.setString(8, rMail);
									insert_pstmt.executeUpdate();
									
									//updateResStaticCount(mIDList);
									
									LOGGER.info("수신확인 : "+ rsID + "  "+rMail  +  "  " +time1);
									
									//수신확인 이력 관리 (respose 폴더에 2021_09_27.log 파일 생성)
									utilLogWriter.setLogFormat("Response", "수신확인 완료", "rsID:"+rsID, "mId:"+mID);
								}
								
								Response rsp = new Response();
								rsp.updateResStaticCount(mIDList);
			
			
							}
							
							//수신통계를 위한 테이블(Response_rsInfo)에도 값을 넣어준다.
			
							
							
							}
							
					}
				if(br!=null) br.close();
				//수신로그를 지워준다.
				if(reRcLog.exists()) {
					reRcLog.delete();
				}
				
				//con_work.commit();
				
				return_value = true;
			}
				
		}catch(Exception e)
		{
			LOGGER.error(e);
			//e.printStackTrace();
			//에러 로그에 남겨준다.
//			ErrorLogGenerator.setErrorLogFormat("LogFileManager", ReserveStatusCode.SQL_ERROR_TYPE,ReserveStatusCode.RESPONSE_LOG_FAIL_COMMENT,mID);
			return_value = false;
		}finally
		{
			try
			{
				if(br != null) br.close();
				if(rs != null) rs.close();
				if(insert_pstmt != null) insert_pstmt.close();
				if(res_pstmt != null) res_pstmt.close();
				if(con_work !=null) con_work.close();
			}catch(Exception e)
			{
				LOGGER.error(e);
				//e.printStackTrace();
			}
		}
		return return_value;
	}

	
	//수신통계를 위한 테이블(Response_rsInfo)에도 값을 넣어준다.
		public boolean updateResStaticCount(ArrayList mIDList)
		{
			Connection con_work = DBConnection.getConnection();
			ResultSet rs =null;
			PreparedStatement pstmt = null;
			PreparedStatement search_pstmt = null;
			
			
			boolean return_value = false;
			
			try
			{
				int mIDListSize = mIDList.size();
				
				pstmt = con_work.prepareStatement(RES_STATIC_SEARCH_QUERY);
				
				search_pstmt = con_work.prepareStatement(RES_STATIC_UPDATE_QUERY);
				
				for(int i=0; i<mIDListSize; i++)
				{
					//LOGGER.info("검색할 MID:"+(String)mIDList.get(i));
					pstmt.clearParameters();
					pstmt.setString(1, (String)mIDList.get(i));
					rs = pstmt.executeQuery();
				
					if(rs.next())
					{
						int rsCnt = rs.getInt(1);
						//LOGGER.info("업데이트할 내용:"+rsCnt);
						//LOGGER.info("업데이트할 내용:"+(String)mIDList.get(i));
						search_pstmt.clearParameters();
						search_pstmt.setInt(1, rsCnt);
						search_pstmt.setString(2, (String)mIDList.get(i));
						search_pstmt.executeUpdate();
					}
				}

				//con_work.commit();
				
				return_value = true;
			}catch(Exception e)
			{
				LOGGER.error(e);
				//e.printStackTrace();
				//에러 로그에 남겨준다.
//				ErrorLogGenerator.setErrorLogFormat("LogFileManager", ReserveStatusCode.SQL_ERROR_TYPE,ReserveStatusCode.RESPONSE_LOG_FAIL_COMMENT,mID);
				return_value = false;
			}finally
			{
				try
				{
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(search_pstmt != null) search_pstmt.close();
					if(con_work !=null) con_work.close();
					
				}catch(Exception e)
				{
					LOGGER.error(e);
					//e.printStackTrace();
				}
			}
			return return_value;
		}
	
	
	private synchronized String getLogDate()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy_MM_dd");
		return fmt.format(new java.util.Date());
	}

	private synchronized String getLogTime()
	{
		String tmpDateStr = "";
		Calendar rightNow = Calendar.getInstance();
		int tmpHour = 0;

		if( rightNow.get(Calendar.AM_PM) == 1 ) {
			tmpHour=12;
		}

		tmpDateStr = (new StringBuffer().append(tmpHour).append(rightNow.get(Calendar.HOUR))
					  .append(":").append(rightNow.get(Calendar.MINUTE)).append(":")
					  .append(rightNow.get(Calendar.SECOND))).toString();

		return tmpDateStr;
	}


	public static void main(String[] args) {
		LOGGER.info("TS Response start..");
		new Response();
	}


}
