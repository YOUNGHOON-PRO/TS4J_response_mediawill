/**
 * Ŭ������: Config_File_Receiver.java
 * ��������: JDK 1.4.1
 * ��༳��: ���� ����
 * �ۼ�����: 2003-04-04 �ϱ���
 */


//2003.10.1 ���ǰ�ħ
//�̱��� �𵨷� �ٲ�
package com.config;

import java.io.*;
import java.util.*;

import com.log.LogWriter;
import com.util.EncryptUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Config_File_Receiver
{
	
	private static final Logger LOGGER = LogManager.getLogger(Config_File_Receiver.class.getName());
	
	private static Config_File_Receiver instance;

	LogWriter logWriter;

	public static int Log_Write_Period;
	public static int Dir_Delete_Period;
	public static String Log_Sep;

        //DB Configuration
	public static String DB_DRIVER;
	public static String DBURL;
	public static String USER;
	public static String PASSWD;
	public static String PASSWARD_YN;

	public static String RESPONSE_CONFIRM_FULL_PATH;
	public static String RESPONSE_LOG_PATH;
	public static String ENC_YN;
	
	public static int RESPONSE_INSERT_PERIOD;
	
	public static int NeoSMTP_Agent;
	public static String Root_Dir[];

	public static String MERGE_QUEUE_FOLDER;

	//2003.10.22���� �߰�
	public static int GARBAGE_START_TIME;

	StringTokenizer st;

	//��ü�� ��´�.
	public static Config_File_Receiver getInstance()
	{
		if( instance == null )//���� �����
		{
			instance = new Config_File_Receiver();
			return instance;

		}
		else	//�ι�° ����...
		{
			return instance;
		}
	}

        /**
         *  load configuration.
         */
        public Config_File_Receiver()
	{
		logWriter = new LogWriter();

                //FileInputStream for configuration.
                FileInputStream config = null;
                FileInputStream dbconf = null;
                FileInputStream resconf = null;

                Properties props = new Properties();

		try
		{
			//configuration for work.
            //config = new FileInputStream("../config/DBRecorder.conf");
            config = new FileInputStream("./config/DBrecorder.conf");
			props.load(config);

			NeoSMTP_Agent=Integer.parseInt(props.getProperty("NeoSMTP_Agent"));

			String Root_Dir_str=props.getProperty("Root_Dir");
			st=new StringTokenizer(Root_Dir_str,",");

			Root_Dir=new String[NeoSMTP_Agent];

			for( int i = 0; i < NeoSMTP_Agent; i++ ) {
				Root_Dir[i]=(st.nextToken().trim());
			}

			MERGE_QUEUE_FOLDER = Root_Dir[0] + File.separator + "Merge_Queue";

			Log_Write_Period=Integer.parseInt(props.getProperty("Log_Write_Period"));
			Dir_Delete_Period=Integer.parseInt(props.getProperty("Dir_Delete_Period"));
			Log_Sep=props.getProperty("Log_Sep");

            //configuration for db connection.
            //dbconf = new FileInputStream("../config/database.conf");
            dbconf = new FileInputStream("./config/database.conf");
            props.load(dbconf);
            
            PASSWARD_YN = props.getProperty("PASSWARD_YN");
            
            DB_DRIVER=props.getProperty("DRIVER");
			DBURL=props.getProperty("URL");
			USER=props.getProperty("USER");
			PASSWD=props.getProperty("PASSWARD");
			
			//resconf = new FileInputStream("../config/Response.conf");
			resconf = new FileInputStream("./config/Response.conf");
			props.load(resconf);
			
			RESPONSE_INSERT_PERIOD = Integer.parseInt(props.getProperty("RESPONSE_INSERT_PERIOD"));
			RESPONSE_CONFIRM_FULL_PATH = props.getProperty("RESPONSE_CONFIRM_FULL_PATH");
			RESPONSE_LOG_PATH = props.getProperty("RESPONSE_LOG_PATH");
			ENC_YN = props.getProperty("ENC_YN");
			
			LOGGER.info("RESPONSE_INSERT_PERIOD : " + RESPONSE_INSERT_PERIOD);
			LOGGER.info("RESPONSE_CONFIRM_FULL_PATH : " + RESPONSE_CONFIRM_FULL_PATH);
			LOGGER.info("RESPONSE_LOG_PATH : " + RESPONSE_LOG_PATH);
			LOGGER.info("ENC_YN : " + ENC_YN);
			
		}
		catch(Exception e)
		{
			logWriter.logWrite("CONFIG_FILE_RECEIVER ERROR","construct",e);
		}finally{
                  try{
                    if (config != null)
                      config.close();
                    if (dbconf != null)
                      dbconf.close();
                  }catch(Exception e){}
                }
	}
}
