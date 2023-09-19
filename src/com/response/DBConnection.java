package com.response;

import java.sql.Connection;
import java.sql.DriverManager;

import com.config.Config_File_Receiver;
import com.util.EncryptUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection
{
	
	private static final Logger LOGGER = LogManager.getLogger(DBConnection.class.getName());
	
	static Config_File_Receiver config_File_Receiver ;
	
	public static Connection dbConn;
	  public static Connection getConnection() 
	  {
	    Connection conn = null;
	    
        ///��ȣȭ/��ȣȭ
        String ALGORITHM = "PBEWithMD5AndDES";
        String KEYSTRING = "ENDERSUMS";
        EncryptUtil enc =  new EncryptUtil();
        
	    try{

	    	String user=config_File_Receiver.USER;;
	        String pw=config_File_Receiver.PASSWD;
	        String url=config_File_Receiver.DBURL;
	        Class.forName(config_File_Receiver.DB_DRIVER);
	    	
	        //��ȣȭ
			if("Y".equals(config_File_Receiver.PASSWARD_YN)) {
				pw = enc.getJasyptDecryptedFixString(ALGORITHM, KEYSTRING, pw);
			}
	    	
			//LOGGER.info("driver load success");
	        conn=DriverManager.getConnection(url,user,pw);
	        //LOGGER.info("DBConnectsuccess");
	 }catch(Exception e){
	        LOGGER.error(e);
	        e.printStackTrace();
	    }
	    return conn;
	
	
	
	  }
	
	
}
