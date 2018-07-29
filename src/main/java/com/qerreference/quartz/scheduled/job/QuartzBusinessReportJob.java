package com.qerreference.quartz.scheduled.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class QuartzBusinessReportJob implements Job {

	private static final Logger logger = LogManager.getLogger(QuartzBusinessReportJob.class.getName());
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		 logger.info("QuartzBusinessReportJob execute Method Started... ");
		 System.out.println("Java web application + Quartz 2.2.1");
		 //Either write business logic or Call Business Logic Methods 
	}

}
