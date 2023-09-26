package com.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
//���� �α� ������ ������ �����ϴ� Ŭ����
import java.util.Hashtable;

import com.config.Config_File_Receiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ���� �α� ���� Ŭ����
 * @version 1.0
 * @author ymkim
 */
public class utilLogWriter
{
	
	private static final Logger LOGGER = LogManager.getLogger(utilLogWriter.class.getName());

	
	public utilLogWriter() {
		// TODO Auto-generated constructor stub
		Config_File_Receiver config_File_Receiver;
		config_File_Receiver = Config_File_Receiver.getInstance();
	
	}
	
	
	  /**�α� ���� Ȯ����*/
	  public static String LOG_EXT = ".log";
	  /**������ϸ�*/
	  public static String BACK_EXT = ".bak";
	  /**�����αװ� ���̴� ����*/
	  public static String ERROR_LOG_FOLDER = Config_File_Receiver.RESPONSE_LOG_PATH;
	  //public static String ERROR_LOG_FOLDER = ".\\response\\";
	  

	  
	/**
	 * �α� ������ ������ش�.
	 * @version 1.0
	 * @author ymkim
	 * @param errorClassName ������ �߻��� Ŭ������
	 * @param errorType ��������
	 * @param comment �����޽���
	 * @param etcInfo ��Ÿ��������
	 */
	public static void setLogFormat(String errorClassName, String errorType, String comment,String etcInfo)
	{
		String errorTime = LogDateWriter.getLogTime(); //������ �߻��� ����

		String demimiter = "``";
		StringBuffer sb = new StringBuffer();
		sb.append(errorTime).append(demimiter).append(errorClassName)
				.append(demimiter).append(errorType).append(demimiter)
				.append(comment).append(demimiter).append(etcInfo);

		//������ �����α׸� �־��ش�.
		setInfoInsert(sb.toString());
		sb = null;
	}
	
	/**
	   * �����α� ó���� ���Ͽ� �����Ѵ�.
	   * @version 1.0
	   * @author ymkim
	   * @param errorInfo ���� ���� ����
	   * @return boolean true - ���� ���� ����, false - ���� ���� ����
	   */
	  public static boolean setInfoInsert(String errorInfo) {
	    String errorLogName = LogDateWriter.getLogFolderName() + LOG_EXT;
	    String errorLogFullPath = ERROR_LOG_FOLDER + errorLogName;
	    PrintWriter pw = null;

	    boolean return_value = false;

	    try {
	      pw = new PrintWriter(new FileWriter(errorLogFullPath, true));
	      pw.println(errorInfo);
	      pw.flush();

	      return_value = true;
	    }
	    catch (Exception e) {
	    	LOGGER.error(e);
	      //e.printStackTrace();
	      return_value = false;
	    }
	    finally {
	      if (pw != null) {
	        pw.close();
	      }
	    }
	    return return_value;
	  }
	
}