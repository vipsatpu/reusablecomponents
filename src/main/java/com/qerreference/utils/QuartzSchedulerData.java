package com.qerreference.utils;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;

public class QuartzSchedulerData {

	private static final Logger logger = LogManager.getLogger(QuartzSchedulerData.class.getName());

	public static Map<JobDetail, Trigger> getSchduledJob(Properties properties, ServletContextEvent sce) {

		logger.info("QuartzSchedulerData.getSchduledJob(Properties properties, ServletContextEvent sce) get Called...");
		Map<JobDetail, Trigger> jobTriggerMap = new LinkedHashMap<JobDetail, Trigger>();
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("servletContext", sce.getServletContext());
		int totalQuartzJob = Integer.parseInt(properties.getProperty(QERReferenceConstants.TOTAL_CRON_JOB));
		for (int i = 1; i <= totalQuartzJob; i++) {
			String jobDetailKey = QERReferenceConstants.CRON_JOB_CLASS.concat(String.valueOf(i));
			String jobTriggerKey = (QERReferenceConstants.CRON_JOB_CLASS.concat(String.valueOf(i)))
					.concat(QERReferenceConstants.TRIGGER);
			String jobDetail = properties.getProperty(jobDetailKey);
			String jobTrigger = properties.getProperty(jobTriggerKey);
			Class jobDetailClass = null;
			JobDetail quartzJobDetail = null;
			Trigger quartzJobTrigger = null;
			try {
				jobDetailClass = Class.forName(jobDetail);
				// Setup the Job class and the Job group
				quartzJobDetail = newJob(jobDetailClass).withIdentity(jobDetailKey, QERReferenceConstants.QUARTZ_GROUP)
						.usingJobData(jobDataMap).build();
				// Create a Trigger that uses cron_job_class_i_trigger .
				quartzJobTrigger = newTrigger().withIdentity(jobTriggerKey, QERReferenceConstants.QUARTZ_GROUP)
						.withSchedule(CronScheduleBuilder.cronSchedule(jobTrigger)).build();
				jobTriggerMap.put(quartzJobDetail, quartzJobTrigger);
				
			} catch (ClassNotFoundException e) {
				logger.info("QuartzSchedulerData.getSchduledJob() ClassNotFoundException..."+e.getMessage());
				e.printStackTrace();
			}
		}
		return jobTriggerMap;
	}
}
