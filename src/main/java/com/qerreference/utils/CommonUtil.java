package com.qerreference.utils;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class CommonUtil {
	
private static final Logger logger = LogManager.getLogger(CommonUtil.class.getName());	
	
	public static String getFormatedDate(String conversationDate) {
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		SimpleDateFormat docDateFormatter = new SimpleDateFormat(QERReferenceConstants.DOCUMENT_DATE_FORMAT, Locale.US);
		formatter.setTimeZone(timeZone);
		docDateFormatter.setTimeZone(timeZone);
		Date documentDate = null;
		String docDate = null;
		try {
			documentDate = (Date) docDateFormatter.parse(getNewZealandTime(conversationDate));
			docDate = formatter.format(documentDate);
		} catch (ParseException e) {
			logger.error("CommonUtil.getFormatedDate() Exception :- "+e.getMessage());
			e.printStackTrace();
		}
		return docDate;
	}

	public static String getNewZealandTime(String docDate) throws ParseException {

		SimpleDateFormat docDateFormatter = new SimpleDateFormat(QERReferenceConstants.DOCUMENT_DATE_FORMAT, Locale.US);
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		Calendar calendar = Calendar.getInstance(timeZone);

		docDateFormatter.setTimeZone(timeZone);
		Date nzDate = docDateFormatter.parse(docDate);

		Date nzDayLightSavingStart = docDateFormatter.parse(QERReferenceConstants.NZ_DAY_LIGHT_SAVING_START_DATE);
		Date nzDayLightSavingEnd = docDateFormatter.parse(QERReferenceConstants.NZ_DAY_LIGHT_SAVING_END_DATE);
		calendar.setTime(nzDate);

		if (nzDate.before(nzDayLightSavingStart) || nzDate.after(nzDayLightSavingEnd)) {
			calendar.add(Calendar.HOUR, 12);
		}
		if (nzDate.before(nzDayLightSavingEnd) && nzDate.after(nzDayLightSavingStart)) {
			calendar.add(Calendar.HOUR, 13);
		}
		//logger.info(Messages.getString("CommonUtil.GET_NEWZEALAND_TIME_END"));
		return docDateFormatter.format(calendar.getTime());
	}
	
	/**
	 * It returns the Month Name of the Date
	 * @param date
	 * @return
	 */
	public static String getMonthNameFromDate(String date){
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		formatter.setTimeZone(timeZone);
		Date documentDate = null;
		String monthName = "";
		try {
			documentDate = formatter.parse(date);
			Calendar c = Calendar.getInstance();
			c.set(Calendar.MILLISECOND, 0);
			c.setTime(documentDate);
			int monthNum = c.get(Calendar.MONTH);
			DateFormatSymbols dfs = new DateFormatSymbols();
			String[] months = dfs.getMonths();
			if (monthNum >= 0 && monthNum <= 11) {
				monthName = months[monthNum];
			}
		} catch (ParseException e) {
			logger.error("CommonUtil.getMonthNameFromDate.ParseException OCCURED");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return monthName;
	}
	
	/**
	 * 
	 * @param documentDate
	 * @return
	 */
	public static String isItNZDT(Date documentDate){
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		SimpleDateFormat docDateFormatter = new SimpleDateFormat(QERReferenceConstants.DOCUMENT_DATE_FORMAT, Locale.US);
		docDateFormatter.setTimeZone(timeZone);
		String nzTimeZone = "";
		try {
			Date nzDayLightSavingStart = docDateFormatter.parse(QERReferenceConstants.NZ_DAY_LIGHT_SAVING_START_DATE);
			Date nzDayLightSavingEnd = docDateFormatter.parse(QERReferenceConstants.NZ_DAY_LIGHT_SAVING_END_DATE);
			
			if (documentDate.before(nzDayLightSavingStart) || documentDate.after(nzDayLightSavingEnd)) {
				nzTimeZone = QERReferenceConstants.NEW_ZEALAND_STANDARD_TIME;
			}
			if (documentDate.before(nzDayLightSavingEnd) && documentDate.after(nzDayLightSavingStart)) {
				nzTimeZone = QERReferenceConstants.NEW_ZEALAND_DAYLIGHT_TIME;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nzTimeZone;		
	}
	
	
	/**
	 * Used to create a Report folder in Resource folder of Project Structure.
	 */
	public static String createReportFolderForReports() {
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		String reportDate = formatter.format(calendar.getTime());
		File file = null;
		String reportFolderPath = "";
		String path = System.getProperty(QERReferenceConstants.PROJECT_PATH_KEY).toString();
		file = new File(path+QERReferenceConstants.REPORT_FOLDER+"_"+reportDate);
		boolean result = file.mkdir();
		if (file != null) {
			reportFolderPath = file.getPath();
			reportFolderPath = reportFolderPath.concat("\\");
		}
		return reportFolderPath;
	}
	
	/**
	 * Used to create a Report folder in Resource folder of Project Structure.
	 */
	public String createFolderInServerWhereAppIsRunning() {
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		String reportDate = formatter.format(calendar.getTime());
		File file = null;
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String pathArr[] = path.split("/WEB-INF/classes/");
		path = pathArr[0];
		String reportFolderPath = "";
		file = new File(path+QERReferenceConstants.REPORT_FOLDER+"_"+reportDate);
		boolean result = file.mkdir();
		if (file != null) {
			reportFolderPath = file.getPath();
			reportFolderPath = reportFolderPath.concat("/");
			//System.out.println(" reportFolderPath :-"+reportFolderPath);
		}
		return reportFolderPath;
	}

}