package com.qerreference.quartz.scheduled.job;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzUploadJob implements Job {

	private static final Logger logger = LogManager.getLogger(QuartzGenerateReportJob.class.getName());
	private static final Properties propertyData = new Properties();
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("QuartzUploadJob.execute() get Called...");
		System.out.println("...Start Login & Upload Process For WA...");
		ServletContext servletContext = (ServletContext) context.getMergedJobDataMap().get("servletContext");
		/*InputStream keyStream = servletContext.getResourceAsStream(QERReferenceConstants.PROPERTY_FILE_PATH);
		try {
			propertyData.load(keyStream);
			WALoginUpload loginUpload = new WALoginUpload();
			loginUpload.demoOAuth2Code(propertyData);
		} catch (IOException e) {
			logger.info("QuartzUploadJob.execute() Method Exception..."+e.getMessage());
			e.printStackTrace();
		}*/		
	}
}
