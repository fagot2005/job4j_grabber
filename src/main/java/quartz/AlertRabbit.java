package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    public static void main(String[] args) {
        AlertRabbit alertRabbit = new AlertRabbit();
        int interval = alertRabbit.readProperties("./src/main/resources/rabbit.properties");
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }

    private int readProperties(String patch) {
        int resalt = 0;
        String[] temp = new String[2];
        try (BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/rabbit.properties"))) {
            do {
                temp = in.readLine().split("=");
                if (temp[0].equals("rabbit.interval")) {
                    resalt = Integer.parseInt(temp[1]);
                    break;
                }
            } while (temp.length != 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return resalt;
    }
}
