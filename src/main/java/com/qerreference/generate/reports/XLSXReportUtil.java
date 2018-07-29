package com.qerreference.generate.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qerreference.pojo.Document;
import com.qerreference.utils.CommonUtil;
import com.qerreference.utils.QERReferenceConstants;
public class XLSXReportUtil {
	
	private static final Logger logger = LogManager.getLogger(XLSXReportUtil.class.getName());
	
	public static void generateXLXSReport(Map<String, Object[]> xlsxMap, String sheetName){

		logger.info("XLSXReportUtil.generateXLXSReport For "+sheetName+ " Sheet");
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		formatter.setTimeZone(timeZone);
		Calendar calendar = Calendar.getInstance(timeZone);
		String reportDate = formatter.format(calendar.getTime());
		String monthName = CommonUtil.getMonthNameFromDate(reportDate);

		CommonUtil utilObject = new CommonUtil();
		String reportFolderPath = utilObject.createFolderInServerWhereAppIsRunning();
		try {
			calendar.setTime(formatter.parse(reportDate));
		} catch (ParseException e1) {
			logger.error("XLSXReportUtil.generateXLXSReport.ParseException OCCURED");
			logger.error(e1.getMessage());
		}
		int xlsxReportDate = calendar.get(Calendar.DATE);
		int year = calendar.get(Calendar.YEAR);
		String reportPath = reportFolderPath.concat("Report"+"_"+String.valueOf(xlsxReportDate)+"_"+monthName.substring(0, 3)+"_"+String.valueOf(year));
		File file = new File(reportPath + ".xlsx");
		XSSFWorkbook workbook = null;
		if (file.exists()) {
            try {
				workbook = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(file));
			} catch (IllegalArgumentException e) {
				logger.error("XLSXReportUtil.generateXLXSReport.IllegalArgumentException OCCURED");
				logger.error(e.getMessage());
				//file.delete();
				//generateXLXSForBusinessReport(xlsxMap,sheetName);
			}catch (InvalidFormatException e) {
				logger.error("XLSXReportUtil.generateXLXSReport.InvalidFormatException OCCURED");
				logger.error(e.getMessage());
			} catch (FileNotFoundException e) {
				logger.error("XLSXReportUtil.generateXLXSReport.FileNotFoundException OCCURED");
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error("XLSXReportUtil.generateXLXSReport.IOException OCCURED");
				logger.error(e.getMessage());
			} catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
				logger.error("XLSXReportUtil.generateXLXSReport.ParseException OCCURED");
				e.printStackTrace();
			}
        } else {
            workbook = new XSSFWorkbook();
        }
		// Create blank workbook
		//XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet(sheetName);
		// Create row object
		XSSFRow row;
		SimpleDateFormat docDateFormatter = new SimpleDateFormat(QERReferenceConstants.OTHER_DATE_FORMAT);
		docDateFormatter.setTimeZone(timeZone);
		// Iterate over data and write to sheet
		Set<String> keyid = xlsxMap.keySet();
		int rowid = 0;
		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = xlsxMap.get(key);			
			int cellid = 0;
			String question = "";
			int columnIndex = -1;
			for (Object obj : objectArr) {
				columnIndex++;
				XSSFCell cell = row.createCell(cellid++);
				if( obj instanceof List) {
					List<Document> qList = (List) obj;
					for(Document q : qList){
						if(q.getQuestion()!= null && !q.getQuestion().isEmpty()){
							
							question = question+ "\n "+q.getQuestion();						
						}
					}
					XSSFCellStyle cs = workbook.createCellStyle();
				    cs.setWrapText(true);
				    cell.setCellStyle(cs);
					cell.setCellValue(question);
				}
				if(obj instanceof Date){					
					String date = docDateFormatter.format(obj);
					cell.setCellValue(date);
				}
				if (obj instanceof String){					
					cell.setCellValue((String) obj);
				}
				if(rowid != QERReferenceConstants.START_ROW_INDEX &&  columnIndex == QERReferenceConstants.COLUMN_INDEX_OF_NUMBER_OF_MESSAGES){
					FormulaEvaluator fev = workbook.getCreationHelper().createFormulaEvaluator();
			        String value = cell.getStringCellValue();            
			        cell.setCellFormula("VALUE(" + value + ")");
			        fev.evaluateInCell(cell);
				}
			}
		}
		// Write the workbook in file system
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			
		} catch (FileNotFoundException e) {
			logger.error("XLSXReportUtil.generateXLXSForBusinessReport() method FileNotFoundException OCCURED");
			logger.error(e.getMessage());			
		} catch (IOException e) {
			logger.error("XLSXReportUtil.generateXLXSForBusinessReport() method IOException OCCURED");
			logger.error(e.getMessage());			
		}	
	}
}
