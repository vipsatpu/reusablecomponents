package com.qerreference.emailsender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;

import com.qerreference.utils.CommonUtil;
import com.qerreference.utils.QERReferenceConstants;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

public class EmailSender {

	private static final Logger logger = LogManager.getLogger(EmailSender.class.getName());
	private static final Properties propertyData = new Properties();
	
	public void sendEmailWithAttachments(String reportFolderPath, ServletContext servletContext) {
		logger.info("EmailSender.sendEmailWithAttachments Method Called....");
		TimeZone timeZone = TimeZone.getTimeZone(QERReferenceConstants.DEFAULT_TIME_ZONE);
		SimpleDateFormat formatter = new SimpleDateFormat(QERReferenceConstants.CONVERT_DATE_FORMAT);
		formatter.setTimeZone(timeZone);
		Calendar calendar = Calendar.getInstance(timeZone);
		String reportDate = formatter.format(calendar.getTime());
		String monthName = CommonUtil.getMonthNameFromDate(reportDate);
		try {

			InputStream keyStream = servletContext.getResourceAsStream(QERReferenceConstants.PROPERTY_FILE_PATH);
			propertyData.load(keyStream);
			int businessReportDate = calendar.get(Calendar.DATE);
			int year = calendar.get(Calendar.YEAR);
			String weekDate = String.valueOf(businessReportDate)+"_"+monthName.substring(0, 3)+"_"+String.valueOf(year);
			String reportPath = reportFolderPath.concat("Report"+"_"+weekDate);
			File file = new File(reportPath + ".xlsx");			
			byte[] fileData = null;
			if (file.exists()) {
				
				Mail mail = new Mail();
				Personalization personalization = new Personalization();
				String toEmail = propertyData.getProperty("ToEmail");
				String ccEmail = propertyData.getProperty("ccEmail");
				StringTokenizer st = new StringTokenizer(toEmail,",");
				while (st.hasMoreTokens()) {  
			         personalization.addTo(new Email(st.nextToken()));
			     }
				st = null;
				st = new StringTokenizer(ccEmail,",");
				while (st.hasMoreTokens()) { 
			         personalization.addCc(new Email(st.nextToken()));
			     }
				//personalization.addCc(new Email(propertyData.getProperty("CCEmail")));
				//personalization.addCc(new Email(propertyData.getProperty("ccEmail")));

				personalization.setSubject(propertyData.getProperty("email_subject_for").concat(weekDate));
				Content content = new Content("text/plain", propertyData.getProperty("email_content_for").concat(weekDate));

				mail.setFrom(new Email(propertyData.getProperty("FromEmail")));
				mail.addPersonalization(personalization);
				mail.addContent(content);

				Attachments attachments = new Attachments();
		        Base64 x = new Base64();
		        //fileData = org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(file));
		        String xlsDataString = x.encodeAsString(fileData);
		        attachments.setContent(xlsDataString);
		        attachments.setType(propertyData.getProperty("Content-Type-XLSX"));//"application/pdf"
		        attachments.setFilename("Report"+"_"+weekDate+".xlsx");
		        attachments.setDisposition("attachment");
		        attachments.setContentId("Report Sheet");
		        mail.addAttachments(attachments);
		        		        
				SendGrid sg = new SendGrid(propertyData.getProperty("SENDGRID_API_KEY"));
				Request req = new Request();

				try {
					req.method = Method.POST;
					req.endpoint = "mail/send";
					req.body = mail.build();
					com.sendgrid.Response res = sg.api(req);

					logger.info("Response Code: " + res.statusCode);
					logger.info("Response Body: " + res.body);
					logger.info("Response Header: " + res.headers);
					String responseContentType = res.headers.get("Content-Type");
					System.out.println("Response Content Type :"+responseContentType);
					logger.info("Response Content Type :"+responseContentType);

				} catch (IOException ex) {
					logger.error("IOException "+ex.getMessage());
					ex.printStackTrace();
					try {
						Thread.sleep(300000);//sleep for 10 mins
					} catch (InterruptedException e1) {
						logger.error("InterruptedException "+e1.getMessage());
						e1.printStackTrace();
					} 
		            JobExecutionException e2 = new JobExecutionException(ex);
		            //fire it again
		            e2.setRefireImmediately(true);
		            throw e2;
				}
			}
			else{
				logger.warn("File does not exist");
				JobExecutionException e2 = new JobExecutionException("File does not exist");
				//fire it again
	            e2.setRefireImmediately(true);
	            throw e2;
			}
		} catch (Exception e) {
			logger.error("Exception "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Send Email for Dashbaord Updates
	 * @param propertydata2 
	 * @throws JobExecutionException 
	 */
	public void sendEmail(Properties propertydata2) throws JobExecutionException{
		logger.info("EmailSender.sendEmail() Method get Called...");
		Mail mail = new Mail();
		Personalization personalization = new Personalization();
		String toEmail = propertydata2.getProperty("ToEmail_Dashboard");
		String ccEmail = propertydata2.getProperty("ccEmail_Dashboard");
		StringTokenizer st = new StringTokenizer(toEmail,",");
		while (st.hasMoreTokens()) {  
	         personalization.addTo(new Email(st.nextToken()));
	     }
		st = null;
		st = new StringTokenizer(ccEmail,",");
		while (st.hasMoreTokens()) { 
	         personalization.addCc(new Email(st.nextToken()));
	     }
		//personalization.addCc(new Email(propertyData.getProperty("CCEmail")));
		//personalization.addCc(new Email(propertyData.getProperty("ccEmail")));

		personalization.setSubject(propertydata2.getProperty("email_subject_for_Dashboard"));
		Content content = new Content("text/plain", propertydata2.getProperty("email_content_for_Dashboard"));

		mail.setFrom(new Email(propertydata2.getProperty("FromEmail")));
		mail.addPersonalization(personalization);
		mail.addContent(content);
		
		SendGrid sg = new SendGrid(propertydata2.getProperty("SENDGRID_API_KEY"));
		Request req = new Request();

		try {
			req.method = Method.POST;
			req.endpoint = "mail/send";
			req.body = mail.build();
			com.sendgrid.Response res = sg.api(req);

			logger.info("Response Code: " + res.statusCode);
			logger.info("Response Body: " + res.body);
			logger.info("Response Header: " + res.headers);
			String responseContentType = res.headers.get("Content-Type");
			System.out.println("Response Content Type :"+responseContentType);
			logger.info("Response Content Type :"+responseContentType);

		} catch (IOException ex) {
			logger.error("EmailSender.sendEmail() Exception"+ex.getMessage());
			ex.printStackTrace();
			try {
				Thread.sleep(300000);//sleep for 5 mins
			} catch (InterruptedException e1) {
				logger.error("InterruptedException "+e1.getMessage());
				e1.printStackTrace();
			} 
            JobExecutionException e2 = new JobExecutionException(ex);
            //fire it again
            e2.setRefireImmediately(true);
            throw e2;
		}
		
	}
}
