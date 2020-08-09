package quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public interface Job {
    public void execute(JobExecutionContext context) throws JobExecutionException;
}