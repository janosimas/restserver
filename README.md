# Simple java rest server

This is a simple rest server to schedule and print messages in the console.

## API

- Add a Job
The message should be in the format: {"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}
	  
 - name is a unique identifier of the job.
 - cron is a cron-style string of the job schedule. 
 - msg is the message the will be printed as scheduled in cron. 
   
   ```
    curl -H "Content-Type: application/json" -X POST \
    -d '{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}' \
    http://localhost:8080/api/jobs
    ```
    
- Get a list of jobs

```
    curl http://localhost:8080/api/jobs
    #   [{"name": "job-name", "msg": "Hello World", "cron": "* * * * *"}]
```
    
- Remove a job
```
    curl -X DELETE http://localhost:8080/api/jobs/job-name
```

## Libraries

To create the rest service I used the jersey library and quartz-scheduler for scheduling.
