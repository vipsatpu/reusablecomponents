package com.qerreference.quartz.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.qerreference.quartz.scheduled.job.QuartzUploadJob;

public class GenerateReportJobListener implements JobListener {

	public static final String LISTENER_NAME = "GenerateReportJobListener";
	private static final Logger logger = LogManager.getLogger(GenerateReportJobListener.class.getName());
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return LISTENER_NAME; //must return a name
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		//System.out.println("jobToBeExecuted");
		logger.info("GenerateReportJobListener.jobToBeExecuted");
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
		logger.info("GenerateReportJobListener.jobWasExecuted");
		logger.info("Job : " + jobName + " was Executed...");
		QuartzUploadJob uploadJob = new QuartzUploadJob();
		try {
			uploadJob.execute(context);
		} catch (JobExecutionException e) {
			logger.error("GenerateReportJobListener.jobWasExecuted Exception "+e.getMessage());
			e.printStackTrace();
		}		
	}
}
