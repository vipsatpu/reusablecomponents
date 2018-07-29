package com.qerreference.quartz.servletcontext.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import com.qerreference.quartz.job.listener.QuartzReportJobListener;
import com.qerreference.utils.QERReferenceConstants;
import com.qerreference.utils.QuartzSchedulerData;

public class QuartzListener implements ServletContextListener {

	Scheduler scheduler = null;
	private static final Logger logger = LogManager.getLogger(QuartzListener.class.getName());
	private static final Properties yourAppKey = new Properties();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("Scheduler Contenxt Initialized");
		
		try {
			InputStream keyStream = sce.getServletContext()
					.getResourceAsStream(QERReferenceConstants.PROPERTY_FILE_PATH);
			yourAppKey.load(keyStream);
			logger.info("Properties File has been Initialised");
			Map<JobDetail,Trigger> scheduledTaskMap = QuartzSchedulerData.getSchduledJob(yourAppKey,sce);			
			// Setup the Job and Trigger with Scheduler & schedule jobs
			scheduler = new StdSchedulerFactory().getScheduler();
			for (Map.Entry<JobDetail,Trigger> entry : scheduledTaskMap.entrySet()){				
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				scheduler.scheduleJob(entry.getKey(), entry.getValue());				
			}			
			JobKey jobKeyReport = new JobKey("cron_job_class_1", QERReferenceConstants.QUARTZ_GROUP);
			scheduler.getListenerManager().addJobListener(
		    		new QuartzReportJobListener(), KeyMatcher.keyEquals(jobKeyReport)
		    	);
			scheduler.start();
			/*JobKey jobKeyDashboardReport = new JobKey("cron_job_class_3", QERReferenceConstants.QUARTZ_GROUP);
			scheduler.getListenerManager().addJobListener(
		    		new GenerateReportJobListener(), KeyMatcher.keyEquals(jobKeyDashboardReport)
		    	);
			JobKey jobKeyUpload = new JobKey("cron_job_class_4", QERReferenceConstants.QUARTZ_GROUP);
			scheduler.getListenerManager().addJobListener(
		    		new UploadJobListener(), KeyMatcher.keyEquals(jobKeyUpload)
		    	);*/			
			/*JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("servletContext", sce.getServletContext());
            
			// Setup the Job class and the Job group
			JobDetail reportJob = newJob(QuartzreportJob.class).withIdentity("reportJob", "Group").usingJobData(jobDataMap).build();

			// Create a Trigger that fires every 10 minutes. 0 0/10 * 1/1 * ? *
			// Create a Trigger that fires Weekly on Friday @ 9:30AM (0 30 9 ? * FRI *).
			Trigger reportJobTrigger = newTrigger().withIdentity("ReportTrigger", "Group")
					.withSchedule(CronScheduleBuilder.cronSchedule(yourAppKey.getProperty("CronJob_For_ReportTrigger"))).build();

			// Setup the Job class and the Job group
			JobDetail reportEmailSendJob = newJob(QuartzEmailJob.class).withIdentity("EmailJob", "Group").usingJobData(jobDataMap).build();

			// Create a Trigger that fires Weekly on Friday @ 9:30AM (0 30 9 ? * FRI *).
			Trigger reportEmailSendJobTrigger = newTrigger().withIdentity("EmailTrigger", "Group")
					.withSchedule(CronScheduleBuilder.cronSchedule(yourAppKey.getProperty("CronJob_For_EmailTrigger"))).build();

			// Setup the Job and Trigger with Scheduler & schedule jobs
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(reportJob, reportJobTrigger);
			scheduler.scheduleJob(reportEmailSendJob, reportEmailSendJobTrigger);*/

		} catch (SchedulerException | IOException e) {
			logger.error("Scheduler Exception in Contenxt Initialized " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("Scheduler contextDestroyed Method");
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error("Scheduler Exception in contextDestroyed Method " + e.getMessage());
			e.printStackTrace();
		}
	}

}
