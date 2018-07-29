package com.qerreference.quartz.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.qerreference.quartz.scheduled.job.QuartzEmailJob;

public class QuartzReportJobListener implements JobListener{

	public static final String LISTENER_NAME = "QuartzReportJobListener";
	private static final Logger logger = LogManager.getLogger(QuartzReportJobListener.class.getName());
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return LISTENER_NAME; //must return a name
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		//System.out.println("jobToBeExecuted");
		logger.info("QuartzJobListener.jobToBeExecuted");
		logger.info("Job : " + jobName + " is going to start...");
		//System.out.println("Job : " + jobName + " is going to start...");
		
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobName = context.getJobDetail().getKey().toString();
		logger.info("QuartzReportJobListener.jobWasExecuted");
		logger.info("Job : " + jobName + " was Executed...");
		QuartzEmailJob emailJob = new QuartzEmailJob();
		//QuartzGenerateReportJob generateReportJob = new QuartzGenerateReportJob();
		try {
			emailJob.execute(context);
			//generateReportJob.execute(context);
		} catch (JobExecutionException e) {
			logger.error("QuartzReportJobListener.jobWasExecuted Exception "+e.getMessage());
			e.printStackTrace();
		}
		
	}

}
