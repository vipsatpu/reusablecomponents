package com.qerreference.quartz.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.qerreference.quartz.scheduled.job.QuartzEmailJob;

public class UploadJobListener implements JobListener {

	public static final String LISTENER_NAME = "UploadJobListener";
	private static final Logger logger = LogManager.getLogger(UploadJobListener.class.getName());
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return LISTENER_NAME; //must return a name
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		String jobName = context.getJobDetail().getKey().toString();
		//System.out.println("jobToBeExecuted");
		logger.info("UploadJobListener.jobToBeExecuted");
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
		logger.info("UploadJobListener.jobWasExecuted");
		logger.info("Job : " + jobName + " was Executed...");
		context.getJobDetail().getJobDataMap().put("emailWithAttachment", false);
		QuartzEmailJob emailJob = new QuartzEmailJob();
		try {
			emailJob.execute(context);
		} catch (JobExecutionException e) {
			logger.error("UploadJobListener.jobWasExecuted Exception "+e.getMessage());
			e.printStackTrace();
		}
		
	}

}
