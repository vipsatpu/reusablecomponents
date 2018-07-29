package com.qerreference.generate.reports;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qerreference.utils.CommonUtil;
import com.qerreference.utils.QERReferenceConstants;

public class CSVReportUtil {

	private static final Logger logger = LogManager.getLogger(CSVReportUtil.class.getName());
	private static final String reportFolderPath = CommonUtil.createReportFolderForReports();
	/**
	 * Code to generate CSV Report.
	 * 
	 * @param fileName
	 * @param xlsxMap
	 * @throws IOException
	 */
	public static void generateCSVReport(String fileName, Map<String, Object[]> xlsxMap) {

		logger.info(":::::::::: CSVReportUtil.CSVReportUtil Method Start :::::::::::: ");
		SimpleDateFormat docDateFormatter = new SimpleDateFormat(QERReferenceConstants.OTHER_DATE_FORMAT);
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		docDateFormatter.setTimeZone(timeZone);
		logger.info("GENERATE_CSV_REPORT_FOR_" + fileName);
		FileWriter writer = null;
		try {

			writer = new FileWriter(fileName);
			Set<String> keyid = xlsxMap.keySet();
			for (String key : keyid) {

				Object[] objectArr = xlsxMap.get(key);
				int obLenght = objectArr.length;
				for (Object obj : objectArr) {
					if(obj instanceof Date){
						
						//String nzTime = CommonUtil.isItNZDT((Date)obj);
						String date = docDateFormatter.format(obj);
						//String formatedDate = date.substring(0,19)+ nzTime + date.substring(26, 30);
						writer.append(date);
					}
					else{						
						writer.append(String.valueOf(obj));
					}
					if (obLenght > 1) {
						writer.append(',');
						obLenght--;
					}
				}
				writer.append('\n');
			}
			logger.info("CSV_REPORT_GENERATED_SUCCESSFULLY_FOR_" + fileName);
		} catch (IOException e) {
			logger.debug("Exception Occured in generateCSVReport Method " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.debug("Exception Occured in generateCSVReport Method " + e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info(":::::::::: CSVReportUtil.CSVReportUtil Method END :::::::::::: ");
	}
}