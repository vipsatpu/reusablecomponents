package com.qerreference.quartz.scheduled.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzGenerateReportJob implements Job {

	private static final Logger logger = LogManager.getLogger(QuartzGenerateReportJob.class.getName());
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("QuartzGenerateReportJob.execute() get Called...");
		System.out.println("...Start Generating  Reports...");
		//Cal your Code
	}
}
