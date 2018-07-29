package com.qerreference.quartz.scheduled.job;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.qerreference.emailsender.EmailSender;
import com.qerreference.utils.CommonUtil;

public class QuartzEmailJob implements Job {

	private static final Logger logger = LogManager.getLogger(QuartzEmailJob.class.getName());

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("Quartz EmailJob execute Method Started... ");
		ServletContext servletContext = (ServletContext) context.getMergedJobDataMap().get("servletContext");
		logger.info("Properties File has been Initialised");
		CommonUtil utilObject = new CommonUtil();
		String reportFolderPath = utilObject.createFolderInServerWhereAppIsRunning();
		try {
			EmailSender emailSender = new EmailSender();
			emailSender.sendEmailWithAttachments(reportFolderPath, servletContext);
		} catch (Exception e) {
			logger.error("QuartzEmailJob.execute() Method Exception " + e.getMessage());
		}
		/*finally{
			QuartzGenerateReportJob reportJob = new QuartzGenerateReportJob();
			reportJob.execute(context);
		}*/
	}
}
