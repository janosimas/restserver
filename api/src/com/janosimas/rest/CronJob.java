package com.janosimas.rest;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * Simple Job the prints the string in the "msg" key.
 * 
 * @author janosimas
 *
 */
public class CronJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String msg = dataMap.getString("msg");
		
		System.out.println(msg);
	}

}
