package com.util;
/*
* ����: ��¥�� ������ �����ִ� Ŭ�����̴�.
*		�밳 ��¥�� MID�� ������ �����̸��������� ����ϴ°͵� ó�����ش�.
*		�α� ���� �̸��̴��� Ȥ�� MID���� ���� �̸��� �����ش�.
*/

import java.util.*;
import java.text.*;

/**
 * Log������ ��¥ ǥ�� Ŭ����
 * @version 1.0
 * @author ymkim
 */
public class LogDateWriter
{
	/**
	 * ���� ��¥�� ǥ���Ѵ�.
	 * @version 1.0
	 * @author ymkim
	 * @return String ���� ��¥�� ǥ���Ѵ�.
	 */
	public static String getDisplayTime()
	{
		DateFormat fmt = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT,
				Locale.getDefault());
		return fmt.format(new Date());
	}

	/**
	 * ���� ��¥�� ǥ���Ѵ�.
	 * @version 1.0
	 * @author ymkim
	 * @return String ���� ��¥�� ǥ���Ѵ�.
	 */
	public static String getLogTime()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		return fmt.format(new Date());
	}

	/**
	 * ���� ��¥�� ǥ���Ѵ�.(�α����Ͽ�)
	 * @version 1.0
	 * @author ymkim
	 * @return String ���� ��¥�� ǥ���Ѵ�.
	 */
	public static String getResultLogTime()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
		return fmt.format(new Date());
	}

	/**
	 * ���� ��¥�� ǥ���Ѵ�.(��¥ ������)
	 * @version 1.0
	 * @author ymkim
	 * @return String ���� ��¥�� ǥ���Ѵ�.
	 */
	public static String getLogFolderName()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy_MM_dd", Locale.US);
		return fmt.format(new Date());
	}

	/**
	 * ���� ��¥�� ǥ���Ѵ�.(�����)
	 * @version 1.0
	 * @author ymkim
	 * @return String ���� ��¥�� ǥ���Ѵ�.
	 */
	public static String getMailFormatTime()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);

		StringBuffer sb = new StringBuffer();
		return (sb.append(fmt.format(new Date())).append(' ').append(initTimeZone())).toString();
	}

	/**
	 * TimeZone�� ��´�.
	 * @version 1.0
	 * @author ymkim
	 * @return String TimeZone�� ��´�.
	 */
	private static String initTimeZone()
	{
		DecimalFormat numberFormat = new DecimalFormat("00");
		StringBuffer sb = new StringBuffer();
		TimeZone timeZone = TimeZone.getDefault();
		int zoneOffset = timeZone.getRawOffset();
		String zoneSign = "+";
		if( zoneOffset < 0 )
		{
			zoneOffset = -zoneOffset;
			zoneSign = "-";
		}

		double zoneMinuteOffset = zoneOffset / 60000;
		sb.append(zoneSign);
		sb.append(numberFormat.format(zoneMinuteOffset / 60));
		sb.append(numberFormat.format(zoneMinuteOffset % 60));
		return sb.toString();
	}
}