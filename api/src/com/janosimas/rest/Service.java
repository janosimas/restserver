package com.janosimas.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

@Path("/jobs")
public class Service {
	static Map<String, Job> jobs = new HashMap<String, Job>();
	static Scheduler scheduler = null;

	/**
	 * REST method to retrieve the list of running jobs.
	 * 
	 * @return JSonArray of running jobs
	 */
	@GET
	@Produces("application/json")
	public Response getJobs() {
		return Response.status(200).entity(jobs.values().toString()).build();
	}

	/**
	 * REST method to a job.
	 * 
	 * The message should be in the format: {"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}
	 * 
	 * - job-name is a unique identifier of the job.
	 * - cron is a cron-style string of the job schedule. 
	 * - msg is the message the will be printed as scheduled in cron. 
	 * 
	 * @param msg JSon data of the job.
	 */
	@POST
	@Path("/")
	public void addJob(String msg) {
		try {
			if (scheduler == null) {
				SchedulerFactory schfa = new StdSchedulerFactory();
				scheduler = schfa.getScheduler();
				scheduler.start();
			}

			JSONObject jobJson = new JSONObject(msg);
			Job job = new Job(jobJson, scheduler);
			String jobName = job.getName();

			if (jobs.containsKey(jobName)) {
				//if there is a job with the same name, stop
				jobs.get(jobName).stop();
			}

			jobs.put(jobName, job);

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * REST method to remove a job
	 * 
	 * @param name Unique identifier of the job to be removed.
	 */
	@DELETE
	@Path("/{param}")
	public void removeJob(@PathParam("param") String name) {
		try {
			jobs.get(name).stop();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		jobs.remove(name);
	}
}
