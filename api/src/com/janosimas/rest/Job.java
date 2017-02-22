package com.janosimas.rest;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import org.json.JSONObject;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

public class Job {
	String name;
	String msg;
	String cron;
	JSONObject json;
	
	JobKey key = null;
	Scheduler scheduler = null;
	
	public Job(JSONObject json, Scheduler scheduler) throws SchedulerException{
		
		this.json = json;
		this.scheduler = scheduler;
		name = json.getString("name");
		msg = json.getString("msg");
		cron = json.getString("cron");
		
		createCronJob();
	}
	
	
	/**
	 * Create a CronJob and add to the schedule.
	 * 
	 * @throws SchedulerException
	 */
	private void createCronJob() throws SchedulerException {
		//sanity check
		if(key != null)
			return;
		
		//Create a CrongJob with the message received in the json.
		JobDetail jobdetail = JobBuilder.newJob(CronJob.class)
			    .withIdentity(name, "group")
			    .usingJobData("msg", msg).build();
		key = jobdetail.getKey();
		
		//Trigger based on the cron string.
		CronTrigger crontrigger = TriggerBuilder.newTrigger().withIdentity("trigger_"+name, "crongroup1")
			    .withSchedule(cronSchedule(cron)).build();
		
		//add to the schedule
		scheduler.scheduleJob(jobdetail, crontrigger);
	}

	public String getName(){
		return name;
	}
	
	@Override
	public String toString(){
		return json.toString();
	}

	/**
	 * Removes the job from the schedule
	 * 
	 * @throws SchedulerException
	 */
	public void stop() throws SchedulerException {
		scheduler.deleteJob(key);
		key = null;
	}
}
